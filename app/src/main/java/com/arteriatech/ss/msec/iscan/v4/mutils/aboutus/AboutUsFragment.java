package com.arteriatech.ss.msec.iscan.v4.mutils.aboutus;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arteriatech.ss.msec.iscan.v4.R;


/**
 * Created by e10742 on 6/16/2017.
 */

public class AboutUsFragment extends Fragment {

    TextView tvDate = null, tvVersion = null, tvAppName = null, tvCompanyName = null,
            tvWebsite = null, tvEmail = null, tvPhone = null;
    ImageView ivBottomLogo = null;

    String AppName = "";
    String Version = "";
    String Date = "";
    String Company = "";
    String Website = "";
    String Email = "";
    String Phone = "";

    Drawable LogoBottom = null;

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public Drawable getLogoBottom() {
        return LogoBottom;
    }

    public void setLogoBottom(Drawable logoBottom) {
        LogoBottom = logoBottom;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.fragment_aboutus, container,false);

        tvAppName = (TextView) myInflatedView.findViewById(R.id.tv_app_name);
        tvDate = (TextView) myInflatedView.findViewById(R.id.aboutdate);
        tvVersion = (TextView) myInflatedView.findViewById(R.id.aboutversion);
        tvCompanyName = (TextView) myInflatedView.findViewById(R.id.aboutcomp);
        tvWebsite = (TextView) myInflatedView.findViewById(R.id.aboutwebsite);
        tvEmail = (TextView) myInflatedView.findViewById(R.id.aboutemail);
        tvPhone = (TextView) myInflatedView.findViewById(R.id.aboutph);

        ivBottomLogo = (ImageView) myInflatedView.findViewById(R.id.iv_company_logo_bottom);

        try {
            tvAppName.setText(AppName);
            tvVersion.setText(Version);
            tvDate.setText(Date);
            tvCompanyName.setText(Company);
            tvWebsite.setText(Website);
            tvEmail.setText(Email);
            tvPhone.setText(Phone);

            ivBottomLogo.setImageDrawable(LogoBottom);
        } catch (Exception e){
            e.printStackTrace();
        }

        return myInflatedView;
    }
}
