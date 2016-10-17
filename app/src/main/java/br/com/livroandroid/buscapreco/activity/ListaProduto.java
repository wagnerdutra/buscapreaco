package br.com.livroandroid.buscapreco.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import br.com.livroandroid.buscapreco.R;
import br.com.livroandroid.buscapreco.adapter.ProdutoAdapter;
import br.com.livroandroid.buscapreco.model.Produto;

public class ListaProduto extends BaseActivity {

    protected RecyclerView recyclerView;
    private List<Produto> produtos;
    private ProdutoAdapter produtoAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ActionMode actionMode;
    private String text="";
    private String empresa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produto);
        setUpToolbar();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        produtos = getIntent().getParcelableArrayListExtra("produtos");
        empresa = getIntent().getStringExtra("empresa");

        setTitle(getIntent().getStringExtra("title"));
        recyclerView = (RecyclerView) findViewById(R.id.recyclerProduto);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        recyclerView.setHasFixedSize(true);

        produtoAdapter = new ProdutoAdapter(getContext(), produtos, onClickProduto());
        recyclerView.setAdapter(produtoAdapter);
    }

    private ProdutoAdapter.ProdutoOnClickListener onClickProduto() {
        return new ProdutoAdapter.ProdutoOnClickListener() {
            @Override
            public void onLongClickProduto(ProdutoAdapter.ProdutosViewHolder holder, int idx) {
                if (actionMode!=null){
                    return;
                }
                actionMode = startSupportActionMode(getActionModeCallback());
                Produto p = produtoAdapter.getProdutos().get(idx-1);
                p.setSelected(true);
                recyclerView.getAdapter().notifyDataSetChanged();
                updateActionModeTitle();
            }

            @Override
            public void onClickProduto(ProdutoAdapter.ProdutosViewHolder holder, int idx) {
                Produto p = produtoAdapter.getProdutos().get(idx-1);
                if (actionMode!=null) {
                    p.setSelected(!p.isSelected());
                    updateActionModeTitle();
                    recyclerView.getAdapter().notifyDataSetChanged();
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
                        getIntent().getStringExtra("empresa")+"\n");
            else
                text = text + (p.getNome()+" apenas R$"+
                        p.getPrecoPromocao() + " em "+
                        empresa+"\n");
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ActivityCompat.finishAfterTransition(getActivity());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}