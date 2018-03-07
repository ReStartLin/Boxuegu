package cn.edu.gdmec.android.boxuegu.adapter;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.edu.gdmec.android.boxuegu.bean.CourseBean;
import cn.edu.gdmec.android.boxuegu.fragment.AdBannerFragment;
import cn.edu.gdmec.android.boxuegu.view.CourseView;

public class AdBannerAdapter extends FragmentStatePagerAdapter implements View.OnTouchListener{

    private List<CourseBean> cad1;
    private Handler mHandler;

    public AdBannerAdapter(FragmentManager fm) {
        super(fm);
        cad1 = new ArrayList<CourseBean>();
    }

    public AdBannerAdapter(FragmentManager fm,Handler handler) {
        super(fm);
        cad1 = new ArrayList<CourseBean>();
        mHandler = handler;
    }

    public void setDatas(List<CourseBean> cad1) {
        this.cad1 = cad1;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        //防止刷新结果显示列表是出现缓存数据，重载此方法，使之默认返回position_none
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        if (cad1.size()>0){
            args.putString("ad",cad1.get(position % cad1.size()).icon);
        }
        return AdBannerFragment.newInstance(args);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    public int getSize(){
        return cad1 == null ? 0 : cad1.size();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mHandler.removeMessages(CourseView.MSG_AD_SLID);
        return false;
    }
}
