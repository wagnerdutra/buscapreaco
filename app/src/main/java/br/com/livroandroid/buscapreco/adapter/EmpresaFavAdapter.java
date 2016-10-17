package br.com.livroandroid.buscapreco.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.livroandroid.buscapreco.R;
import br.com.livroandroid.buscapreco.domain.EmpresaFavDB;
import br.com.livroandroid.buscapreco.model.Empresa;


// Herda de RecyclerView.Adapter e declara o tipo genérico <EmpresaFavAdapter.EmpresasViewHolder>
public class EmpresaFavAdapter extends RecyclerView.Adapter<EmpresaFavAdapter.EmpresasViewHolder> {
    protected static final String TAG = "livroandroid";
    private final List<Empresa> Empresas;
    private final Context context;
    private final EmpresaFavAdapter.EmpresaOnClickListener onClickListener;
    private final EmpresaFavDB empresaFavDB;

    public interface EmpresaOnClickListener {
        public void onClickEmpresa(EmpresasViewHolder holder, int idx);
        public void onClickCheckBox(EmpresasViewHolder holder, int idx, CheckBox cb);
        public void onClickInfo(EmpresasViewHolder holder, int idx);
    }

    public EmpresaFavAdapter(Context context, List<Empresa> Empresas, EmpresaOnClickListener onClickListener) {
        this.context = context;
        this.Empresas = Empresas;
        this.onClickListener = onClickListener;
        this.empresaFavDB = new EmpresaFavDB(context);
    }


    @Override
    public EmpresasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_empresacard, parent, false);

        // Cria a classe do ViewHolder
        EmpresasViewHolder holder = new EmpresasViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final EmpresasViewHolder holder, final int position) {
        // Este método recebe o índice do elemento, e atualiza as views que estão dentro do ViewHolder
        Empresa e = Empresas.get(position);

        holder.progressBar.setVisibility(View.VISIBLE);
        Picasso.with(context).load(e.getUrlFoto()).fit().into(holder.imagem,
            new com.squareup.picasso.Callback(){
                @Override
                public void onSuccess() {
                    holder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    holder.progressBar.setVisibility(View.GONE);
                    holder.imagem.setImageResource(R.drawable.semimagem);
                }
            });

        holder.eNome.setText(e.getNome());
        holder.eEnd.setText(e.getRua().concat(", "+e.getNumero()));
        holder.eTel.setText(e.getTel());
        holder.eHora.setText(e.getHoraInicio().concat("-".concat(e.getHoraFim())));
        holder.checkBox.setChecked(empresaFavDB.existeFav(e.getId()));
        holder.eTipo.setText(e.getTipo());

        // Click
        if (onClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClickEmpresa(holder, position);
                }
            });

            holder.btInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClickInfo(holder,position);
                }
            });

            holder.checkBox.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    onClickListener.onClickCheckBox(holder, position,(CheckBox) v);
                }
            });
        }
    }

    public List<Empresa> getEmpresas(){
        return Empresas;
    }

    @Override
    public int getItemCount() {
        return this.Empresas != null ? this.Empresas.size() : 0;
    }

    // Subclasse de RecyclerView.ViewHolder. Contém todas as views.
    public static class EmpresasViewHolder extends RecyclerView.ViewHolder {
        public TextView eNome;
        public TextView eEnd;
        public TextView eTel;
        public TextView eHora;
        public ImageButton btInfo;
        private View view;
        public CheckBox checkBox;
        public ImageView imagem;
        public TextView eTipo;
        public ProgressBar progressBar;

        public EmpresasViewHolder(View view) {
            super(view);
            this.view = view;
            // Cria as views para salvar no ViewHolder
            imagem = (ImageView) view.findViewById(R.id.imgTipoEmpresa);
            btInfo = (ImageButton) view.findViewById(R.id.btInfo);
            eNome = (TextView) view.findViewById(R.id.eNome);
            eEnd = (TextView) view.findViewById(R.id.eEnd);
            eTel = (TextView) view.findViewById(R.id.eTel);
            eHora = (TextView) view.findViewById(R.id.eHora);
            eTipo = (TextView) view.findViewById(R.id.eTipo);
            checkBox = (CheckBox) view.findViewById(R.id.chFav);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);
        }
    }
}