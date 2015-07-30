package mx.gob.conavi.sniiv;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.Utils.Utils;
import android.widget.NumberPicker.OnValueChangeListener;

public class ReporteGeneralActivity extends Activity implements AdapterView.OnItemSelectedListener, NumberPicker.OnClickListener {


    private Utils u;
    private NumberPicker pickerEstados;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportegeneral);

        String[] edos = Utils.listEdo;
        pickerEstados = (NumberPicker) findViewById(R.id.pckEstados);
        pickerEstados.setMaxValue(edos.length - 1);
        pickerEstados.setMinValue(0);
        pickerEstados.setDisplayedValues(edos);
        pickerEstados.setOnClickListener(this);

        pickerEstados.setOnValueChangedListener(new OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {


                //To implement function to change values ///"acciones" y "montos"///
                // showMontos(newVal) <--- Like This

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

    }
}
