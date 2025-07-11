package com.arteriatech.ss.msec.iscan.v4.mutils.registration;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arteriatech.ss.msec.iscan.v4.R;

/**
 * Created by e10769 on 27-04-2017.
 */

public class MainMenuVH extends RecyclerView.ViewHolder {
    public final ImageView ivMenuImage;
    public final TextView tvMenuText;

    public MainMenuVH(View itemView) {
        super(itemView);
        ivMenuImage = (ImageView)itemView.findViewById(R.id.iv_menu);
        tvMenuText = (TextView)itemView.findViewById(R.id.icon_text);
    }
}
