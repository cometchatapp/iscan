package com.arteriatech.ss.msec.iscan.v4.customTextViewFonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class ProximaNovaSoftRegular extends TextView {

    public ProximaNovaSoftRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ProximaNovaSoftRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProximaNovaSoftRegular(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/ProximaNovaSoft-Regular.otf");
        setTypeface(tf);

    }
}



