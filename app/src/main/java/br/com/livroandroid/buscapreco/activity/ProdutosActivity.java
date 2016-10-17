package br.com.livroandroid.buscapreco.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.view.MenuItem;
import android.view.View;

import br.com.livroandroid.buscapreco.R;
import br.com.livroandroid.buscapreco.fragment.ProdutosFragment;
import br.com.livroandroid.buscapreco.model.Empresa;

public class ProdutosActivity extends BaseActivity {

    private Empresa empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        empresa = getIntent().getParcelableExtra("empresa");

        setContentView(R.layout.activity_produtos);
        setUpToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(getContext(),SacolaProdutoActivity.class);
            intent.putExtra("empresa",empresa);
            startActivity(intent);
            }
        });

        //Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up);
        //fab.startAnimation(animation);

        if (savedInstanceState == null) {
            // Cria o fragment com o mesmo Bundle (args) da intent
            ProdutosFragment frag = new ProdutosFragment();
            frag.setArguments(getIntent().getExtras());
            // Adiciona o fragment no layout
            getSupportFragmentManager().beginTransaction().add(R.id.ProdutoFragment, frag).commit();
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