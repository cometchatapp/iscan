package com.arteriatech.ss.msec.iscan.v4.introscreen.model;

public class IntroModel {
    String titleName,titleDesc;
    int ivIcon;

    public IntroModel(String titleName, String titleDesc, int ivIcon) {
        this.titleName = titleName;
        this.titleDesc = titleDesc;
        this.ivIcon = ivIcon;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getTitleDesc() {
        return titleDesc;
    }

    public void setTitleDesc(String titleDesc) {
        this.titleDesc = titleDesc;
    }

    public int getIvIcon() {
        return ivIcon;
    }

    public void setIvIcon(int ivIcon) {
        this.ivIcon = ivIcon;
    }
}
