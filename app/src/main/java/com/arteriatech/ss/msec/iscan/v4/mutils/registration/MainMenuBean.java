package com.arteriatech.ss.msec.iscan.v4.mutils.registration;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

/**
 * Created by e10769 on 20-09-2017.
 */

public class MainMenuBean implements Serializable {
    private String menuName;
    private int menuImage;
    private int id;
    private int menuRightImage;
    private String itemType;

    public String isTitleFlag() {
        return titleFlag;
    }

    public void setTitleFlag(String titleFlag) {
        this.titleFlag = titleFlag;
    }

    private String titleFlag="";
    private Class<? extends AppCompatActivity> activityRedirect=null;

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(int menuImage) {
        this.menuImage = menuImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getMenuRightImage() {
        return menuRightImage;
    }

    public void setMenuRightImage(int menuRightImage) {
        this.menuRightImage = menuRightImage;
    }

    public Class<? extends AppCompatActivity> getActivityRedirect() {
        return activityRedirect;
    }

    public void setActivityRedirect(Class<? extends AppCompatActivity> activityRedirect) {
        this.activityRedirect = activityRedirect;
    }
}
