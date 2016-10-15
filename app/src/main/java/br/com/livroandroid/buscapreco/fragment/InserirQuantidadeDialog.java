package br.com.livroandroid.buscapreco.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import br.com.livroandroid.buscapreco.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InserirQuantidadeDialog extends DialogFragment {

    private Callback callback;
    private EditText etQuantidade;
    private String title;
    private TextInputLayout inputLayout;

    public static interface Callback {
        public void onQuantidadeUpdated(float quandidade);
    }

    public static void show(FragmentManager fm, String title, Callback callback){
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("editar_quantidade");

        if (prev!=null){
            ft.remove(prev);
        }

        ft.addToBackStack(null);
        InserirQuantidadeDialog frag = new InserirQuantidadeDialog();
        frag.callback = callback;
        frag.title=title;

        Bundle args = new Bundle();

        frag.setArguments(args);
        frag.show(ft,"editar_quantidade");
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() == null){
            return;
        }

        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_height1);
        getDialog().getWindow().setLayout(width,height);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inserir_quantidade_dialog, container, false);
        inputLayout = (TextInputLayout) view.findViewById(R.id.hintQuant);
        etQuantidade = (EditText) view.findViewById(R.id.etQuantidade);
        inputLayout.setHint(title);
        view.findViewById(R.id.btOk).setOnClickListener(onClickOk());
        setCancelable(false);

        return view;
    }

    private View.OnClickListener onClickOk() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etQuantidade.getText().toString().equals("")){
                    float quant = Float.parseFloat(etQuantidade.getText().toString());

                    if (quant <= 0) {
                        etQuantidade.setError("Informe um valor válido");
                        return;
                    }
                    Context context = v.getContext();

                    /*
                        Atualizar banco...
                    */

                    if (callback != null) {
                        callback.onQuantidadeUpdated(quant);
                    }
                    dismiss();
                }else {
                    etQuantidade.setError("Informe um valor válido");
                }
            }
        };
    }

}
