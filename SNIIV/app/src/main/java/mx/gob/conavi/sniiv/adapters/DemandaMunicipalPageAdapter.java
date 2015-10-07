package mx.gob.conavi.sniiv.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.fragments.evolucion.EvolucionFragment;
import mx.gob.conavi.sniiv.modelos.EvolucionTipo;

/**
 * Created by octavio.munguia on 07/10/2015.
 */
public class DemandaMunicipalPageAdapter extends FragmentPagerAdapter {
    private final Context mContext;

    public DemandaMunicipalPageAdapter(Context mContext, FragmentManager fm) {
        super(fm);
        this.mContext = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return EvolucionFragment.newInstance(EvolucionTipo.FINANCIAMIENTOS);
            case 1: return EvolucionFragment.newInstance(EvolucionTipo.SUBSIDIOS);
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return mContext.getString(R.string.title_financiamiento).toUpperCase(l);
            case 1:
                return mContext.getString(R.string.title_subsidios).toUpperCase(l);
        }
        return null;
    }
}