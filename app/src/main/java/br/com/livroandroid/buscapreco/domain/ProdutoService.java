package br.com.livroandroid.buscapreco.domain;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.livroandroid.buscapreco.model.Produto;
import livroandroid.lib.utils.HttpHelper;
import livroandroid.lib.utils.Prefs;

public class ProdutoService {

    private static final String url = "http://192.168.1.104:8080/buscapreco/rest/produtos";
    private static final String TAG = "ProdutoService";
    private static final boolean LOG_ON = false;

    public static List<Produto> getProdutosByEmpresa(Context context, Long id, boolean refresh) throws IOException, JSONException {
        List<Produto> produtos;
        boolean buscaNoBancoDeDados = !refresh;

        if (buscaNoBancoDeDados) {
            produtos = getProdutosFromBanco(context, id);
            if (produtos!=null && produtos.size()>0){
                return produtos;
            }
        }

        produtos = getProdutosFromWebService(context, id);
        return produtos;
    }

    private static List<Produto> getProdutosFromBanco(Context context, Long idEmpresa) {
        Log.i("NAO Pegou PROMOCAO ","NAO VEIO PROMOCAO BANCO");
        ProdutoDB db = new ProdutoDB(context);
        try {
            List<Produto> produtos = db.findByEmpresa(idEmpresa);
            Log.d(TAG,"Retornando " + produtos.size() + " produtos do banco");
            return produtos;
        } finally {
            db.close();
        }
    }

    private static List<Produto> getProdutosFromWebService(Context context, Long idEmpresa) throws IOException, JSONException {
        HttpHelper http = new HttpHelper();
        String json = http.doGet(url.concat("/empresa/"+idEmpresa));
        if (json!=null && json.length()>5) {
            List<Produto> produtos = parserJSON(context, json, idEmpresa);
            salvarProdutoByEmpresa(context, produtos, idEmpresa);
            salvaHoraData(context,idEmpresa);
            return produtos;
        } else {
            return null;
        }
    }

    public static List<Produto> getProdutosByEmpresaPromocao(Context context, Long idEmpresa, boolean refresh) throws IOException, JSONException {
        List<Produto> produtos;

        boolean buscaNoBancoDeDados = !refresh;

        if (buscaNoBancoDeDados) {
            produtos = getProdutosPromocaoFromBanco(context, idEmpresa);
            if (produtos!=null && produtos.size()>0){
                return produtos;
            }
        }

        produtos = getProdutosPromocaoFromWebService(context, idEmpresa);
        return produtos;
    }

    private static List<Produto> getProdutosPromocaoFromBanco(Context context, Long idEmpresa) {
        Log.i("Pegou PROMOCAO ","PROMOCAO BANCO");
        ProdutoDB db = new ProdutoDB(context);
        try {
            List<Produto> produtos = db.findByEmpresaPromocao(idEmpresa);
            Log.d(TAG,"Retornando " + produtos.size() + " produtos do banco");
            return produtos;
        } finally {
            db.close();
        }
    }

    private static List<Produto> getProdutosPromocaoFromWebService(Context context, Long idEmpresa) throws IOException, JSONException {
        HttpHelper http = new HttpHelper();
        String json = http.doGet(url.concat("/empresa/promocao/"+idEmpresa));
        if (json!=null && json.length()>5) {
            List<Produto> produtos = parserJSON(context, json, idEmpresa);
            salvarProdutoByEmpresaPromocao(context, produtos, idEmpresa);
            salvaHoraDataPromocao(context, idEmpresa);
            return produtos;
        } else {
            return null;
        }
    }

    public static void salvarProdutoByEmpresa(Context context, List<Produto> produtos, Long idEmpresa){
        ProdutoDB db = new ProdutoDB(context);
        try {
            db.deleteByEmpresa(idEmpresa);
            for (Produto p:produtos){
                Log.d(TAG,"Salvando o produto: "+p.getNome());
                db.save(p);
            }
        } finally {
            db.close();
        }
    }

    public static void salvarProdutoByEmpresaPromocao(Context context, List<Produto> produtos, Long idEmpresa){
        ProdutoDB db = new ProdutoDB(context);
        try {
            db.deleteByEmpresaPromocao(idEmpresa);
            for (Produto p:produtos){
                Log.d(TAG,"Preço PROMO: "+p.getPrecoPromocao());
                db.save(p);
            }
        } finally {
            db.close();
        }
    }

    public static void salvaHoraData(Context context, Long idEmpresa){
        Calendar c = Calendar.getInstance();
        int day, month, year, hora, minuto, segundos;
        String dataHora;
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH)+1;
        year = c.get(Calendar.YEAR);
        hora = c.get(Calendar.HOUR_OF_DAY);
        minuto = c.get(Calendar.MINUTE);
        segundos = c.get(Calendar.SECOND);
        dataHora = day+"/"+month+"/"+year+" "+hora+"h"+minuto+"m"+segundos+"s";
        String path = "dhP".concat(String.valueOf(idEmpresa));
        Prefs.setString(context, path, dataHora);
        Log.i("DATAHORA MODIF ",path);
    }

    public static void salvaHoraDataPromocao(Context context, Long idEmpresa){
        Calendar c = Calendar.getInstance();
        int day, month, year, hora, minuto, segundos;
        String dataHora;
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH)+1;
        year = c.get(Calendar.YEAR);
        hora = c.get(Calendar.HOUR_OF_DAY);
        minuto = c.get(Calendar.MINUTE);
        segundos = c.get(Calendar.SECOND);
        dataHora = day+"/"+month+"/"+year+" "+hora+"h"+minuto+"m"+segundos+"s";
        String path = "dhPP".concat(String.valueOf(idEmpresa));
        Prefs.setString(context, path, dataHora);
        Log.i("DATAHORA MODIF ",path);
    }

    private static List<Produto> parserJSON(Context context, String json, long id) throws IOException, JSONException {
        List<Produto> produtos = new ArrayList<Produto>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonProduto = jsonArray.getJSONObject(i);
                Produto p = new Produto();

                p.setId(jsonProduto.optInt("id"));
                p.setIdEmpresa(id);
                p.setNome(jsonProduto.optString("nome"));
                p.setPrecoVista(Float.parseFloat(jsonProduto.optString("precoVista")));
                p.setPrecoPromocao(Float.parseFloat(jsonProduto.optString("precoPromocao")));
                p.setUnidade(jsonProduto.optString("unidade"));
                p.setCodBarras(jsonProduto.optString("codBarras"));
                p.setUrlFoto(jsonProduto.optString("urlFoto"));
                if (LOG_ON) {
                    Log.d(TAG, "Carro " + p.getNome() + " > " + p.getPrecoVista());
                }
                produtos.add(p);
            }
            if (LOG_ON) {
                Log.d(TAG, produtos.size() + " encontrados.");
            }
        } catch (JSONException e) {
            throw new IOException(e.getMessage(), e);
        }
        return produtos;
    }
}