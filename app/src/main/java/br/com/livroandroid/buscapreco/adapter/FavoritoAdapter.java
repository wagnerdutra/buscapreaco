package br.com.livroandroid.buscapreco.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.livroandroid.buscapreco.R;
import br.com.livroandroid.buscapreco.domain.EmpresaFavDB;
import br.com.livroandroid.buscapreco.model.Empresa;

/**
 * Created by Wagne on 10/08/2016.
 */
public class FavoritoAdapter extends RecyclerView.Adapter<FavoritoAdapter.EmpresasViewHolder> {

    private final List<Empresa> empresas;
    private final Context context;
    private final EmpresaOnClickListener onClickListener;
    private final EmpresaFavDB empresaFavDB;

    public interface EmpresaOnClickListener {
        public void onClickEmpresa(EmpresasViewHolder holder, int idx);
        public void onClickCheckBox(EmpresasViewHolder holder, int idx, CheckBox cb);
        public void onClickInfo(EmpresasViewHolder holder, int idx);
    }

    public FavoritoAdapter(Context context, List<Empresa> Empresas, EmpresaOnClickListener onClickListener) {
        this.context = context;
        this.empresas = Empresas;
        this.onClickListener = onClickListener;
        this.empresaFavDB = new EmpresaFavDB(context);
    }

    @Override
    public EmpresasViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_empresacard, viewGroup, false);
        EmpresasViewHolder holder = new EmpresasViewHolder(view);
        return holder;
    }

    public List<Empresa> getEmpresas(){
        return empresas;
    }

    @Override
    public void onBindViewHolder(final EmpresasViewHolder holder, final int position) {
        Empresa e = empresas.get(position);

        switch (e.getTipo()){
            case "Supermercado":{
                holder.imagem.setImageResource(R.drawable.supermercado);
                break;
            }
            case "Açougue":{
                holder.imagem.setImageResource(R.drawable.acougue);
                break;
            }
            case "Restaurante":{
                holder.imagem.setImageResource(R.drawable.restaurante);
                break;
            }
            case "Lanchonete":{
                holder.imagem.setImageResource(R.drawable.lanchonete);
                break;
            }
            case "Pizzaria":{
                holder.imagem.setImageResource(R.drawable.pizzaria);
                break;
            }
            case "Cervejaria":{
                holder.imagem.setImageResource(R.drawable.cervejaria);
                break;
            }
            case "Materiais":{
                holder.imagem.setImageResource(R.drawable.materiais);
                break;
            }
            case "Farmacia":{
                holder.imagem.setImageResource(R.drawable.farmacia);
                break;
            }
            case "Lojas":{
                holder.imagem.setImageResource(R.drawable.loja);
                break;
            }
        }

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

    @Override
    public int getItemCount() {
        return this.empresas != null ? this.empresas.size() : 0;
    }

    public class EmpresasViewHolder extends RecyclerView.ViewHolder{
        public TextView eNome;
        public TextView eEnd;
        public TextView eTel;
        public TextView eHora;
        public ImageButton btInfo;
        private View view;
        public CheckBox checkBox;
        public ImageView imagem;
        public TextView eTipo;

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
        }
    }
}
