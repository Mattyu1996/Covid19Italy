package it.univaq.disim.mwt.covid19italy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView stato = findViewById(R.id.valore_stato);
        TextView regione = findViewById(R.id.valore_regione);
        TextView sigla = findViewById(R.id.valore_sigla);
        TextView codiceNuts1 = findViewById(R.id.valore_codice_nuts_1);
        TextView codiceNuts2 = findViewById(R.id.valore_codice_nuts_2);
        TextView codiceNuts3 = findViewById(R.id.valore_codice_nuts_3);
        TextView codiceProvincia = findViewById(R.id.valore_codice_provincia);
        TextView codiceRegione = findViewById(R.id.valore_codice_regione);
        TextView casi = findViewById(R.id.valore_casi);
        TextView ultimoAggiornamento = findViewById(R.id.valore_ultimo_aggiornamento);


        //Ottengo l'intent
        Intent intent = getIntent();
        //Ottengo la piattaforma dall'intent
        Provincia provincia = intent.getParcelableExtra("provincia");
        if(provincia != null){
            //Imposto il nome della provincia come titolo dell'activity
            toolbar.setTitle(provincia.getNome());
            //Imposto tutti i dettagli della piattaforma
            stato.setText(provincia.getStato());
            regione.setText(provincia.getRegione());
            sigla.setText(provincia.getSigla());
            codiceNuts1.setText(provincia.getCodiceNuts1());
            codiceNuts2.setText(provincia.getCodiceNuts2());
            codiceNuts3.setText(provincia.getCodiceNuts3());
            codiceProvincia.setText(""+provincia.getCodiceProvincia());
            codiceRegione.setText(""+provincia.getCodiceRegione());
            casi.setText(""+provincia.getTotaleCasi());
            ultimoAggiornamento.setText(provincia.getLastUpdateDateTime().toString());
        }

        setSupportActionBar(toolbar);


    }

}
