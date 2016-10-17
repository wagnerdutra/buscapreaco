package br.com.livroandroid.buscapreco.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.livroandroid.buscapreco.R;
import br.com.livroandroid.buscapreco.model.Produto;
import livroandroid.lib.utils.Prefs;

public class ProdutoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected static final String TAG = "livroandroid";
    private final List<Produto> Produtos;
    private final Context context;
    private final ProdutoOnClickListener onClickListener;

    public interface ProdutoOnClickListener {
        public void onLongClickProduto(ProdutosViewHolder holder, int idx);
        public void onClickProduto(ProdutosViewHolder holder, int idx);
    }

    public ProdutoAdapter(Context context, List<Produto> Produtos, ProdutoOnClickListener onClickListener) {
        this.context = context;
        this.Produtos = Produtos;
        this.onClickListener = onClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;

        if (viewType==0){
            View view = LayoutInflater.from(context).inflate(R.layout.layout_nome_cidade, viewGroup, false);
            viewHolder = new CidadeViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_produto, viewGroup, false);
            viewHolder = new ProdutosViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder.getItemViewType()==0){
            CidadeViewHolder cidadeViewHolder = (CidadeViewHolder) holder;
            cidadeViewHolder.tvCidade.setText("Última atualização: ".concat(Prefs.getString(context, "dhP"+Produtos.get(position).getIdEmpresa())));
        } else {

            Produto p = Produtos.get(position-1);

            final ProdutosViewHolder produtosViewHolder = (ProdutosViewHolder) holder;

            produtosViewHolder.pNome.setText(p.getNome());
            if (p.getPrecoPromocao() > 0)
                produtosViewHolder.pPreco.setText(String.valueOf(p.getPrecoPromocao()));
            else
                produtosViewHolder.pPreco.setText(String.valueOf(p.getPrecoVista()));
            produtosViewHolder.pUnid.setText(p.getUnidade());

            produtosViewHolder.progressBar.setVisibility(View.VISIBLE);
            Picasso.with(context).load(p.getUrlFoto()).fit().into(produtosViewHolder.imagem,
                    new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            produtosViewHolder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            produtosViewHolder.progressBar.setVisibility(View.GONE);
                            produtosViewHolder.imagem.setImageResource(R.drawable.semimagem);
                        }
                    });

            //Click
            if (onClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        onClickListener.onLongClickProduto(produtosViewHolder, position);
                        return false;
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickListener.onClickProduto(produtosViewHolder, position);
                    }
                });
            }

            int corFundo = context.getResources().getColor(p.isSelected() ? R.color.colorPrimary : R.color.white);
            produtosViewHolder.cardView.setCardBackgroundColor(corFundo);
            int corFonte = context.getResources().getColor(p.isSelected() ? R.color.white : R.color.colorPrimary);
            produtosViewHolder.pNome.setTextColor(corFonte);
            produtosViewHolder.pPreco.setTextColor(corFonte);
            produtosViewHolder.pUnid.setTextColor(corFonte);
            produtosViewHolder.pCedula.setTextColor(corFonte);
        }
    }

    public List<Produto> getProdutos(){
        return Produtos;
    }

    @Override
    public int getItemCount() {
        return this.Produtos != null ? this.Produtos.size()+1: 0;
    }

    // Subclasse de RecyclerView.ViewHolder. Contém todas as views.
    public static class ProdutosViewHolder extends RecyclerView.ViewHolder {
        public TextView pNome;
        public TextView pPreco;
        public TextView pUnid;
        public CardView cardView;
        public TextView pCedula;
        public ImageView imagem;
        public ProgressBar progressBar;
        private View view;

        public ProdutosViewHolder(View view) {
            super(view);
            this.view = view;
            // Cria as views para salvar no ViewHolder
            cardView = (CardView) view.findViewById(R.id.card_view);
            pNome = (TextView) view.findViewById(R.id.txNome);
            pPreco = (TextView) view.findViewById(R.id.txPrec);
            pUnid = (TextView) view.findViewById(R.id.txUn);
            pCedula = (TextView) view.findViewById(R.id.tvCedula);
            imagem = (ImageView) view.findViewById(R.id.imgProduto);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);
        }
    }

    public static class CidadeViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCidade;
        private View view;

        public CidadeViewHolder(View view) {
            super(view);
            this.view = view;

            tvCidade = (TextView) view.findViewById(R.id.tvCidade);
        }
    }
}