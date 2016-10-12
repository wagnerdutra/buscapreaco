package br.com.livroandroid.buscapreco.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.FloatProperty;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.common.StringUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import br.com.livroandroid.buscapreco.R;
import br.com.livroandroid.buscapreco.Utils.AnyOrientationCaptureActivity;
import br.com.livroandroid.buscapreco.activity.ListaProduto;
import br.com.livroandroid.buscapreco.adapter.ProdutoAdapter;
import br.com.livroandroid.buscapreco.adapter.SacolaProdutoAdapter;
import br.com.livroandroid.buscapreco.domain.ProdutoService;
import br.com.livroandroid.buscapreco.model.Empresa;
import br.com.livroandroid.buscapreco.model.Produto;

public class SacolaProdutoFragment extends BaseFragment {

    protected RecyclerView recyclerView;
    private List<Produto> produtos;
    private SacolaProdutoAdapter produtoAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Empresa empresa;
    private ImageButton btLimpar;
    private TextView tvTotal;

    public SacolaProdutoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_sacola_produto, container, false);
        setHasOptionsMenu(true);
        empresa = (Empresa) getArguments().get("empresa");
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerProduto);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        recyclerView.setHasFixedSize(true);
        produtoAdapter = new SacolaProdutoAdapter(getContext(), produtos, onClickListener());
        recyclerView.setAdapter(produtoAdapter);
        getActionBar().setTitle("Simula compra");
        btLimpar = (ImageButton) view.findViewById(R.id.btLimpar);
        tvTotal = (TextView) view.findViewById(R.id.tvTotal);
        btLimpar.setOnClickListener(onClickLimpar());
        btLimpar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                toast("Zera o valor da lista");
                return false;
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskProdutos();
    }

    private void taskProdutos(){
        startTask("sacola", new GetProdutoTask(false), R.id.progress);
    }

    private class GetProdutoTask implements BaseFragment.TaskListener<List<Produto>> {
        boolean refresh;

        public GetProdutoTask(boolean refresh) {
            this.refresh = refresh;
        }

        @Override
        public List<Produto> execute() throws Exception {
            return ProdutoService.getProdutosByEmpresa(getContext(),empresa.getId(), refresh);
        }

        @Override
        public void updateView(List<Produto> produtos) {
            if (produtos!=null){
                SacolaProdutoFragment.this.produtos=produtos;
                //Atualiza a view na UI Thread
                produtoAdapter = new SacolaProdutoAdapter(getContext(), produtos, onClickListener());
                recyclerView.setAdapter(produtoAdapter);
            } else
                Toast.makeText(getContext(), "Não foi encontrado nenhum produto", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(Exception exception) {
            alert("Ocorreu algum erro ao buscar os dados");
        }

        @Override
        public void onCancelled(String cod) {

        }
    }

    private SacolaProdutoAdapter.ProdutoOnClickListener onClickListener() {
        return new SacolaProdutoAdapter.ProdutoOnClickListener() {
            @Override
            public void onClickCheckbox(final SacolaProdutoAdapter.ProdutosViewHolder holder, final int idx) {
                if (holder.checkBox.isChecked()){
                    if (holder.pUnid.getText().equals("KG")) {
                        InserirQuantidadeDialog.show(getFragmentManager(),"Peso", new InserirQuantidadeDialog.Callback() {
                            @Override
                            public void onQuantidadeUpdated(float quandidade) {
                                Produto p = produtoAdapter.getProdutos().get(idx);
                                p.setQtd(quandidade);
                                float total = Float.parseFloat(tvTotal.getText().toString());
                                total = total + (quandidade * (Float.parseFloat(holder.pPreco.getText().toString())));
                                tvTotal.setText(String.valueOf(total));
                                recyclerView.getAdapter().notifyDataSetChanged();
                            }
                        });
                    }else
                    if (holder.pUnid.getText().equals("UN")){
                        InserirQuantidadeDialog.show(getFragmentManager(),"Quantidade", new InserirQuantidadeDialog.Callback() {
                            @Override
                            public void onQuantidadeUpdated(float quandidade) {
                                Produto p = produtoAdapter.getProdutos().get(idx);
                                p.setQtd(quandidade);
                                float total = Float.parseFloat(tvTotal.getText().toString());
                                total = total + (quandidade * (Float.parseFloat(holder.pPreco.getText().toString())));
                                tvTotal.setText(String.valueOf(total));
                                recyclerView.getAdapter().notifyDataSetChanged();
                            }
                        });
                    }
                }else{
                    Produto p = produtoAdapter.getProdutos().get(idx);
                    float total = Float.parseFloat(tvTotal.getText().toString());
                    Log.i("TESETEE",(holder.pQtd.getText().toString()));
                    Log.i("TESTANDOO",holder.pPreco.getText().toString());
                    total = total - (p.getQtd()) * (Float.parseFloat(holder.pPreco.getText().toString()));
                    tvTotal.setText(String.valueOf(total));
                    p.setQtd(0);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        };
    }

    private View.OnClickListener onClickLimpar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final List<Produto> produtos = produtoAdapter.getProdutos();

                if (produtos!=null) {
                    LimpaCompraDialog.show(getFragmentManager(), new LimpaCompraDialog.Callback() {
                        @Override
                        public void onCidadeUpdated() {
                            for (int i = 0; i < produtos.size(); i++) {
                                produtos.get(i).setChecked(false);
                                produtos.get(i).setQtd(0);
                            }
                            tvTotal.setText("0.0");
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }
                    });
                }
            }
        };
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);

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
                List<Produto> produtos = produtoAdapter.getProdutos();

                boolean verifica = false;

                for (int i=0;i<produtos.size();i++) {
                    if (produtos.get(i).getNome().toLowerCase().contains(query.toLowerCase())) {
                        linearLayoutManager.scrollToPositionWithOffset(i,20);
                        verifica = true;
                    }
                }

                if (!verifica)
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

                List<Produto> produtos = produtoAdapter.getProdutos();

                boolean verifica = false;

                for (int i=0;i<produtos.size();i++){
                    if (produtos.get(i).getCodBarras().equals(result.getContents())) {
                        linearLayoutManager.scrollToPositionWithOffset(i,20);
                        verifica = true;
                    }
                }

                if (!verifica)
                    toast("Não foi encontrado produto com o código");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_barCode){
            scanFromFragment();
            return true;
        }else
        if (id ==android.R.id.home) {
            ActivityCompat.finishAfterTransition(getActivity());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
