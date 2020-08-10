package com.mindology.app.viewmodels;

import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.RequestManager;
import com.google.android.material.button.MaterialButton;
import com.mindology.app.R;
import com.mindology.app.util.Constants;
import com.mindology.app.util.Enums;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

public class YasBindingAdapters {

    private static RequestManager requestManager;

    @Inject
    public YasBindingAdapters(RequestManager requestManager) {
        this.requestManager = requestManager;
    }
//


//    @BindingAdapter(value = "toggleCheckedButton")
//    public static void toggleCheckedButton(SegmentedGroup view, String value) {
//        try {
//            value = value.toUpperCase();
//            if (value.equals("FULLY_FURNISHED")) {
//                view.check(R.id.tgl_full);
//            } else if (value.toUpperCase().equals("UNFURNISHED")) {
//                view.check(R.id.tgl_none);
//            } else {
//                view.check(R.id.tgl_part);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @BindingAdapter(value = "hasOrNot")
    public static void propertyType(TextView view, boolean value) {
        view.setText(value ? "Yes" : "");
    }

    @BindingAdapter(value = "propertyType")
    public static void propertyType(TextView view, String value) {
        if (TextUtils.isEmpty(value)) {
            view.setText("");
        } else {
            view.setText(Enums.PropertyTypes.getNameByType(Enums.PropertyTypes.getTypeByValue(value)));
        }
    }

    @BindingAdapter(value = "furnishing")
    public static void furnishing(TextView view, String value) {
        if (TextUtils.isEmpty(value)) {
            view.setText("");
        } else {
            view.setText(Enums.Furnishing.getNameByType(Enums.Furnishing.getTypeByKey(value)));
        }
    }

//    @BindingAdapter(value = "inspectionType")
//    public static void inspectionType(MultiStateSwitch view, Enums.InspectionTypes value) {
//        if (value != null)
//            switch (value) {
//                case CHECK_OUT:
//                    view.selectState(2);
//                    break;
//                case MID_TERM:
//                    view.selectState(1);
//                    break;
//                case CHECK_IN:
//                    view.selectState(0);
//                    break;
//            }
//    }

    @BindingAdapter(value = "buttonVisibility")
    public static void buttonVisibility(View view, boolean value) {
        if (value) {
//            view.setBackgroundTintList(ColorStateList.valueOf(view.getResources().getColor(R.color.YIGreen)));
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
//            view.setBackgroundTintList(ColorStateList.valueOf(view.getResources().getColor(R.color.turquoise)));
//            view.setEnabled(false);
        }
    }

    @BindingAdapter(value = {"expireDate", "prefixText"}, requireAll = false)
    public static void expireDate(TextView textView, long dateInt, String prefixText) {

        Date date = new Date(dateInt);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
        String dateText = df2.format(date);

        textView.setText(prefixText + " " + dateText);
    }

}
