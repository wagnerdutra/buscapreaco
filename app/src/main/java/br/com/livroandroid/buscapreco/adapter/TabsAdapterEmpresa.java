package br.com.livroandroid.buscapreco.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.livroandroid.buscapreco.R;
import br.com.livroandroid.buscapreco.fragment.CategoriaFragment;
import br.com.livroandroid.buscapreco.fragment.EmpresasFragment;

/**
 * Created by Wagner on 27/03/2016.
 */
public class TabsAdapterEmpresa extends FragmentPagerAdapter {
    private Context context;

    public TabsAdapterEmpresa(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof EmpresasFragment){
            EmpresasFragment empresasFragment = (EmpresasFragment) object;
            empresasFragment.show();
        }
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getString(R.string.empresa);
        } else
            return context.getString(R.string.categoria);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return new EmpresasFragment();
        }else
            return new CategoriaFragment();
    }
}