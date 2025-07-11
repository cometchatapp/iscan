package com.arteriatech.ss.msec.iscan.v4.ui;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.List;

/**
 * Created by e10860 on 11/17/2017.
 */

public class AxisValueFormatter extends ValueFormatter implements IAxisValueFormatter {

    private List<String> labels;

    public AxisValueFormatter(List<String> labels) {
        this.labels = labels;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        try {
            if (value == Math.round(value)) {
                int index = (int) Math.floor(value);
                return labels.get(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
