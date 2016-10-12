package br.com.livroandroid.buscapreco.domain;

import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import br.com.livroandroid.buscapreco.Utils.Utils;
import br.com.livroandroid.buscapreco.model.Empresa;
import livroandroid.lib.utils.HttpHelper;
import livroandroid.lib.utils.Prefs;

public class EmpresaService {

    private static final String url = "http://192.168.1.104:8080/buscapreco/rest/empresas";
    private static final String TAG = "EmpresaService";
    private static final boolean LOG_ON = false;

    public static List<Empresa> getEmpresas(Context context,String cidade, String estado, boolean refresh) throws IOException, JSONException {
        if (cidade.equals("")) {
            cidade = " ";
            estado = " ";
        }
        List<Empresa> empresas;

        boolean buscaNoBancoDeDados = !refresh;

        if (buscaNoBancoDeDados) {
            empresas = getEmpresasFromBanco(context, cidade, estado);
            if (empresas != null && empresas.size() > 0) {
                return empresas;
            }
        }

        empresas = getEmpresasFromWebService(context, cidade, estado);
        return empresas;
    }

    public static List<Empresa> getEmpresasFromBanco(Context context, String cidade, String estado){
        EmpresaDB db = new EmpresaDB(context);
        try{
            List<Empresa> empresas = db.findByCidadeEstado(cidade, estado);
            Log.d(TAG,"Retornando " + empresas.size() + " empresas do banco");
            return empresas;
        } finally {
            db.close();
        }
    }

    public static List<Empresa> getEmpresasFromWebService(Context context, String cidade, String estado)
            throws IOException, JSONException {

        cidade = Utils.encodeString(cidade);
        Log.i("TESTE",url+"/cidade/"+cidade+"/estado/"+estado);
        HttpHelper http = new HttpHelper();
        String json = http.doGet(url+"/cidade/"+cidade+"/estado/"+estado);

        if (json!=null && json.length()>5) {
            List<Empresa> empresas = parserJSON(context,json);
            salvarEmpresasByCidadeEstado(context, empresas, empresas.get(0).getCidade(), empresas.get(0).getEstado());
            salvaHoraData(context,empresas.get(0).getCidade(),empresas.get(0).getEstado());
            return empresas;
        } else {
            return null;
        }

    }
    public static List<Empresa> getEmpresasCat(Context context, String tipo, String cidade, String estado, boolean refresh) throws IOException, JSONException {
        if (cidade.equals("")) {
            cidade = " ";
            estado = " ";
        }
        List<Empresa> empresas;

        boolean buscaNoBancoDeDados = !refresh;

        if (buscaNoBancoDeDados) {
            empresas = getEmpresasTipoFromBanco(context, tipo, cidade, estado);
            if (empresas != null && empresas.size() > 0) {
                return empresas;
            }
        }

        empresas = getEmpresasTipoFromWebService(context, tipo, cidade, estado);
        return empresas;
    }
    
    public static List<Empresa> getEmpresasTipoFromBanco(Context context,String tipo, String cidade, String estado){
        EmpresaDB db = new EmpresaDB(context);
        try{
            List<Empresa> empresas = db.findByEmpresaTipo(tipo, cidade, estado);
            Log.d(TAG,"Retornando " + empresas.size() + " empresas do banco");
            return empresas;
        } finally {
            db.close();
        }
    }

    public static List<Empresa> getEmpresasTipoFromWebService(Context context, String tipo, String cidade, String estado)
            throws IOException, JSONException {

        cidade = Utils.encodeString(cidade);
        HttpHelper http = new HttpHelper();
        String json = http.doGet(url+"/cidade/"+cidade+"/estado/"+estado+"/categoria/"+tipo);
        Log.i("url",url+"/cidade/"+cidade+"/estado/"+estado+"/categoria/"+tipo);

        if (json!=null && json.length()>5) {
            List<Empresa> empresas = parserJSON(context,json);
            salvarEmpresasByTipo(context, empresas, empresas.get(0).getTipo(),empresas.get(0).getCidade(), empresas.get(0).getEstado());
            salvaHoraData(context, empresas.get(0).getCidade(), empresas.get(0).getEstado());
            return empresas;
        } else {
            return null;
        }
    }
    
