package br.com.livroandroid.buscapreco.activity;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.MenuItem;

import br.com.livroandroid.buscapreco.R;
import br.com.livroandroid.buscapreco.fragment.SacolaProdutoFragment;

public class SacolaProdutoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sacola_produto);
        setUpToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            // Cria o fragment com o mesmo Bundle (args) da intent
            SacolaProdutoFragment frag = new SacolaProdutoFragment();
            frag.setArguments(getIntent().getExtras());
            // Adiciona o fragment no layout
            getSupportFragmentManager().beginTransaction().add(R.id.SacolaProdutoFragment, frag).commit();
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
