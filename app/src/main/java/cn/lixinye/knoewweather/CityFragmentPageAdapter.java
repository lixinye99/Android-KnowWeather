package cn.lixinye.knoewweather;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class CityFragmentPageAdapter extends FragmentStatePagerAdapter {
    List<Fragment> fragmentList;
    public CityFragmentPageAdapter(FragmentManager fm, List<Fragment> fragmentLis) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragmentList = fragmentLis;

    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    //表示ViewPage包含的页数
    int childCount = 0;
    //当viewpage的页数发生变化的时候，必须要重写两函数


    @Override
    public void notifyDataSetChanged() {
        this.childCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (childCount>0) {
            childCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