    public static void salvaHoraData(Context context, String cidade, String estado){
        Calendar c = Calendar.getInstance();
        int day, month, year, hora, minuto, segundos;
        String dataHora;
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH)+1;
        year = c.get(Calendar.YEAR);
        hora = c.get(Calendar.HOUR)+12;
        minuto = c.get(Calendar.MINUTE);
        segundos = c.get(Calendar.SECOND);
        dataHora = day+"/"+month+"/"+year+" "+hora+"h"+minuto+"m"+segundos+"s";
        String path = "dh"+cidade+estado;
        Log.i("HORA E DATA ATUAL",dataHora);
        Prefs.setString(context, path, dataHora);
        Log.i("DATAHORA MODIF ",path);
    }

    public static void salvarEmpresaFav(Context context, Empresa empresa){
        EmpresaFavDB db = new EmpresaFavDB(context);
        try {
            db.save(empresa);
        } finally {
            db.close();
        }
    }

    public static void salvarEmpresasByCidadeEstado(Context context, List<Empresa> empresas, String cidade, String estado){
        EmpresaDB db = new EmpresaDB(context);
        try {
            db.deleteByCidadeEstado(cidade, estado);
            for (Empresa e:empresas){
                Log.d(TAG,"Salvando a empresa: "+e.getNome());
                db.save(e);
            }
        } finally {
            db.close();
        }
    }

    public static void salvarEmpresasByTipo(Context context, List<Empresa> empresa, String tipo, String cidade, String estado){
        EmpresaDB db = new EmpresaDB(context);
        try {
            db.deleteByTipo(tipo, cidade, estado);
            for (Empresa e:empresa){
                Log.d(TAG,"Salvando a empresa: "+e.getNome());
                db.save(e);
            }
        } finally {
            db.close();
        }
    }

    public static void deletarEmpresaFav(Context context, Empresa empresa){
        EmpresaFavDB db = new EmpresaFavDB(context);
        try {
            db.delete(empresa);
        } finally {
            db.close();
        }
    }

    private static List<Empresa> parserJSON(Context context, String json) throws IOException, JSONException {
        List<Empresa> empresas = new ArrayList<Empresa>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonEmpresa = jsonArray.getJSONObject(i);
                Empresa c = new Empresa();

                c.setId(jsonEmpresa.optInt("id"));
                c.setNome(jsonEmpresa.optString("nome"));
                c.setCidade(jsonEmpresa.optString("cidade"));
                c.setCnpj(jsonEmpresa.optString("cnpj"));
                c.setRua(jsonEmpresa.optString("rua"));
                c.setNumero(jsonEmpresa.optInt("numero"));
                c.setBairro(jsonEmpresa.optString("bairro"));
                c.setEstado(jsonEmpresa.optString("estado"));
                c.setCep(jsonEmpresa.optString("cep"));
                c.setTel(jsonEmpresa.optString("tel"));
                c.setEmail(jsonEmpresa.optString("email"));
                c.setTipo(jsonEmpresa.optString("tipo"));
                c.setHoraInicio(jsonEmpresa.optString("horaInicio"));
                c.setHoraFim(jsonEmpresa.optString("horaFim"));
                c.setUrlFoto(jsonEmpresa.optString("urlFoto"));

                if (LOG_ON) {
                    Log.d(TAG, "Carro " + c.getNome() + " > " + c.getCidade());
                }
                empresas.add(c);
            }
            if (LOG_ON) {
                Log.d(TAG, empresas.size() + " encontrados.");
            }
        } catch (JSONException e) {
            throw new IOException(e.getMessage(), e);
        }
            return empresas;
    }
}