package mx.gob.conavi.sniiv;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;


public class OfertaActivity extends Activity {

    TabHost th;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oferta);
        th=(TabHost)findViewById(R.id.th);

        //TAB1 Avance Obra
        th.setup();
        TabSpec ts1=th.newTabSpec("Tab1");
        ts1.setIndicator("Avance Obra");
        ts1.setContent(R.id.tab1);
        th.addTab(ts1);


        //TAB2 PCU
        th.setup();
        TabSpec ts2=th.newTabSpec("Tab2");
        ts2.setIndicator("PCU");
        ts2.setContent(R.id.tab2);

        th.addTab(ts2);



        //TAB3 Tipo Vivienda
        th.setup();
        TabSpec ts3=th.newTabSpec("Tab3");
        ts3.setIndicator("Tipo Vivienda");
        ts3.setContent(R.id.tab3);

        th.addTab(ts3);



        //TAB4 Valor vivienda
        th.setup();
        TabSpec ts4=th.newTabSpec("Tab4");
        ts4.setIndicator("Tipo Vivienda");
        ts4.setContent(R.id.tab4);

        th.addTab(ts4);



    }
}
