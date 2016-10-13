package br.com.livroandroid.buscapreco.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.livroandroid.buscapreco.model.Produto;

public class ProdutoDB extends sqlLite{

    private static final String TAG = "sql";

    public ProdutoDB(Context context) {
        super(context);
    }

    public long save(Produto produto){
        long id;
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("id", produto.getId());
            values.put("idEmpresa", produto.getIdEmpresa());
            values.put("nome", produto.getNome());
            values.put("precoVista", produto.getPrecoVista());
            values.put("precoPromocao", produto.getPrecoPromocao());
            values.put("unidade", produto.getUnidade());
            values.put("codBarras", produto.getCodBarras());
            values.put("urlFoto", produto.getUrlFoto());
            Log.i(TAG, "Gravou "+produto.getNome());
            id = db.insert("produtos","", values);
            return id;
        } finally {
            db.close();
        }
    }

    public int deleteByEmpresa(Long idEmpresa){
        SQLiteDatabase db = getWritableDatabase();

        try{
            int count = db.delete("produtos", "idEmpresa=?", new String[]{String.valueOf(idEmpresa)});
            Log.i(TAG, "Deletou ["+count+" registro do tipo");
            return count;
        } finally {
            db.close();
        }
    }

    public int deleteByEmpresaPromocao(Long idEmpresa){
        SQLiteDatabase db = getWritableDatabase();

        try{
            Log.i("DELETOU", "PRODUTOS EM PROMOCAO");
            int count = db.delete("produtos", "idEmpresa=? and precoPromocao>0.0", new String[]{String.valueOf(idEmpresa)});
            Log.i(TAG, "Deletou ["+count+" registro");
            return count;
        } finally {
            db.close();
        }
    }

    public List<Produto> findByEmpresa(Long idEmpresa){
        SQLiteDatabase db = getWritableDatabase();

        try {
            Cursor c = db.query("produtos", null,"idEmpresa = '"+idEmpresa+"'", null, null, null, "nome", null);
            return toList(c);
        } finally {
            db.close();
        }
    }

    public List<Produto> findByEmpresaPromocao(Long idEmpresa){
        SQLiteDatabase db = getWritableDatabase();

        try {
            Cursor c = db.query("produtos", null, "idEmpresa = '"+idEmpresa+"' and precoPromocao > 0.0", null, null, null, "nome", null);
            return toList(c);
        } finally {
            db.close();
        }
    }

    private List<Produto> toList(Cursor c){
        List<Produto> produtos = new ArrayList<Produto>();
        if (c.moveToFirst()){
            do{
                Produto produto = new Produto();
                Log.i(TAG, "ENTROOUU");
                produto.setId(c.getLong(c.getColumnIndex("id")));
                produto.setIdEmpresa(c.getLong(c.getColumnIndex("idEmpresa")));
                produto.setNome(c.getString(c.getColumnIndex("nome")));
                produto.setPrecoVista(c.getFloat(c.getColumnIndex("precoVista")));
                produto.setPrecoPromocao(c.getFloat(c.getColumnIndex("precoPromocao")));
                produto.setUnidade(c.getString(c.getColumnIndex("unidade")));
                produto.setCodBarras(c.getString(c.getColumnIndex("codBarras")));
                produto.setUrlFoto(c.getString(c.getColumnIndex("urlFoto")));
                produtos.add(produto);
            } while (c.moveToNext());
        }
        return produtos;
    }
}