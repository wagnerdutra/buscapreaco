package br.com.livroandroid.buscapreco.activity;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.MenuItem;

import br.com.livroandroid.buscapreco.R;
import br.com.livroandroid.buscapreco.fragment.ListaCategoriaFragment;

public class ListaCategoria extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_categoria);
        setUpToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            // Cria o fragment com o mesmo Bundle (args) da intent
            ListaCategoriaFragment frag = new ListaCategoriaFragment();
            frag.setArguments(getIntent().getExtras());
            // Adiciona o fragment no layout
            getSupportFragmentManager().beginTransaction().add(R.id.EmpresaFragment, frag).commit();
        }
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