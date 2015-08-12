package mx.gob.conavi.sniiv.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.fragments.ReporteGeneralFragment;

public class ReporteGeneralActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new ReporteGeneralFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.title_reporte_general);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
    }
}
