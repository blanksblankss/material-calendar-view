package com.applandeo.materialcalendarview.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.R;
import com.applandeo.materialcalendarview.utils.CalendarDay;
import com.applandeo.materialcalendarview.utils.CalendarProperties;
import com.applandeo.materialcalendarview.utils.DateUtils;
import com.applandeo.materialcalendarview.utils.DayColorsUtils;
import com.applandeo.materialcalendarview.utils.ImageUtils;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * This class is responsible for loading a one day cell.
 * <p>
 * Created by Mateusz Kornakiewicz on 24.05.2017.
 */

class CalendarDayAdapter extends ArrayAdapter<CalendarDay> {
    private CalendarPageAdapter mCalendarPageAdapter;
    private LayoutInflater mLayoutInflater;
    private int mPageMonth;
    private Calendar mToday = DateUtils.getCalendar();

    private CalendarProperties mCalendarProperties;

    CalendarDayAdapter(CalendarPageAdapter calendarPageAdapter, Context context, CalendarProperties calendarProperties,
                       ArrayList<CalendarDay> calendarDays, int pageMonth) {
        super(context, calendarProperties.getItemLayoutResource(), calendarDays);
        mCalendarPageAdapter = calendarPageAdapter;
        mCalendarProperties = calendarProperties;
        mPageMonth = pageMonth < 0 ? 11 : pageMonth;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = mLayoutInflater.inflate(mCalendarProperties.getItemLayoutResource(), parent, false);
        }

        CalendarDay calendarDay = getItem(position);

        TextView dayLabel = (TextView) view.findViewById(R.id.dayLabel);
        calendarDay.setView(dayLabel);

        ImageView dayIcon = (ImageView) view.findViewById(R.id.dayIcon);

        Calendar day = calendarDay.getCalendar();

        // Loading an image of the event
        if (dayIcon != null) {
            loadIcon(dayIcon, day);
        }

        setLabelColors(calendarDay);

        dayLabel.setText(String.valueOf(day.get(Calendar.DAY_OF_MONTH)));
        return view;
    }

    private void setLabelColors(CalendarDay calendarDay) {
        // Setting not current month day color
        if (!isCurrentMonthDay(calendarDay.getCalendar())) {
            DayColorsUtils.setDayColors(calendarDay.getView(), mCalendarProperties.getAnotherMonthsDaysLabelsColor(),
                    Typeface.NORMAL, R.drawable.background_transparent);
            return;
        }

        // Update view for all selected CalendarDays
        if (isSelectedDay(calendarDay)) {
            updateViewVariable(calendarDay);
            DayColorsUtils.setSelectedDayColors(calendarDay.getView(), mCalendarProperties);
            return;
        }

        // Setting disabled days color
        if (!isActiveDay(calendarDay.getCalendar())) {
            DayColorsUtils.setDayColors(calendarDay.getView(), mCalendarProperties.getDisabledDaysLabelsColor(),
                    Typeface.NORMAL, R.drawable.background_transparent);
            return;
        }

        // Setting current month day color
        DayColorsUtils.setCurrentMonthDayColors(calendarDay.getCalendar(), mToday, calendarDay.getView(), mCalendarProperties);
    }

    private boolean isSelectedDay(CalendarDay calendarDay) {
        return mCalendarProperties.getCalendarType() != CalendarView.CLASSIC
                && calendarDay.getCalendar().get(Calendar.MONTH) == mPageMonth
                && mCalendarProperties.getCalendarDaysWithAction().contains(calendarDay);
    }

    /**
     * This methods updates View variable in CalendarDay with new one after view reloading
     */
    private void updateViewVariable(CalendarDay calendarDay) {
        Stream.of(mCalendarProperties.getCalendarDaysWithAction())
                .filter(day -> day.equals(calendarDay))
                .findFirst().ifPresent(selectedDay -> selectedDay.setView(calendarDay.getView()));
    }

    private boolean isCurrentMonthDay(Calendar day) {
        return day.get(Calendar.MONTH) == mPageMonth &&
                !((mCalendarProperties.getMinimumDate() != null && day.before(mCalendarProperties.getMinimumDate()))
                        || (mCalendarProperties.getMaximumDate() != null && day.after(mCalendarProperties.getMaximumDate())));
    }

    private boolean isActiveDay(Calendar day) {
        return !mCalendarProperties.getDisabledDays().contains(day);
    }

    private void loadIcon(ImageView dayIcon, Calendar day) {
        if (mCalendarProperties.getEventDays() == null || !mCalendarProperties.getEventsEnabled()) {
            dayIcon.setVisibility(View.GONE);
            return;
        }

        Stream.of(mCalendarProperties.getEventDays())
                .filter(eventDate -> eventDate.getCalendar().equals(day))
                .findFirst().executeIfPresent(eventDay -> {

            ImageUtils.loadResource(dayIcon, eventDay.getImageResource());

            // If a day doesn't belong to current month then image is transparent
            if (!isCurrentMonthDay(day) || !isActiveDay(day)) {
                dayIcon.setAlpha(0.12f);
            }
        });
    }
}
