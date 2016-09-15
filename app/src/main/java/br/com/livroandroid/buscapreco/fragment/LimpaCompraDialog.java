package br.com.livroandroid.buscapreco.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import br.com.livroandroid.buscapreco.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LimpaCompraDialog extends DialogFragment {

    private Callback callback;

    public static interface Callback {
        public void onCidadeUpdated();
    }

    public static void show(FragmentManager fm, Callback callback) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("exclui_compra");

        if (prev != null) {
            ft.remove(prev);
        }

        ft.addToBackStack(null);
        LimpaCompraDialog frag = new LimpaCompraDialog();
        frag.callback = callback;

        Bundle args = new Bundle();

        frag.setArguments(args);
        frag.show(ft, "exclui_compra");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DialogInterface.OnClickListener dialogClickListenes = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case DialogInterface.BUTTON_POSITIVE:
                        if (callback != null){
                            callback.onCidadeUpdated();
                            break;
                        }
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Deseja limpar a lista de compra ?");
        builder.setPositiveButton("Sim", dialogClickListenes);
        builder.setNegativeButton("Não", dialogClickListenes);
        return builder.create();
    }
}