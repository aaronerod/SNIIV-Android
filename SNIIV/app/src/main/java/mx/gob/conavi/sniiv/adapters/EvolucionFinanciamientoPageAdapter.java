package mx.gob.conavi.sniiv.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.fragments.oferta.AvanceObraFragment;
import mx.gob.conavi.sniiv.fragments.oferta.PCUFragment;
import mx.gob.conavi.sniiv.fragments.oferta.TipoViviendaFragment;

/**
 * Created by octavio.munguia on 28/09/2015.
 */
public class EvolucionFinanciamientoPageAdapter extends FragmentPagerAdapter {
    private final Context mContext;

    public EvolucionFinanciamientoPageAdapter(Context mContext, FragmentManager fm) {
        super(fm);
        this.mContext = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new AvanceObraFragment();
            case 1: return new PCUFragment();
            case 2: return new TipoViviendaFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return mContext.getString(R.string.title_financiamiento).toUpperCase(l);
            case 1:
                return mContext.getString(R.string.title_subsidios).toUpperCase(l);
            case 2:
                return mContext.getString(R.string.title_oferta_vivienda).toUpperCase(l);
        }
        return null;
    }
}
