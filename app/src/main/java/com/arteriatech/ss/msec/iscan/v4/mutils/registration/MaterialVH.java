package com.arteriatech.ss.msec.iscan.v4.mutils.registration;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.ui.MaterialDesignSpinner;

public class MaterialVH extends RecyclerView.ViewHolder {
    AppCompatTextView txtMaterial,txtMrp,txtUom;
    EditText txtMaterialQty;
    MaterialDesignSpinner spinner;
    SOCreateQtyTextWatcher soCreateQtyTextWatcher;
    ImageView iv_img;
    public MaterialVH(View viewItem, SOCreateQtyTextWatcher soCreateQtyTextWatcher) {
        super(viewItem);
        txtMaterial=viewItem.findViewById(com.arteriatech.ss.msec.iscan.v4.R.id.txtMaterial);
        txtMaterialQty=viewItem.findViewById(com.arteriatech.ss.msec.iscan.v4.R.id.txtMaterialQty);
        txtUom=viewItem.findViewById(com.arteriatech.ss.msec.iscan.v4.R.id.txtUom);
        spinner=viewItem.findViewById(com.arteriatech.ss.msec.iscan.v4.R.id.spinner);
        txtMrp=viewItem.findViewById(com.arteriatech.ss.msec.iscan.v4.R.id.txtMrp);
        iv_img=viewItem.findViewById(com.arteriatech.ss.msec.iscan.v4.R.id.iv_img);
        this.soCreateQtyTextWatcher = soCreateQtyTextWatcher;
        txtMaterialQty.addTextChangedListener(soCreateQtyTextWatcher);
        setIsRecyclable(false);
    }
}
