package br.com.livroandroid.buscapreco.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.livroandroid.buscapreco.R;
import br.com.livroandroid.buscapreco.fragment.ProdutosPromocaoFragment;
import br.com.livroandroid.buscapreco.model.Empresa;
import br.com.livroandroid.buscapreco.fragment.ProdutosFragment;

/**
 * Created by Wagner on 27/03/2016.
 */
public class TabsAdapterProduto extends FragmentPagerAdapter {
    private Context context;
    private Empresa empresa;

    public TabsAdapterProduto(Context context, FragmentManager fm, Empresa empresa) {
        super(fm);
        this.context = context;
        this.empresa=empresa;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getString(R.string.produto);
        } else
            return context.getString(R.string.promocao);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return ProdutosFragment.newInstance(empresa);
        }else
            return ProdutosPromocaoFragment.newInstance(empresa);
    }
}