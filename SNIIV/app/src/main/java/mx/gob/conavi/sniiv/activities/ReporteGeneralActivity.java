package mx.gob.conavi.sniiv.activities;

import android.support.v4.app.Fragment;

import mx.gob.conavi.sniiv.activities.SingleFragmentActivity;
import mx.gob.conavi.sniiv.fragments.ReporteGeneralFragment;

public class ReporteGeneralActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new ReporteGeneralFragment();
    }
}
