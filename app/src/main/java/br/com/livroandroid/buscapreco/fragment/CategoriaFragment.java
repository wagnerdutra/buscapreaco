package br.com.livroandroid.buscapreco.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import br.com.livroandroid.buscapreco.R;
import br.com.livroandroid.buscapreco.activity.ListaCategoria;

public class CategoriaFragment extends BaseFragment {

    public CardView cardViewSup;
    public CardView cardViewAço;
    public CardView cardViewRes;
    public CardView cardViewLan;
    public CardView cardViewPiz;
    public CardView cardViewCer;
    public CardView cardViewMat;
    public CardView cardViewFar;
    public CardView cardViewLoj;

    public CategoriaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categoria, container, false);

        //setHasOptionsMenu(true);
        cardViewSup = (CardView) view.findViewById(R.id.card_viewSup);
        cardViewAço = (CardView) view.findViewById(R.id.card_viewAço);
        cardViewCer = (CardView) view.findViewById(R.id.card_viewCerv);
        cardViewFar = (CardView) view.findViewById(R.id.card_viewFar);
        cardViewLan = (CardView) view.findViewById(R.id.card_viewLan);
        cardViewLoj = (CardView) view.findViewById(R.id.card_viewLoj);
        cardViewMat = (CardView) view.findViewById(R.id.card_viewMat);
        cardViewPiz = (CardView) view.findViewById(R.id.card_viewPiz);
        cardViewRes = (CardView) view.findViewById(R.id.card_viewRes);

        cardViewSup.setOnClickListener(onClickSup());
        cardViewAço.setOnClickListener(onClickAco());
        cardViewCer.setOnClickListener(onClickCer());
        cardViewFar.setOnClickListener(onClickFar());
        cardViewLan.setOnClickListener(onClickLan());
        cardViewLoj.setOnClickListener(onClickLoj());
        cardViewMat.setOnClickListener(onClickMat());
        cardViewPiz.setOnClickListener(onClickPiz());
        cardViewRes.setOnClickListener(onClickRes());

        return view;
    }

    private View.OnClickListener onClickSup(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ListaCategoria.class);
                intent.putExtra("categoria","Supermercado");
                startActivity(intent);
            }
        };
    }
    private View.OnClickListener onClickAco(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ListaCategoria.class);
                intent.putExtra("categoria","Açougue");
                startActivity(intent);
            }
        };
    }
    private View.OnClickListener onClickCer(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ListaCategoria.class);
                intent.putExtra("categoria","Cervejaria");
                startActivity(intent);
            }
        };
    }
    private View.OnClickListener onClickFar(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ListaCategoria.class);
                intent.putExtra("categoria","Farmacia");
                startActivity(intent);
            }
        };
    }
    private View.OnClickListener onClickLan(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ListaCategoria.class);
                intent.putExtra("categoria","Lanchonete");
                startActivity(intent);
            }
        };
    }
    private View.OnClickListener onClickLoj(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ListaCategoria.class);
                intent.putExtra("categoria","Lojas");
                startActivity(intent);
            }
        };
    }
    private View.OnClickListener onClickMat(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ListaCategoria.class);
                intent.putExtra("categoria","Materiais");
                startActivity(intent);
            }
        };
    }
    private View.OnClickListener onClickPiz(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ListaCategoria.class);
                intent.putExtra("categoria","Pizzaria");
                startActivity(intent);
            }
        };
    }
    private View.OnClickListener onClickRes(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ListaCategoria.class);
                intent.putExtra("categoria","Restaurante");
                startActivity(intent);
            }
        };
    }
}