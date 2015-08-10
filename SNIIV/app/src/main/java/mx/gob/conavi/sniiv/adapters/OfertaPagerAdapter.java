package mx.gob.conavi.sniiv.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.fragments.AvanceObraFragment;
import mx.gob.conavi.sniiv.fragments.PCUFragment;
import mx.gob.conavi.sniiv.fragments.TipoViviendaFragment;
import mx.gob.conavi.sniiv.fragments.ValorViviendaFragment;

/**
 * Created by admin on 04/08/15.
 */
public class OfertaPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;

    public OfertaPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new AvanceObraFragment();
            case 1: return new PCUFragment();
            case 2: return new TipoViviendaFragment();
            case 3: return new ValorViviendaFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return mContext.getString(R.string.oferta_section1).toUpperCase(l);
            case 1:
                return mContext.getString(R.string.oferta_section2).toUpperCase(l);
            case 2:
                return mContext.getString(R.string.oferta_section3).toUpperCase(l);
            case 3:
                return mContext.getString(R.string.oferta_section4).toUpperCase(l);
        }
        return null;
    }

    /*public int getIcon(int position) {
        switch (position) {
            case 0:
                return R.drawable.ic_tab_inbox;
            case 1:
                return R.drawable.ic_tab_friends;

            default: return 0;
        }
    }*/
}
