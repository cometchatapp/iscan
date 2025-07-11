/*
package com.arteriatech.ss.msec.bil.v4.original;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arteriatech.ss.msec.bil.v4.R;
import com.arteriatech.ss.msec.bil.v4.common.Constants;
import com.arteriatech.ss.msec.bil.v4.mbo.SKUGroupBean;

import java.util.ArrayList;

*/
/**
 * Created by e10769 on 09-06-2017.
 *//*


public class FirstBodyHeaderViewGroup extends FrameLayout implements TableFixHeaderAdapter.FirstBodyBinder<SKUGroupBean>, TableFixHeaderAdapter.FirstHeaderBinder<String> {
    private Context context;
    private TextView textView = null;
    private View vg_root = null;
    private ImageView ivSkuGrpScheme = null;
    private ImageView ivMatScheme = null;
    private ImageView ivExpandIcon = null;
    private LinearLayout ll_header_body_area = null;

    public FirstBodyHeaderViewGroup(Context context) {
        super(context);
        this.context = context;
        initUI();
    }

    private void initUI() {
        LayoutInflater.from(context).inflate(R.layout.header_body_view, this, true);
        textView = (TextView) findViewById(R.id.tv_item_so_create_sku_grp);
        ivSkuGrpScheme = (ImageView) findViewById(R.id.iv_sku_grp_scheme);
        ivMatScheme = (ImageView) findViewById(R.id.iv_mat_scheme);
        ivExpandIcon = (ImageView) findViewById(R.id.iv_expand_icon);
        ll_header_body_area = (LinearLayout) findViewById(R.id.ll_header_body_area);
        vg_root = findViewById(R.id.vg_root);
    }

    @Override
    public void bindFirstHeader(String item) {

    }

    @Override
    public void bindFirstBody(final SKUGroupBean item, final int row, final int column, final TableFixHeaderAdapter tableFixHeaderAdapter, final TableFixHeaderAdapter.ClickListener clickListenerFirstBody) {
        if (textView != null) {
            if (item.isHeader()) {
                textView.setText(item.getSKUGroupDesc());
                if (item.getUnBilledStatus().equalsIgnoreCase("")) {
                    textView.setTextColor(getResources().getColor(R.color.RED));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.BLACK));
                }


            } else {
                textView.setText(item.getMaterialDesc() + " - " + item.getMaterialNo());
                textView.setTextColor(getResources().getColor(R.color.BLACK));
            }
            Constants.setFontSizeByMaxText(textView);

            if (ll_header_body_area != null) {
                if (item.isHeader()) {
                    try {
                        // Must sell and focused products are orange color
                        if (item.getMatTypeVal().equalsIgnoreCase(Constants.str_01) || item.getMatTypeVal().equalsIgnoreCase(Constants.str_02)) {
                            ll_header_body_area.setBackgroundColor(getResources().getColor(R.color.ORANGE));
                        } else
                            //  new launched products are blue color
                            if (item.getMatTypeVal().equalsIgnoreCase(Constants.str_03)) {
                                ll_header_body_area.setBackgroundColor(getResources().getColor(R.color.light_blue_color));
                            }
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        // Must sell and focused products are orange color
                        if (item.getMatTypeVal().equalsIgnoreCase(Constants.str_01) || item.getMatTypeVal().equalsIgnoreCase(Constants.str_02)) {
                            ll_header_body_area.setBackgroundColor(getResources().getColor(R.color.ORANGE));
                        } else
                            //  new launched products are blue color
                            if (item.getMatTypeVal().equalsIgnoreCase(Constants.str_03)) {
                                ll_header_body_area.setBackgroundColor(getResources().getColor(R.color.light_blue_color));
                            }
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        if (ivExpandIcon != null) {
            if (item.isHeader()) {
                ivExpandIcon.setVisibility(VISIBLE);
                ivExpandIcon.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickListenerFirstBody != null) {
                            //clickListenerFirstBody.onClickItem(item, null, row, column, tableFixHeaderAdapter, ivExpandIcon);
                        }
                    }
                });
                if (!item.isViewOpened()) {
                    ivExpandIcon.setImageResource(R.drawable.down);
                } else {
                    ivExpandIcon.setImageResource(R.drawable.up);
                }
            } else {
                ivExpandIcon.setVisibility(GONE);
            }

        }
        if (ivSkuGrpScheme != null) {
            if (item.isHeader()) {
                if (item.getIsSchemeActive().equalsIgnoreCase(Constants.X) || item.getSchemeQPSActive().equalsIgnoreCase(Constants.X)) {
                    ivSkuGrpScheme.setVisibility(VISIBLE);
                } else {
                    ivSkuGrpScheme.setVisibility(GONE);
                }
                ivSkuGrpScheme.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!item.getSchemeGuid().isEmpty() && !item.getQPSSchemeGuid().equalsIgnoreCase("")) {
                            openSchemeActivity(TextUtils.join(",", item.getSchemeGuid()) + "," + item.getQPSSchemeGuid());
                        } else if (!item.getSchemeGuid().isEmpty() && item.getQPSSchemeGuid().equalsIgnoreCase("")) {
                            openSchemeActivity(TextUtils.join(",", item.getSchemeGuid()));
                        } else if (item.getSchemeGuid().isEmpty() && !item.getQPSSchemeGuid().equalsIgnoreCase("")) {
                            openSchemeActivity(item.getQPSSchemeGuid());
                        }
                    }
                });


            } else {
                ivSkuGrpScheme.setVisibility(GONE);

                ivSkuGrpScheme.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!item.getSchemeGuid().isEmpty() && !item.getQPSSchemeGuid().equalsIgnoreCase("")) {
                            openSchemeActivity(TextUtils.join(",", item.getSchemeGuid()) + "," + item.getQPSSchemeGuid());
                        } else if (!item.getSchemeGuid().isEmpty() && item.getQPSSchemeGuid().equalsIgnoreCase("")) {
                            openSchemeActivity(TextUtils.join(",", item.getSchemeGuid()));
                        } else if (item.getSchemeGuid().isEmpty() && !item.getQPSSchemeGuid().equalsIgnoreCase("")) {
                            openSchemeActivity(item.getQPSSchemeGuid());
                        }
                    }
                });
            }
        }
        if (ivMatScheme != null) {
            if (item.isHeader()) {
                if (item.getIsMaterialActive().equalsIgnoreCase(Constants.X)) {
                    ivMatScheme.setVisibility(VISIBLE);
                } else {
                    ivMatScheme.setVisibility(GONE);
                }
            } else {
                if (item.isMatLevelImageDisplay()) {
                    ivMatScheme.setVisibility(View.VISIBLE);
                } else {
                    ivMatScheme.setVisibility(View.GONE);
                }
                ivMatScheme.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<String> mStrMatSchemeList = new ArrayList<>();
                        try {
                            mStrMatSchemeList = Constants.MAPSCHGuidByMaterial.get(item.getMaterialNo());
                        } catch (Exception e) {
                            mStrMatSchemeList = new ArrayList<String>();
                        }
                        String mStrMatQPSScheme = "";
                        try {
                            mStrMatQPSScheme = Constants.MAPQPSSCHGuidByMaterial.get(item.getMaterialNo());
                        } catch (Exception e) {
                            mStrMatQPSScheme = "";
                        }
                        if (mStrMatQPSScheme == null) {
                            mStrMatQPSScheme = "";
                        }
                        if (mStrMatSchemeList == null) {
                            mStrMatSchemeList = new ArrayList<String>();
                        }
                        if (!mStrMatSchemeList.isEmpty() && !mStrMatQPSScheme.equalsIgnoreCase("")) {
                            openSchemeActivity(TextUtils.join(",", mStrMatSchemeList) + "," + mStrMatQPSScheme);
                        } else if (!mStrMatSchemeList.isEmpty() && mStrMatQPSScheme.equalsIgnoreCase("")) {
                            openSchemeActivity(TextUtils.join(",", mStrMatSchemeList));
                        } else if (mStrMatSchemeList.isEmpty() && !mStrMatQPSScheme.equalsIgnoreCase("")) {
                            openSchemeActivity(mStrMatQPSScheme);
                        }
                    }
                });
            }
        }

    }

    private void openSchemeActivity(String schemeGuid) {

    }
}*/
