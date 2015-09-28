package mx.gob.conavi.sniiv.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.fragments.demanda.FinanciamientosFragment;
import mx.gob.conavi.sniiv.fragments.demanda.SubsidiosFragment;

/**
 * Created by admin on 04/08/15.
 */
public class DemandaPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;

    public DemandaPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new FinanciamientosFragment();
            case 1: return new SubsidiosFragment();
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
