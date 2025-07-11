package com.arteriatech.ss.msec.iscan.v4.customTextViewFonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;


public class PTSansCaptionRegular extends androidx.appcompat.widget.AppCompatTextView {

    public PTSansCaptionRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public PTSansCaptionRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PTSansCaptionRegular(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/PTSansCaption-Regular.ttf");
        setTypeface(tf);

    }
}
