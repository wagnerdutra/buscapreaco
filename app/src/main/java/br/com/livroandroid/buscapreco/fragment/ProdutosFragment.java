package br.com.livroandroid.buscapreco.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.view.ActionMode;
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
import android.view.WindowManager;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import br.com.livroandroid.buscapreco.R;
import br.com.livroandroid.buscapreco.Utils.AnyOrientationCaptureActivity;
import br.com.livroandroid.buscapreco.activity.ListaProduto;
import br.com.livroandroid.buscapreco.adapter.ProdutoAdapter;
import br.com.livroandroid.buscapreco.domain.ProdutoService;
import br.com.livroandroid.buscapreco.model.Empresa;
import br.com.livroandroid.buscapreco.model.Produto;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProdutosFragment extends BaseFragment {

    protected RecyclerView recyclerView;
    private List<Produto> produtos;
    private ProdutoAdapter produtoAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Empresa empresa;
    private ActionMode actionMode;
    private String text="";
    private boolean tipo;

    public static ProdutosFragment newInstance(Empresa empresa, boolean tipo){
        Bundle args = new Bundle();
        args.putParcelable("empresa", empresa);
        args.putBoolean("tipo",tipo);
        ProdutosFragment f = new ProdutosFragment();
        f.setArguments(args);
        return f;
    }

    public ProdutosFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Lê o tipo dos argumentos.
            this.empresa = getArguments().getParcelable("empresa");
            this.tipo = getArguments().getBoolean("tipo");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_produtos, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerProduto);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
       // recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        recyclerView.setHasFixedSize(true);
        produtoAdapter = new ProdutoAdapter(getContext(), produtos, onClickListener());
        recyclerView.setAdapter(produtoAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskProdutos();
    }

    private void taskProdutos(){
        startTask("produtos", new GetProdutoTask(), R.id.progress);
    }

    private class GetProdutoTask implements TaskListener<List<Produto>>{
        @Override
        public List<Produto> execute() throws Exception {
            if (tipo)
                return ProdutoService.getProdutosByEmpresa(getContext(),empresa.getId());
            else
                return ProdutoService.getProdutosByEmpresaPromocao(getContext(),empresa.getId());
        }

        @Override
        public void updateView(List<Produto> produtos) {
            if (produtos!=null){
                ProdutosFragment.this.produtos=produtos;
                //Atualiza a view na UI Thread
                produtoAdapter = new ProdutoAdapter(getContext(), produtos, onClickListener());
                recyclerView.setAdapter(produtoAdapter);
            } else
                snack(recyclerView,"Não foi encontrado nenhum produto");
                //Toast.makeText(getContext(), "Não foi encontrado nenhum produto", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(Exception exception) {
            alert("Ocorreu algum erro ao buscar os dados");
        }

        @Override
        public void onCancelled(String cod) {

        }
    }

    private ProdutoAdapter.ProdutoOnClickListener onClickListener() {
        return new ProdutoAdapter.ProdutoOnClickListener() {
            @Override
            public void onLongClickProduto(ProdutoAdapter.ProdutosViewHolder holder, int idx) {
                if (actionMode!=null){
                    return;
                }
                actionMode = getAppCompatActivity().startSupportActionMode(getActionModeCallback());
                Produto p = produtoAdapter.getProdutos().get(idx);
                p.setSelected(true);
                recyclerView.getAdapter().notifyDataSetChanged();
                updateActionModeTitle();
            }

            @Override
            public void onClickProduto(ProdutoAdapter.ProdutosViewHolder holder, int idx) {
                Produto p = produtoAdapter.getProdutos().get(idx);
                if (actionMode!=null) {
                    p.setSelected(!p.isSelected());
                    updateActionModeTitle();
                    recyclerView.getAdapter().notifyDataSetChanged();
                } else {
                    toast("Toque e segure para compartilhar!");
                }
            }
        };
    }

    private void updateActionModeTitle(){
        if (actionMode!=null){
            actionMode.setTitle("Selecione os produtos.");
            actionMode.setSubtitle(null);
            List<Produto> selectedProdutos = getselectedProdutos();
            if (selectedProdutos.size()==1){
                actionMode.setSubtitle("1 Produto selecionado");
            } else if (selectedProdutos.size()>1)
                actionMode.setSubtitle(selectedProdutos.size()+" Produtos selecionado");
            updateShareIntent(selectedProdutos);
        }
    }

    private void updateShareIntent(List<Produto> selectProdutos){
        text = "";
        for (Produto p: selectProdutos){
            if (p.getPrecoPromocao()==0)
                text = text + (p.getNome()+" apenas R$ "+
                    p.getPrecoVista() + " em "+
                    empresa.getNome()+"\n");
            else
                text = text + (p.getNome()+" apenas R$"+
                    p.getPrecoPromocao() + " em "+
                    empresa.getNome()+"\n");
        }
    }

    private List<Produto> getselectedProdutos() {
        List<Produto> list = new ArrayList<>();
        for (Produto p: produtoAdapter.getProdutos()){
            if (p.isSelected()){
                list.add(p);
            }
        }
        return list;
    }

    private ActionMode.Callback getActionModeCallback(){
        return new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = getActivity().getMenuInflater();
                inflater.inflate(R.menu.menu_frag_produtos_context, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    // This is to highlight the status bar and distinguish it from the action bar,
                    // as the action bar while in the action mode is colored app_green_dark
                    getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                }

                // Other stuff...
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (!text.equals("")) {
                    if (item.getItemId() == R.id.action_share) {
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.setType("text/*");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                        startActivity(Intent.createChooser(shareIntent, "Enviar Produtos"));
                    }
                }

                mode.finish();
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionMode = null;

                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.zxing_transparent));
                }*/

                for (Produto p:produtoAdapter.getProdutos()){
                    p.setSelected(false);
                }
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        };
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.mainprodutos, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item); //item.getActionView();
        searchView.setInputType(1);
        searchView.setQueryHint("Digite o nome do produto..");
        searchView.setOnQueryTextListener(onSearch());
    }

    private SearchView.OnQueryTextListener onSearch() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                /*for (int i=0;i<produtos.size();i++) {
                    if (produtos.get(i).getNome().toLowerCase().contains(query.toLowerCase())) {
                        linearLayoutManager.scrollToPositionWithOffset(i,20);
                        Log.i("TESTE", "ENTROU");
                    }
                }*/

                List<Produto> produtos = produtoAdapter.getProdutos();
                List<Produto> produtosFiltro = new ArrayList<>();

                if (produtos!=null) {
                    for (int i = 0; i < produtos.size(); i++) {
                        if (produtos.get(i).getNome().toLowerCase().contains(query.toLowerCase())) {
                            produtosFiltro.add(produtos.get(i));
                        }
                    }

                    if (produtosFiltro.size() > 0) {
                        Intent intent = new Intent(getContext(), ListaProduto.class);
                        intent.putParcelableArrayListExtra("produtos", (ArrayList<? extends Parcelable>) produtosFiltro);
                        startActivity(intent);
                    } else
                        toast("Nenhum produto com o nome pesquisado");
                } else
                    toast("Nenhum produto com o nome pesquisado");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };
    }

    public void scanFromFragment() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES);
        integrator.setPrompt("Posicione o código de barras para a leitura");
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                toast("Operação cancelada");
            } else {

                List<Produto> produtos;
                produtos = produtoAdapter.getProdutos();

                boolean teste=false;

                for (int i=0;i<produtos.size();i++){
                    if (produtos.get(i).getCodBarras().equals(result.getContents())) {
                        if (produtos.get(i).getPrecoPromocao()==0)
                            showAbout(produtos.get(i).getPrecoVista());
                        else
                            showAbout(produtos.get(i).getPrecoPromocao());
                        teste=true;
                    }
                }

                if (!teste)
                    toast("Não foi encontrado produto com o código");
            }
            // At this point we may or may not have a reference to the activity
        }
    }

    public void showAbout(float valor) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog_about");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        AboutPreco aboutPreco = AboutPreco.newInstante(valor+" R$");
        aboutPreco.show(ft, "dialog");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_barCode){
            scanFromFragment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}