/*
package com.arteriatech.ss.msec.bil.v4.ordertobill;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class OrderTextWatcher implements TextWatcher {

    private int position;
    private volatile List<SPStockItemBean> arrayList;
    private EditText editTextQty;
    private RecyclerView.ViewHolder viewHolder;
    private IUpdateViews views;

    public interface IUpdateViews{
        void updateView(int position, String text);
    }

    public OrderTextWatcher(List<SPStockItemBean> arrayList, IUpdateViews views) {
        this.arrayList = arrayList;
        this.views = views;
    }

    public void updatePosition(int position, EditText editTextQty, RecyclerView.ViewHolder viewHolder) {
        this.position = position;
        this.editTextQty = editTextQty;
        this.viewHolder = viewHolder;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (editTextQty != null && s == editTextQty.getEditableText()) {
            double gross = 0;
            try {
                arrayList.get(position).setQAQty(editTextQty.getText().toString());
                gross = returnGrossAmount(arrayList.get(position).getQAQty(), arrayList.get(position).getRate());
                arrayList.get(position).setGross(String.valueOf(gross));
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }
        views.updateView(1, "");
    }
    private double returnGrossAmount(String qty,String rate){
        double grossAmount =0;
        try {
            if (!TextUtils.isEmpty(qty)&&!TextUtils.isEmpty(rate)){
                if (arrayList.get(position).getSelectedUOM()==null||TextUtils.isEmpty(arrayList.get(position).getSelectedUOM())) {
                    grossAmount = Double.parseDouble(qty)*Double.parseDouble(rate);
                }else if(arrayList.get(position).getSelectedUOM()!=null&&!TextUtils.isEmpty(arrayList.get(position).getSelectedUOM())){
                    if (TextUtils.equals(arrayList.get(position).getSelectedUOM(),arrayList.get(position).getUOM())){
                        grossAmount = Double.parseDouble(qty)*Double.parseDouble(rate);
                    }else{
                        if (arrayList.get(position).getAlternativeUOM1Num()!=null&&!TextUtils.isEmpty(arrayList.get(position).getAlternativeUOM1Num())) {
                            grossAmount = Double.parseDouble(qty)*Double.parseDouble(rate)*Double.parseDouble(arrayList.get(position).getAlternativeUOM1Num());
                        }
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return grossAmount;
    }
}
*/
