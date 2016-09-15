package br.com.livroandroid.buscapreco.fragment;


import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
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
public class InserirCidadeDialog extends DialogFragment {

    private Callback callback;
    private EditText etCidade;
    private Spinner spinner;

    public static interface Callback {
        public void onCidadeUpdated(String cidade, String estado);
    }

    public static void show(FragmentManager fm, Callback callback){
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("editar_local");

        if (prev!=null){
            ft.remove(prev);
        }

        ft.addToBackStack(null);
        InserirCidadeDialog frag = new InserirCidadeDialog();
        frag.callback = callback;

        Bundle args = new Bundle();

        frag.setArguments(args);
        frag.show(ft,"editar_local");
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() == null){
            return;
        }

        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        getDialog().getWindow().setLayout(width,height);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inserir_cidade_dialog, container, false);

        spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.estado_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        etCidade = (EditText) view.findViewById(R.id.txCidade);

        view.findViewById(R.id.btOk).setOnClickListener(onClickOk());
        return view;
    }

    private View.OnClickListener onClickOk() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cidade = etCidade.getText().toString();

                if (cidade == null || cidade.trim().length()==0){
                    etCidade.setError("Informe o nome");
                    return;
                }
                Context context = v.getContext();

                /*
                    Atualizar banco...
                */

                if (callback != null){
                    callback.onCidadeUpdated(cidade, spinner.getSelectedItem().toString());
                }
                dismiss();
            }
        };
    }

}
