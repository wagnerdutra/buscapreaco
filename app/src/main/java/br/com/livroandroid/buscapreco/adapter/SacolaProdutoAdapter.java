package br.com.livroandroid.buscapreco.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import br.com.livroandroid.buscapreco.R;
import br.com.livroandroid.buscapreco.model.Produto;

// Herda de RecyclerView.Adapter e declara o tipo genérico <ProdutoAdapter.ProdutosViewHolder>
public class SacolaProdutoAdapter extends RecyclerView.Adapter<SacolaProdutoAdapter.ProdutosViewHolder> {
    protected static final String TAG = "livroandroid";
    private final List<Produto> Produtos;
    private final Context context;
    private final ProdutoOnClickListener onClickListener;

    public interface ProdutoOnClickListener {
        public void onClickCheckbox(ProdutosViewHolder holder, int idx);
    }

    public SacolaProdutoAdapter(Context context, List<Produto> Produtos, ProdutoOnClickListener onClickListener) {
        this.context = context;
        this.Produtos = Produtos;
        this.onClickListener = onClickListener;
    }

    @Override
    public ProdutosViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // Este método cria uma subclasse de RecyclerView.ViewHolder
        // Infla a view do layout
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_sacola_produto, viewGroup, false);

        // Cria a classe do ViewHolder
        ProdutosViewHolder holder = new ProdutosViewHolder(view);
        return holder;
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
        holder.checkBox.setChecked(p.isChecked());
        holder.pQtd.setText(String.valueOf(p.getQtd()));

        if (onClickListener != null) {
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox checkBox = (CheckBox) view;
                    Produtos.get(position).setChecked(checkBox.isChecked());
                    onClickListener.onClickCheckbox(holder, position);
                }
            });
        }
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
        public TextView pQtd;
        public CheckBox checkBox;
        private View view;

        public ProdutosViewHolder(View view) {
            super(view);
            this.view = view;
            // Cria as views para salvar no ViewHolzder
            pNome = (TextView) view.findViewById(R.id.txNome);
            pPreco = (TextView) view.findViewById(R.id.txPrec);
            pUnid = (TextView) view.findViewById(R.id.txUn);
            pQtd = (TextView) view.findViewById(R.id.tvQtdNum);
            checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        }
    }
}