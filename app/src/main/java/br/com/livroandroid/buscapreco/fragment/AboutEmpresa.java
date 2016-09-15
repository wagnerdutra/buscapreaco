package br.com.livroandroid.buscapreco.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.widget.TextView;

import br.com.livroandroid.buscapreco.R;
import br.com.livroandroid.buscapreco.model.Empresa;

public class AboutEmpresa extends DialogFragment {

    public static AboutEmpresa newInstante(Empresa empresa){
        AboutEmpresa a = new AboutEmpresa();

        Bundle args = new Bundle();
        args.putParcelable("empresa", empresa);
        a.setArguments(args);
        return a;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Empresa empresa = getArguments().getParcelable("empresa");

        SpannableStringBuilder aboutBody = new SpannableStringBuilder();
        String aboutEmpresa = "<strong>Empresa: </strong>"+empresa.getNome()+"" +
                "<br/><br/>" +
                "<strong>CNPJ: </strong>"+empresa.getCnpj()+"" +
                "<br/><br/>" +
                "<strong>Rua: </strong>"+empresa.getRua()+"" +
                "<br/><br/>" +
                "<strong>Numero: </strong>"+empresa.getNumero()+"" +
                "<br/><br/>" +
                "<strong>Bairro: </strong>"+empresa.getBairro()+"" +
                "<br/><br/>" +
                "<strong>Cidade: </strong>"+empresa.getCidade()+"" +
                "<br/><br/>" +
                "<strong>Estado: </strong>"+empresa.getEstado()+"" +
                "<br/><br/>" +
                "<strong>CEP: </strong>"+empresa.getCep()+"" +
                "<br/><br/>" +
                "<strong>Tel: </strong>"+empresa.getTel()+"" +
                "<br/><br/>"+
                "<strong>Email: </strong>"+empresa.getEmail();

        aboutBody.append(Html.fromHtml(aboutEmpresa));
        // Infla o layout
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        TextView view = (TextView) inflater.inflate(R.layout.dialog_about, null);
        view.setText(aboutBody);
        view.setMovementMethod(new LinkMovementMethod());
        // Cria o dialog customizado
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.about_dialog_title)
                .setView(view)
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
