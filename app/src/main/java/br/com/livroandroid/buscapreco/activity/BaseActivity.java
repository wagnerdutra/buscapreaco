package br.com.livroandroid.buscapreco.activity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import br.com.livroandroid.buscapreco.R;

/**
 * Created by Wagner on 04/02/2016.
 */
public class BaseActivity extends livroandroid.lib.activity.BaseActivity {
    public void setUpToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar!=null){
            setSupportActionBar(toolbar);
        }
    }
}
