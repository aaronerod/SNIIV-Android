package mx.gob.conavi.sniiv.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.Utils.Utils;

public class MainActivity extends AppCompatActivity {

    ListView lvwMenu;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> listMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvwMenu = (ListView) findViewById(R.id.lvwMenu);
        listMenu= Utils.listMenu;
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listMenu);
        lvwMenu.setAdapter(arrayAdapter);
        lvwMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                changeActivity(position);
            }
        });
    }

    public void changeActivity(int position) {
        Intent intent=null;

        if (position == 0) {
            intent=new Intent(this, ReporteGeneralActivity.class);
        } else if (position == 1) {
            intent=new Intent(this, OfertaActivity.class);
        } else if (position == 2)
        {
            intent=new Intent(this, DemandaActivity.class);
        }

        startActivity(intent);
    }
}
