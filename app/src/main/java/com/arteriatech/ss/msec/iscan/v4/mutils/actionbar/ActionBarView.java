package com.arteriatech.ss.msec.iscan.v4.mutils.actionbar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arteriatech.ss.msec.iscan.v4.R;

/**
 * Created by e10742 on 6/19/2017.
 */

public class ActionBarView {


    public static void initActionBarView(final AppCompatActivity mActivity, boolean homeUpEnabled, String title, Drawable backImage, Drawable appIcon) {
        try {

            if (homeUpEnabled)
                mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            else
                mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            mActivity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_title_bar, null);
            TextView textView = (TextView) view.findViewById(R.id.txtTitle);
            ImageView imgIcon = (ImageView) view.findViewById(R.id.imgIcon);
            ImageView backImg = (ImageView) view.findViewById(R.id.img_back);

            if (backImage != null) {
                backImg.setImageDrawable(backImage);
            }

            if (appIcon != null) {
                imgIcon.setImageDrawable(appIcon);
            }
            textView.setText(title);
            if (!homeUpEnabled) {
                backImg.setVisibility(View.GONE);
            } else {
                backImg.setVisibility(View.VISIBLE);
                imgIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mActivity.onBackPressed();
                    }
                });
            }
            backImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mActivity.onBackPressed();
                }
            });

            //  displayBackButton(backImg,title);
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, Gravity.START);
            mActivity.getSupportActionBar().setDisplayShowCustomEnabled(true);
            mActivity.getSupportActionBar().setCustomView(view, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*actionbar center image*/
    public static void initActionBarView(AppCompatActivity mActivity, Toolbar toolbar, boolean homeUpEnabled, String title, int appIcons, int backButtonIcon) {
        try {
            mActivity.setSupportActionBar(toolbar);
            mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(homeUpEnabled);
            if (homeUpEnabled) {
                if (backButtonIcon != 0)
                    toolbar.setNavigationIcon(AppCompatResources.getDrawable(mActivity, backButtonIcon));
//                else
//                    toolbar.setNavigationIcon(AppCompatResources.getDrawable(mActivity, R.drawable.ic_arrow_back));
            }
            if (appIcons != 0)
                mActivity.getSupportActionBar().setIcon(appIcons);
            if (TextUtils.isEmpty(title)) {
                mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            } else {
                mActivity.getSupportActionBar().setDisplayShowTitleEnabled(true);
                mActivity.getSupportActionBar().setTitle(title);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
