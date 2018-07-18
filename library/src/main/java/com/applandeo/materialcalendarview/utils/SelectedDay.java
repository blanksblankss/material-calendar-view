package com.applandeo.materialcalendarview.utils;

import java.util.Calendar;

/**
 * This helper class represent a selected day when calendar is in a picker date mode.
 * It is used to remember a selected calendar cell.
 * <p>
 * Created by Mateusz Kornakiewicz on 23.05.2017.
 */

public class SelectedDay {

    private CalendarDay mCalendarDay;

    public SelectedDay(CalendarDay calendarDay) {
        mCalendarDay = calendarDay;
    }

    public SelectedDay(Calendar calendar) {
        mCalendarDay = new CalendarDay(calendar);
    }

    public CalendarDay getCalendarDay() {
        return mCalendarDay;
    }

    public void setCalendarDay(CalendarDay calendarDay) {
        mCalendarDay = calendarDay;
    }

    /**
     * @return Calendar instance representing selected cell date
     */
    public Calendar getCalendar() {
        return mCalendarDay.getCalendar();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SelectedDay) {
            return getCalendar().equals(((SelectedDay) obj).getCalendar());
        }

        return super.equals(obj);
    }
}

