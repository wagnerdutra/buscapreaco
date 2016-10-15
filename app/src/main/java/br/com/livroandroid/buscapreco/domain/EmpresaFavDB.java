package br.com.livroandroid.buscapreco.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.livroandroid.buscapreco.model.Empresa;

public class EmpresaFavDB extends sqlLite{

    private static final String TAG = "sql";

    public EmpresaFavDB(Context context) {
        super(context);
    }

    public long save(Empresa empresa){
        long id;
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("id", empresa.getId());
            values.put("nome", empresa.getNome());
            values.put("cnpj", empresa.getCnpj());
            values.put("rua", empresa.getRua());
            values.put("numero", empresa.getNumero());
            values.put("bairro", empresa.getBairro());
            values.put("cidade", empresa.getCidade());
            values.put("estado", empresa.getEstado());
            values.put("cep", empresa.getCep());
            values.put("tel", empresa.getTel());
            values.put("email", empresa.getEmail());
            values.put("tipo", empresa.getTipo());
            values.put("hora_Inicio", empresa.getHoraFim());
            values.put("hora_Fim", empresa.getHoraFim());
            values.put("url_Foto", empresa.getUrlFoto());
            Log.i(TAG, "Gravou "+empresa.getNome());
            id = db.insert("favoritos","", values);
            return id;
        } finally {
            db.close();
        }
    }

    public int delete(Empresa empresa){
        SQLiteDatabase db = getWritableDatabase();

        try{
            int count = db.delete("favoritos", "id=?", new String[]{String.valueOf(empresa.getId())});
            Log.i(TAG, "Deletou ["+count+" registro");
            return count;
        } finally {
            db.close();
        }
    }

    public List<Empresa> findAll(){
        SQLiteDatabase db = getWritableDatabase();

        try {
            Cursor c = db.query("favoritos", null, null, null, null, null, null, null);
            return toList(c);
        } finally {
            db.close();
        }
    }

    public boolean existeFav(long id){
        SQLiteDatabase db = getWritableDatabase();

        try {
            Cursor c = db.query("favoritos", null, "id = "+id, null, null, null, null, null);
            List<Empresa> empresas = toList(c);
            if (empresas.size()>0)
                return true;
            else
                return false;
        } finally {
            db.close();
        }
    }

    private List<Empresa> toList(Cursor c){
        List<Empresa> empresas = new ArrayList<Empresa>();
        if (c.moveToFirst()){
            do{
                Empresa empresa = new Empresa();

                empresa.setId(c.getLong(c.getColumnIndex("id")));
                empresa.setNome(c.getString(c.getColumnIndex("nome")));
                empresa.setCnpj(c.getString(c.getColumnIndex("cnpj")));
                empresa.setRua(c.getString(c.getColumnIndex("rua")));
                empresa.setNumero(c.getInt(c.getColumnIndex("numero")));
                empresa.setBairro(c.getString(c.getColumnIndex("bairro")));
                empresa.setCidade(c.getString(c.getColumnIndex("cidade")));
                empresa.setEstado(c.getString(c.getColumnIndex("estado")));
                empresa.setCep(c.getString(c.getColumnIndex("cep")));
                empresa.setTel(c.getString(c.getColumnIndex("tel")));
                empresa.setEmail(c.getString(c.getColumnIndex("email")));
                empresa.setTipo(c.getString(c.getColumnIndex("tipo")));
                empresa.setHoraInicio(c.getString(c.getColumnIndex("hora_Inicio")));
                empresa.setHoraFim(c.getString(c.getColumnIndex("hora_Fim")));
                empresa.setUrlFoto(c.getString(c.getColumnIndex("url_Foto")));
                empresas.add(empresa);
            } while (c.moveToNext());
        }
        return empresas;
    }
}