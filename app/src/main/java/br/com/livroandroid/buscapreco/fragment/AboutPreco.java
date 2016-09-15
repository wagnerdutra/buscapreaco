package br.com.livroandroid.buscapreco.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.widget.TextView;

import br.com.livroandroid.buscapreco.R;

public class AboutPreco extends DialogFragment {

    public static AboutPreco newInstante(String value){
        AboutPreco a = new AboutPreco();

        Bundle args = new Bundle();
        args.putString("value", value);
        a.setArguments(args);
        return a;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String value = getArguments().getString("value");

        SpannableStringBuilder aboutBody = new SpannableStringBuilder();
        aboutBody.append(value);
        // Infla o layout
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        TextView view = (TextView) inflater.inflate(R.layout.dialog_preco, null);
        view.setText(aboutBody);
        view.setMovementMethod(new LinkMovementMethod());
        // Cria o dialog customizado
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Preço")
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                )
                .create();
    }
}
