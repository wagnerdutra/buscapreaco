package br.com.livroandroid.buscapreco.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import br.com.livroandroid.buscapreco.R;
import br.com.livroandroid.buscapreco.Utils.DividerItemDecoration;
import br.com.livroandroid.buscapreco.adapter.EmpresaAdapter;
import br.com.livroandroid.buscapreco.adapter.ListaProdutoAdapter;
import br.com.livroandroid.buscapreco.adapter.ProdutoAdapter;
import br.com.livroandroid.buscapreco.model.Empresa;
import br.com.livroandroid.buscapreco.model.Produto;

public class ListaProduto extends BaseActivity {

    protected RecyclerView recyclerView;
    private List<Produto> produtos;
    private ListaProdutoAdapter produtoAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produto);
        setUpToolbar();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        produtos = getIntent().getParcelableArrayListExtra("produtos");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerProduto);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        recyclerView.setHasFixedSize(true);

        produtoAdapter = new ListaProdutoAdapter(getContext(), produtos, onClickProduto());
        recyclerView.setAdapter(produtoAdapter);
    }

    private ListaProdutoAdapter.ProdutoOnClickListener onClickProduto() {
        return new ListaProdutoAdapter.ProdutoOnClickListener() {
            @Override
            public void onLongClickProduto(ListaProdutoAdapter.ProdutosViewHolder holder, int idx) {

            }

            @Override
            public void onClickProduto(ListaProdutoAdapter.ProdutosViewHolder holder, int idx) {

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