package br.com.livroandroid.buscapreco.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.livroandroid.buscapreco.BuscaPrecoApplication;
import br.com.livroandroid.buscapreco.R;
import br.com.livroandroid.buscapreco.activity.ListaEmpresa;
import br.com.livroandroid.buscapreco.activity.ProdutosActivity;
import br.com.livroandroid.buscapreco.adapter.EmpresaAdapter;
import br.com.livroandroid.buscapreco.domain.EmpresaService;
import br.com.livroandroid.buscapreco.model.Empresa;
import livroandroid.lib.utils.AndroidUtils;
import livroandroid.lib.utils.Prefs;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmpresasFragment extends BaseFragment {

    protected RecyclerView recyclerView;
    private List<Empresa> empresas;
    private EmpresaAdapter empresaAdapter;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String cidade = "", estado = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_empresa, container, false);

        cidade = Prefs.getString(getContext(),"cidade");
        estado = Prefs.getString(getContext(),"estado");

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerEmpresa);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        recyclerView.setHasFixedSize(true);

        empresaAdapter = new EmpresaAdapter(getContext(), empresas, onClickEmpresa());
        recyclerView.setAdapter(empresaAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setOnRefreshListener(OnRefreshListener());
        swipeRefreshLayout.setColorSchemeResources(
                R.color.blue,
                R.color.green,
                R.color.refresh_progress_3);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (BuscaPrecoApplication.getInstance().isNeedToUpdate("empresa")){
            taskEmpresas(false);
        }
    }

    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener(){
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Valida se existe conexão
                if (AndroidUtils.isNetworkAvailable(getContext())) {
                    taskEmpresas(true);
                }else {
                    swipeRefreshLayout.setRefreshing(false);
                    alert(R.string.error_conexao_indisponivel);
                }
            }
        };
    }

    public void show(){
        InserirCidadeDialog.show(getFragmentManager(), new InserirCidadeDialog.Callback() {
            @Override
            public void onCidadeUpdated(String cidade, String estado) {
                    /*Prefs.setString(getContext(), "cidade", cidade);
                    Prefs.setString(getContext(), "estado", estado);*/
                taskEnd(cidade, estado);
                taskEmpresas(false);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*if (Prefs.getBoolean(getContext(),"show")||Prefs.getString(getContext(),"cidade").equals("")) {
            Prefs.setBoolean(getContext(),"show",false);
            InserirCidadeDialog.show(getFragmentManager(), new InserirCidadeDialog.Callback() {
                @Override
                public void onCidadeUpdated(String cidade, String estado) {
                    //Prefs.setString(getContext(), "cidade", cidade);
                    //Prefs.setString(getContext(), "estado", estado);
                    taskEnd(cidade, estado);
                    taskEmpresas(false);
                }
            });
        } else
            taskEmpresas(false);*/

        if (Prefs.getString(getContext(),"cidade").equals("")) {
            //Prefs.setBoolean(getContext(),"show",false);
            InserirCidadeDialog.show(getFragmentManager(), new InserirCidadeDialog.Callback() {
                @Override
                public void onCidadeUpdated(String cidade, String estado) {
                    /*Prefs.setString(getContext(), "cidade", cidade);
                    Prefs.setString(getContext(), "estado", estado);*/
                    taskEnd(cidade, estado);
                    taskEmpresas(false);
                }
            });
        } else
            taskEmpresas(false);
    }

    private void taskEnd(String cidade, String estado){
        this.cidade=cidade;
        this.estado=estado;
    }

    private void taskEmpresas(boolean pullToRefresh){
        //empresas = Empresa.getEmpresas();
        //empresaAdapter = new EmpresaAdapter(getContext(), empresas, onClickEmpresa());
        //recyclerView.setAdapter(empresaAdapter);
        startTask("empresas", new GetEmpresaTask(pullToRefresh), pullToRefresh ? R.id.swipeToRefresh : R.id.progress);
    }

    private class GetEmpresaTask implements TaskListener<List<Empresa>>{
        private boolean refresh;

        public GetEmpresaTask(boolean refresh) {
            this.refresh = refresh;
        }

        @Override
        public List<Empresa> execute() throws Exception {
            return EmpresaService.getEmpresas(getContext(),
                    cidade,
                    estado,refresh);
        }

        @Override
        public void updateView(List<Empresa> empresas) {
            if (empresas!=null){
                Toast.makeText(getContext(), empresas.size() + " Empresa(s) encontrada(s)!", Toast.LENGTH_LONG).show();
                Prefs.setString(getContext(), "cidade", cidade);
                Prefs.setString(getContext(), "estado", estado);
                EmpresasFragment.this.empresas=empresas;
                empresaAdapter = new EmpresaAdapter(getContext(), empresas, onClickEmpresa());
                recyclerView.setAdapter(empresaAdapter);
            } else
                Toast.makeText(getContext(), "Não foi encontrado nenhuma empresa", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(Exception exception) {
            alert("Ocorreu algum erro ao buscar os dados");
        }

        @Override
        public void onCancelled(String cod) {

        }
    }

    private EmpresaAdapter.EmpresaOnClickListener onClickEmpresa() {
        return new EmpresaAdapter.EmpresaOnClickListener() {
            @Override
            public void onClickEmpresa(EmpresaAdapter.EmpresasViewHolder holder, int idx) {
                Empresa e = empresas.get(idx-1);
                Intent intent = new Intent(getContext(),ProdutosActivity.class);
                intent.putExtra("empresa",e);
                startActivity(intent);
            }

            @Override
            public void onClickCheckBox(EmpresaAdapter.EmpresasViewHolder holder, int idx, CheckBox cb) {
                Empresa e = empresaAdapter.getEmpresas().get(idx-1);
                //Prefs.setBoolean(getContext(),String.valueOf(e.getId()),cb.isChecked());
                if (cb.isChecked()) {
                    EmpresaService.salvarEmpresaFav(getContext(), e);
                    toast("Empresa adicionada aos favoritos!");
                }
                else{
                    EmpresaService.deletarEmpresaFav(getContext(),e);
                    toast("Empresa removida dos favoritos!");
                }
            }

            @Override
            public void onClickInfo(EmpresaAdapter.EmpresasViewHolder holder, int idx) {
                Empresa e = empresas.get(idx-1);
                showAbout(e);
            }
        };
    }

    public void showAbout(Empresa empresa) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog_about");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        AboutEmpresa aboutEmpresa = AboutEmpresa.newInstante(empresa);
        aboutEmpresa.show(ft, "dialog");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main,menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item); //item.getActionView();
        searchView.setInputType(1);
        searchView.setQueryHint("Digite o nome da empresa..");
        searchView.setOnQueryTextListener(onSearch());
    }

    private SearchView.OnQueryTextListener onSearch() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<Empresa> empresas = empresaAdapter.getEmpresas();
                List<Empresa> empresaFiltro = new ArrayList<>();

                for (int i=0;i<empresas.size();i++){
                    if (empresas.get(i).getNome().toLowerCase().contains(query.toLowerCase())){
                        empresaFiltro.add(empresas.get(i));
                    }
                }

                if (empresaFiltro.size()>0) {
                    Intent intent = new Intent(getContext(),ListaEmpresa.class);
                    intent.putParcelableArrayListExtra("empresas", (ArrayList<? extends Parcelable>) empresaFiltro);
                    intent.putExtra("tipo", true);
                    intent.putExtra("title","Empresas encontradas");
                    startActivity(intent);
                }else
                    toast("Nenhuma empresa com o nome pesquisado");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AboutDialog.showAbout(getFragmentManager());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}