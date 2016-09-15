package br.com.livroandroid.buscapreco.domain;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.livroandroid.buscapreco.Utils.HttpPost;
import br.com.livroandroid.buscapreco.Utils.Utils;
import br.com.livroandroid.buscapreco.model.Empresa;
import livroandroid.lib.utils.HttpHelper;
import livroandroid.lib.utils.Prefs;

public class EmpresaService {

    private static final String url = "http://192.168.137.1:8080/buscapreco/rest/empresas";
    private static final String TAG = "EmpresaService";
    private static final boolean LOG_ON = false;

    public static List<Empresa> getEmpresasCat(Context context, String categoria, String cidade, String estado) throws IOException, JSONException {
        HttpHelper http = new HttpHelper();
        if (cidade.equals("")) {
            cidade = " ";
            estado = " ";
        }
        cidade = Utils.encodeString(cidade);
        estado = Utils.encodeString(estado);
        String json = http.doGet(url+"/cidade/"+cidade+"/estado/"+estado+"/categoria/"+categoria);
        Log.i("url",url+"/cidade/"+cidade+"/estado/"+estado+"/categoria/"+categoria);
        List<Empresa> empresas = parserJSON(context,json);
        return empresas;
    }

    public static List<Empresa> getEmpresas(Context context,String cidade, String estado) throws IOException, JSONException {
        HttpHelper http = new HttpHelper();
        if (cidade.equals("")) {
            cidade = " ";
            estado = " ";
        }

        cidade = Utils.encodeString(cidade);
        estado = Utils.encodeString(estado);
        Log.i("TESTE",url+"/cidade/"+cidade+"/estado/"+estado);
        String json = http.doGet(url+"/cidade/"+cidade+"/estado/"+estado);
        List<Empresa> empresas = parserJSON(context,json);
        return empresas;
    }

    public static void salvarEmpresaFav(Context context, Empresa empresa){
        EmpresaFavDB db = new EmpresaFavDB(context);
        try {
            db.save(empresa);
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
        if (json.length()>5){
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
        }else return null;
    }
}