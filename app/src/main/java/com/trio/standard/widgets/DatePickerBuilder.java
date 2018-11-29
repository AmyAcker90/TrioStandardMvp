package com.trio.standard.widgets;

import android.app.DatePickerDialog;
import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lixia on 2018/9/19.
 */

public class DatePickerBuilder {
    private onDateSetListener mOnDateSetListener;

    private Context mContext;
    private String dataStr = null;
    private String pattern="yyyy-MM-dd";

    public DatePickerBuilder(Context context) {
        this.mContext = context;
    }

    public DatePickerBuilder onDateSetListener(onDateSetListener onDateSetListener) {
        this.mOnDateSetListener = onDateSetListener;
        return this;
    }

    public DatePickerBuilder dateString(String dateStr) {
        this.dataStr = dateStr;
        return this;
    }

    public DatePickerBuilder pattern(String pattern) {
        this.pattern = pattern;
        return this;
    }

    public DatePickerDialog build() {
        Calendar calendar = Calendar.getInstance();
        if (dataStr != null && pattern != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                calendar.setTime(sdf.parse(dataStr));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        DatePickerDialog dialog = new DatePickerDialog(mContext,
                (view, year, month, dayOfMonth) -> {
                    String dateStr = year +
                            "-" + String.format("%02d", (month + 1)) +
                            "-" + String.format("%02d", dayOfMonth);
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//默认
                        Date date = sdf.parse(dateStr);
                        if (mOnDateSetListener != null) {
                            if (pattern != null) {
                                SimpleDateFormat formatter = new SimpleDateFormat(pattern);
                                dateStr = formatter.format(date);
                            }
                            mOnDateSetListener.onDateSet(dateStr);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH));
        return dialog;
    }

    public interface onDateSetListener {
        void onDateSet(String dateStr);
    }

}
