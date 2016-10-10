package br.com.livroandroid.buscapreco.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.List;

import br.com.livroandroid.buscapreco.BuscaPrecoApplication;
import br.com.livroandroid.buscapreco.R;
import br.com.livroandroid.buscapreco.Utils.DividerItemDecoration;
import br.com.livroandroid.buscapreco.activity.ProdutosActivity;
import br.com.livroandroid.buscapreco.adapter.EmpresaAdapter;
import br.com.livroandroid.buscapreco.adapter.FavoritoAdapter;
import br.com.livroandroid.buscapreco.domain.EmpresaService;
import br.com.livroandroid.buscapreco.model.Empresa;

public class ListaEmpresaFragment extends BaseFragment {

    protected RecyclerView recyclerView;
    private List<Empresa> empresas;
    private FavoritoAdapter empresaAdapter;
    private LinearLayoutManager linearLayoutManager;
    private View view;
    private String title;

    public ListaEmpresaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        if (getArguments()!=null){
            title = (String) getArguments().get("title");
            empresas = getArguments().getParcelableArrayList("empresas");
        }
        view = inflater.inflate(R.layout.fragment_lista_empresa, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerEmpresa);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        recyclerView.setHasFixedSize(true);
        empresaAdapter = new FavoritoAdapter(getContext(), empresas, onClickEmpresa());
        recyclerView.setAdapter(empresaAdapter);
        getActionBar().setTitle(title);
        return view;
    }

    public void setEmpresa(List<Empresa> empresas){
        if (empresas!=null){
            Log.i("AQUI","AQUI");
            this.empresas = empresas;
            empresaAdapter = new FavoritoAdapter(getContext(), empresas, onClickEmpresa());
            recyclerView.setAdapter(empresaAdapter);
        }
    }

    private FavoritoAdapter.EmpresaOnClickListener onClickEmpresa() {
        return new FavoritoAdapter.EmpresaOnClickListener() {
            @Override
            public void onClickEmpresa(FavoritoAdapter.EmpresasViewHolder holder, int idx) {
                Empresa e = empresas.get(idx);
                Intent intent = new Intent(getContext(),ProdutosActivity.class);
                intent.putExtra("empresa",e);
                startActivity(intent);
            }

            @Override
            public void onClickCheckBox(FavoritoAdapter.EmpresasViewHolder holder, int idx, CheckBox cb) {
                Empresa e = empresaAdapter.getEmpresas().get(idx);

                if (BuscaPrecoApplication.getInstance().isNeedToUpdate("empresa")){
                    BuscaPrecoApplication.getInstance().setNeedToUpdate("empresa",true);
                }else
                    BuscaPrecoApplication.getInstance().setNeedToUpdate("empresa",true);

                if (cb.isChecked()) {
                    EmpresaService.salvarEmpresaFav(getContext(), e);
                    toast("Empresa adicionada aos favoritos!");
                }
                else {
                    EmpresaService.deletarEmpresaFav(getContext(), e);
                    toast("Empresa removida dos favoritos!");
                }
            }

            @Override
            public void onClickInfo(FavoritoAdapter.EmpresasViewHolder holder, int idx) {
                Empresa e = empresas.get(idx);
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
}