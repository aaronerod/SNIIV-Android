package mx.gob.conavi.sniiv.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.gob.conavi.sniiv.R;

/**
 * Created by admin on 04/08/15.
 */
public class ValorViviendaFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_valor_vivienda, container, false);

        return rootView;
    }
}
