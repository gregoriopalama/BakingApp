package com.gregoriopalama.udacity.bakingapp.ui.recipe;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.gregoriopalama.udacity.bakingapp.R;

/**
 * Pager adapter to show a list of ingredients and a list of steps for a given recipe
 *
 * @author Gregorio Palamà
 */

public class RecipePagerAdapter extends FragmentStatePagerAdapter {
    private Context context;
    SparseArray<Fragment> fragments = new SparseArray<Fragment>();


    public RecipePagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return StepsFragment.getInstance();
        }

        return IngredientsFragment.getInstance();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.tab_title_steps);
            case 1:
                return context.getResources().getString(R.string.tab_title_ingredients);
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        fragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        fragments.remove(position);
        super.destroyItem(container, position, object);
    }

}
