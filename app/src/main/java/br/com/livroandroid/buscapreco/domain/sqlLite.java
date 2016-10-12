package br.com.livroandroid.buscapreco.domain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Wagne on 12/10/2016.
 */
public class sqlLite extends SQLiteOpenHelper {

    private static final String TAG = "sql";
    public static final String NOME_BANCO = "busca_preco.sqlite";
    private static final int VERSAO_BANCO = 1;

    public sqlLite(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Criando tabela...");
        db.execSQL("create table if not exists empresas (id integer primary key,nome text,cnpj text," +
                "rua text,numero text,bairro text, cidade text, estado text,cep text, tel text,senha text," +
                "email text,tipo text,hora_Inicio text,hora_Fim text,url_Foto text);");

        db.execSQL("create table if not exists favoritos (id integer primary key,nome text,cnpj text,rua text," +
                "numero text,bairro text, cidade text, estado text,cep text, tel text,senha text,email text," +
                "tipo text,hora_Inicio text,hora_Fim text,url_Foto text);");

        db.execSQL("create table if not exists produtos (id integer primary key,idEmpresa integer,nome text,precoVista text," +
                "precoPromocao text,unidade text,codBarras text,urlFoto text);");

        Log.d(TAG, "Tabela criada");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
