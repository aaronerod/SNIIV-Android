package mx.gob.conavi.sniiv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.Utils.Utils;

public class MainActivity extends Activity {

    ListView lvwMenu;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> listMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvwMenu = (ListView) findViewById(R.id.lvwMenu);
        Utils u=new Utils();
        listMenu=u.getListMenu();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listMenu);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
