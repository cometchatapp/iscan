package com.arteriatech.ss.msec.iscan.v4.customTextViewFonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class ProximaNovaSoftMedium extends TextView {

    public ProximaNovaSoftMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ProximaNovaSoftMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProximaNovaSoftMedium(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/ProximaNovaSoft-Medium.otf");
        setTypeface(tf);

    }
}



