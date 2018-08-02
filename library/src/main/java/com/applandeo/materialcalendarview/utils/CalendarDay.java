package com.applandeo.materialcalendarview.utils;

import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Mateusz Kornakiewicz on 18.07.2018.
 */

public class CalendarDay {

    private Calendar mCalendar;
    private TextView mView;
    private boolean mIsSelected;

    public CalendarDay(Calendar calendar) {
        mCalendar = (Calendar) calendar.clone();
    }

    /**
     * @return Calendar instance representing selected cell date
     */
    public Calendar getCalendar() {
        return mCalendar;
    }

    public void setView(TextView view) {
        mView = view;
    }

    /**
     * @return View representing selected calendar cell
     */
    public TextView getView() {
        return mView;
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public void setSelected(boolean selected) {
        mIsSelected = selected;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CalendarDay) {
            return getCalendar().equals(((CalendarDay) obj).getCalendar());
        }

        return super.equals(obj);
    }
}
