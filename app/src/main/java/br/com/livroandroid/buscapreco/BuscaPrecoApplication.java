package br.com.livroandroid.buscapreco;

import android.app.Application;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class BuscaPrecoApplication extends Application {
    private static final String TAG = "BuscaPrecoApplication";
    private static BuscaPrecoApplication instance = null;

    public static BuscaPrecoApplication getInstance(){
        return instance; //Singleton
    }

    private Map<String, Boolean> mapUpdate = new HashMap<String, Boolean>();

    public void setNeedToUpdate(String type, boolean b){
        this.mapUpdate.put(type,b);
    }

    public boolean isNeedToUpdate(String type){
        if (mapUpdate.containsKey(type)){
            boolean b = mapUpdate.remove(type);
            return b;
        }
        return false;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "BuscaPrecoApplication.onCreate()");
        //Salva a instancia para termos acesso como Singleton
        instance=this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "BuscaPrecoApplication.oTerminate()");
    }
}