package com.arteriatech.ss.msec.iscan.v4.customTextViewFonts;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class ProximaNovaSoftSemiBold extends TextView {

    public ProximaNovaSoftSemiBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ProximaNovaSoftSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProximaNovaSoftSemiBold(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/ProximaNovaSoft-Semibold.otf");
        setTypeface(tf);

    }
}




