package br.com.livroandroid.buscapreco.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

// Herda de RecyclerView.Adapter e declara o tipo genérico <ProdutoAdapter.ProdutosViewHolder>
public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutosViewHolder> {
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
    public ProdutosViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // Este método cria uma subclasse de RecyclerView.ViewHolder
        // Infla a view do layout
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_produto, viewGroup, false);

        // Cria a classe do ViewHolder
        return new ProdutosViewHolder(view);
    }

    public List<Produto> getProdutos(){
        return Produtos;
    }

    @Override
    public void onBindViewHolder(final ProdutosViewHolder holder, final int position) {

        Produto p = Produtos.get(position);

        holder.pNome.setText(p.getNome());
        if (p.getPrecoPromocao()>0)
            holder.pPreco.setText(String.valueOf(p.getPrecoPromocao())); else
            holder.pPreco.setText(String.valueOf(p.getPrecoVista()));
        holder.pUnid.setText(p.getUnidade());

        holder.progressBar.setVisibility(View.VISIBLE);
        Picasso.with(context).load(p.getUrlFoto()).fit().into(holder.imagem,
                new com.squareup.picasso.Callback(){
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        holder.progressBar.setVisibility(View.GONE);
                    }
                });

        //Click
        if (onClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onClickListener.onLongClickProduto(holder, position);
                    return false;
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClickProduto(holder, position);
                }
            });
        }

        int corFundo = context.getResources().getColor(p.isSelected() ? R.color.colorPrimary : R.color.white);
        holder.cardView.setCardBackgroundColor(corFundo);
        int corFonte = context.getResources().getColor(p.isSelected() ? R.color.white : R.color.colorPrimary);
        holder.pNome.setTextColor(corFonte);
        holder.pPreco.setTextColor(corFonte);
        holder.pUnid.setTextColor(corFonte);
        holder.pCedula.setTextColor(corFonte);
    }

    @Override
    public int getItemCount() {
        return this.Produtos != null ? this.Produtos.size() : 0;
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
}