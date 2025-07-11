package com.arteriatech.ss.msec.iscan.v4.introscreen.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.arteriatech.ss.msec.iscan.v4.databinding.LayoutIntroBinding;
import com.arteriatech.ss.msec.iscan.v4.introscreen.model.IntroModel;

import java.util.ArrayList;
import java.util.List;

public class IntroViewPagerSliderAdapter extends PagerAdapter {
    List<IntroModel> introModels = new ArrayList<>();

    public void addSlide(IntroModel introModel) {
        introModels.add(introModel);
    }

    @Override
    public int getCount() {
        return introModels.size();
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutIntroBinding layoutIntroBinding = LayoutIntroBinding.inflate(LayoutInflater.from(container.getContext()), container, false);
        layoutIntroBinding.tvTitleName.setText(introModels.get(position).getTitleName());
        layoutIntroBinding.tvTitleDesc.setText(introModels.get(position).getTitleDesc());
        layoutIntroBinding.ivIntroIcon.setImageResource(introModels.get(position).getIvIcon());
        container.addView(layoutIntroBinding.getRoot());
        return layoutIntroBinding.getRoot();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ViewGroup) object);
    }
}
