package mx.gob.conavi.sniiv.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.Utils.Utils;

/**
 * Created by admin on 04/08/15.
 */
public class AvanceObraFragment extends Fragment {
    public static final String TAG = "ReporteGeneralActivity";
    private NumberPicker pickerEstados;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_avance_obra, container, false);

        String[] edos = Utils.listEdo;
        pickerEstados = (NumberPicker) rootView.findViewById(R.id.pckEstados);
        pickerEstados.setMaxValue(edos.length - 1);
        pickerEstados.setMinValue(0);
        pickerEstados.setDisplayedValues(edos);
        //pickerEstados.setOnScrollListener(scrollListener);
        return rootView;
    }
}
