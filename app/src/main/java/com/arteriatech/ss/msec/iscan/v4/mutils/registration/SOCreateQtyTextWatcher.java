package com.arteriatech.ss.msec.iscan.v4.mutils.registration;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import androidx.recyclerview.widget.RecyclerView;



/**
 * Created by e10769 on 20-12-2017.
 */

public class SOCreateQtyTextWatcher implements TextWatcher {
    private MaterialBean materialBean = null;
    private RecyclerView.ViewHolder holder = null;
    private SOCreateQtyTextWatcherInterface createQtyTextWatcherInterface = null;
    int mPosition=0;
    String previousTxt="";
    public SOCreateQtyTextWatcher() {

    }

    public void updateTextWatcher(MaterialBean soItemBean, RecyclerView.ViewHolder holder,int position, SOCreateQtyTextWatcherInterface createQtyTextWatcherInterface) {
        this.materialBean = soItemBean;
        this.holder = holder;
        this.createQtyTextWatcherInterface = createQtyTextWatcherInterface;
        this.mPosition=position;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (materialBean != null) {
            if (!s.toString().equalsIgnoreCase(materialBean.getQty())) {
                if (materialBean != null) {
                    materialBean.setQty(s.toString());
                }
                if (materialBean != null) {
                    if (!TextUtils.isEmpty(materialBean.getQty())) {
                        if (materialBean.getUom().equalsIgnoreCase("NOS")) {
                            double mrpAmount = Double.parseDouble(materialBean.getQty()) * Double.parseDouble(materialBean.getStkConvQty1()) * Double.parseDouble(materialBean.getMRP());
                            materialBean.setMRP(mrpAmount + "");
                        } else {
                            double mrpAmount = Double.parseDouble(materialBean.getQty()) * Double.parseDouble(materialBean.getMRP());
                            materialBean.setMRP(mrpAmount + "");
                        }
                        if (createQtyTextWatcherInterface != null) {
                            createQtyTextWatcherInterface.onTextChange(s.toString(), materialBean, holder,mPosition);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
       /* if (!TextUtils.isEmpty(s.toString())) {
            String text = Double.toString(Math.abs(Double.parseDouble(s.toString())));
            int integerPlaces = text.indexOf('.');
            int decimalPlaces = text.length() - integerPlaces - 1;
            int charLength = integerPlaces + 1 + decimalPlaces;
            if (charLength<=13 && decimalPlaces >=3) {
                if (holder instanceof SOMultiMaterialVH) {
                    ((SOMultiMaterialVH) holder).etQty.setFilters(new InputFilter[]{new InputFilter.LengthFilter(charLength)});
                }
            }
        }*/
    }

}
