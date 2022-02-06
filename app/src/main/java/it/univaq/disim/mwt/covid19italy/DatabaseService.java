package it.univaq.disim.mwt.covid19italy;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService extends IntentService {
    public static final String FILTER_GET = "it.univaq.disim.mwt.covid19italy.GET";
    public static final String EXTRA_ACTION = "extra_action";
    public static final int ACTION_SAVE     = 0;
    public static final int ACTION_GET    = 1;

    public DatabaseService() {
        super("DatabaseService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int action = intent.getIntExtra(EXTRA_ACTION, -1);

        switch (action){
            case ACTION_SAVE:
                this.save(intent);
                break;
            case ACTION_GET:
                this.get();
                break;
        }
    }

    private void save(Intent intent){
        ArrayList<Provincia> province = intent.getParcelableArrayListExtra("province");
        System.out.println("Numero di pronvince dall'intent: "+ province.size());
        DataBase.getInstance(getApplicationContext()).provincia_dao().save(province);
    }

    private void get() {
        //Ottengo le Preferenze "preferenze"
        SharedPreferences pref = getSharedPreferences("preferenze", Context.MODE_PRIVATE);
        //Se è la prima volta che apro l'applicazione dopo averla installata effettuo la richiesta http
        if (pref.getBoolean("firstTime", true)) {
            Log.i("RICHIESTA HTTP", "EFFETTUO RICHIESTA HTTP");
            StringRequest richiesta = new StringRequest("https://raw.githubusercontent.com/pcm-dpc/COVID-19/master/dati-json/dpc-covid19-ita-province-latest.json",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // ANDATO TUTTO BENE
                            //System.out.println(response);
                            final ArrayList<Provincia> province = new ArrayList<>();
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject json = array.optJSONObject(i);
                                    if (json == null || json.optString("lat") == "null") continue;

                                    Provincia p = new Provincia();

                                    //Parsing del JSON
                                    p.setNome(json.optString("denominazione_provincia"));
                                    p.setRegione(json.optString("denominazione_regione"));
                                    p.setStato(json.optString("stato"));
                                    p.setSigla(json.optString("sigla_provincia"));
                                    p.setCodiceNuts1(json.optString("codice_nuts_1"));
                                    p.setCodiceNuts2(json.optString("codice_nuts_2"));
                                    p.setCodiceNuts3(json.optString("codice_nuts_3"));
                                    p.setCodiceProvincia(json.optInt("codice_provincia"));
                                    p.setCodiceRegione(json.optInt("codice_regione"));
                                    p.setTotaleCasi(json.optInt("totale_casi"));
                                    p.setLatitudine(json.getDouble("lat"));
                                    p.setLongitudine(json.getDouble("long"));
                                    p.setLastUpdateDateTime(new SimpleDateFormat("yyyy-MM-dd").parse(json.optString("data")));
                                    
                                    //Aggiungo la provincia alla lista
                                    province.add(p);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            Log.i("RICHIESTA HTTP","La richiesta http ha ottenuto: " + province.size() + " province");
                            //Inserire nel DB
                            Thread t = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    List<Long> insert = DataBase.getInstance(getApplicationContext()).provincia_dao().save(province);
                                    System.out.println("Inserite nel Database: " + insert.size() + " province");
                                }
                            });
                            t.start();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Richiesta Http fallita");
                    System.out.println(error.toString());
                }
            });
            //Aggiungo la richiesta alla coda di Volley
            MyVolley.getInstance(this).getQueue().add(richiesta);
            //Imposto il valore di firstTime a falso per non dover più effettuare la richiesta http per ottenere le piattaforme
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("firstTime", false);
            editor.apply();
        }
        else {
            //Altrimenti prendo la lista delle piattaforme dal Database e le restituisco con il LocalBroadcastManager
            List<Provincia> province = DataBase.getInstance(getApplicationContext()).provincia_dao().getAll();
            ArrayList<Provincia> piattaforme = new ArrayList<Provincia>(province);
            Intent intent = new Intent(FILTER_GET);
            intent.putParcelableArrayListExtra("province", piattaforme);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }
}
