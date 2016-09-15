package br.com.livroandroid.buscapreco.domain;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.livroandroid.buscapreco.model.Produto;
import livroandroid.lib.utils.HttpHelper;

public class ProdutoService {

    private static final String url = "http://192.168.137.1:8080/buscapreco/rest/produtos";
    private static final String TAG = "ProdutoService";
    private static final boolean LOG_ON = false;

    /*public static List<Produto> getProdutos(Context context){
        List<Produto> produtos = new ArrayList<>();
        /for (int i=0;i<20;i++){
            Produto p = new Produto();
            p.nome = "Produto "+i;
            p.preco = "Preço "+i;
            produtos.add(p);
        }/
        return produtos;
    }
    */

    public static List<Produto> getProdutosByEmpresa(Context context, Long id) throws IOException, JSONException {
        HttpHelper http = new HttpHelper();
        String json = http.doGet(url.concat("/empresa/"+id));
        return parserJSON(context,json);
    }

    public static List<Produto> getProdutosByEmpresaPromocao(Context context, Long id) throws IOException, JSONException {
        HttpHelper http = new HttpHelper();
        String json = http.doGet(url.concat("/empresa/promocao/"+id));
        return parserJSON(context,json);
    }

    private static List<Produto> parserJSON(Context context, String json) throws IOException, JSONException {
        List<Produto> produtos = new ArrayList<Produto>();
        if (json.length()>5){
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonProduto = jsonArray.getJSONObject(i);
                    Produto p = new Produto();

                    p.setId(jsonProduto.optInt("id"));
                    p.setNome(jsonProduto.optString("nome"));
                    p.setPrecoVista(Float.parseFloat(jsonProduto.optString("precoVista")));
                    p.setPrecoPromocao(Float.parseFloat(jsonProduto.optString("precoPromocao")));
                    p.setUnidade(jsonProduto.optString("unidade"));
                    p.setCodBarras(jsonProduto.optString("codBarras"));

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
        }else return null;
    }
}