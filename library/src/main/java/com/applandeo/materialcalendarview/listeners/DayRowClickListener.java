package com.applandeo.materialcalendarview.listeners;

import android.view.View;
import android.widget.AdapterView;

import com.annimon.stream.Stream;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.adapters.CalendarPageAdapter;
import com.applandeo.materialcalendarview.utils.CalendarDay;
import com.applandeo.materialcalendarview.utils.CalendarProperties;
import com.applandeo.materialcalendarview.utils.DateUtils;
import com.applandeo.materialcalendarview.utils.DayColorsUtils;

import java.util.Calendar;
import java.util.List;

/**
 * This class is responsible for handle click events
 * <p>
 * Created by Mateusz Kornakiewicz on 24.05.2017.
 */

public class DayRowClickListener implements AdapterView.OnItemClickListener {

    private CalendarPageAdapter mCalendarPageAdapter;

    private CalendarProperties mCalendarProperties;
    private int mPageMonth;

    public DayRowClickListener(CalendarPageAdapter calendarPageAdapter, CalendarProperties calendarProperties, int pageMonth) {
        mCalendarPageAdapter = calendarPageAdapter;
        mCalendarProperties = calendarProperties;
        mPageMonth = pageMonth < 0 ? 11 : pageMonth;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        CalendarDay calendarDay = (CalendarDay) adapterView.getItemAtPosition(position);

        if (mCalendarProperties.getOnDayClickListener() != null) {
            onClick(calendarDay);
        }

        switch (mCalendarProperties.getCalendarType()) {
//            case CalendarView.ONE_DAY_PICKER:
//                selectOneDay(calendarDay);
//                break;
//
            case CalendarView.MANY_DAYS_PICKER:
                selectManyDays(calendarDay);
                break;
//
            case CalendarView.RANGE_PICKER:
                selectRange(calendarDay);
                break;
//
//            case CalendarView.CLASSIC:
//                mCalendarPageAdapter.setSelectedDay(new SelectedDay(calendarDay));
        }
    }

    private void selectOneDay(CalendarDay calendarDay) {
//        CalendarDay previousSelectedDay = mCalendarPageAdapter.getSelectedDay().getCalendarDay();
//
//        if (isAnotherDaySelected(previousSelectedDay, calendarDay.getCalendar())) {
//            selectDay(calendarDay);
//            reverseUnselectedColor(previousSelectedDay);
//        }
    }

    private void selectManyDays(CalendarDay calendarDay) {
        Calendar calendar = calendarDay.getCalendar();
        if (!isCurrentMonthDay(calendar) || !isActiveDay(calendar)) {
            return;
        }

        // If list days with actions doesn't contain calendarDay
        if (!getCalendarDaysWithAction().contains(calendarDay)) {
            getCalendarDaysWithAction().add(calendarDay);
        }

        CalendarDay day = getExistingCalendarDay(calendarDay);

        if (day.isSelected()) {
            day.setSelected(false);
            DayColorsUtils.reverseUnselectedColor(day, mCalendarProperties);
        } else {
            day.setSelected(true);
            DayColorsUtils.setSelectedDayColors(day.getView(), mCalendarProperties);
        }

        mCalendarPageAdapter.informDatePicker();
    }

    private void selectRange(CalendarDay calendarDay) {
        if (!isCurrentMonthDay(calendarDay.getCalendar()) || !isActiveDay(calendarDay.getCalendar())) {
            return;
        }

        int selectedDays = Stream.of(getCalendarDaysWithAction()).filter(CalendarDay::isSelected).toList().size();

        if (selectedDays > 1) {
            clearAndSelectOne(calendarDay);
        }

        if (selectedDays == 1) {
            selectOneAndRange(calendarDay);
        }

        if (selectedDays < 1) {
            selectDay(calendarDay);
        }
    }

    private void clearAndSelectOne(CalendarDay calendarDay) {
        Stream.of(getCalendarDaysWithAction()).filter(CalendarDay::isSelected)
                .forEach(day -> {
                    day.setSelected(false);
                    DayColorsUtils.reverseUnselectedColor(day, mCalendarProperties);
                });

        selectDay(calendarDay);
    }

    private void selectOneAndRange(CalendarDay calendarDay) {
        CalendarDay previousSelectedDay = Stream.of(getCalendarDaysWithAction()).filter(CalendarDay::isSelected).single();

        System.out.println(getCalendarDaysWithAction().toString());

        Stream.of(DateUtils.getDatesRange(previousSelectedDay.getCalendar(), calendarDay.getCalendar()))
                .filter(calendar -> !mCalendarProperties.getDisabledDays().contains(calendar))
                .forEach(calendar -> {
                    CalendarDay newCalendarDay = new CalendarDay(calendar);
                    if (!getCalendarDaysWithAction().contains(newCalendarDay)) {
                        getCalendarDaysWithAction().add(newCalendarDay);
                    }

                    CalendarDay day = getExistingCalendarDay(newCalendarDay);
                    day.setSelected(true);
                });


        selectDay(calendarDay);
        mCalendarPageAdapter.informDatePicker();
        mCalendarPageAdapter.notifyDataSetChanged();
    }

    private List<CalendarDay> getCalendarDaysWithAction() {
        return mCalendarProperties.getCalendarDaysWithAction();
    }

    private CalendarDay getExistingCalendarDay(CalendarDay calendarDay) {
        return getCalendarDaysWithAction().get(getCalendarDaysWithAction().indexOf(calendarDay));
    }

    private void selectDay(CalendarDay calendarDay) {
        // If list days with actions doesn't contain calendarDay
        if (!getCalendarDaysWithAction().contains(calendarDay)) {
            getCalendarDaysWithAction().add(calendarDay);
        }

        calendarDay.setSelected(true);

        if (calendarDay.getView() != null) {
            DayColorsUtils.setSelectedDayColors(calendarDay.getView(), mCalendarProperties);
        }
    }

    private boolean isCurrentMonthDay(Calendar day) {
        return day.get(Calendar.MONTH) == mPageMonth && isBetweenMinAndMax(day);
    }

    private boolean isActiveDay(Calendar day) {
        return !mCalendarProperties.getDisabledDays().contains(day);
    }

    private boolean isBetweenMinAndMax(Calendar day) {
        return !((mCalendarProperties.getMinimumDate() != null && day.before(mCalendarProperties.getMinimumDate()))
                || (mCalendarProperties.getMaximumDate() != null && day.after(mCalendarProperties.getMaximumDate())));
    }

    private boolean isAnotherDaySelected(CalendarDay previousSelectedDay, Calendar calendar) {
        return previousSelectedDay != null && !calendar.equals(previousSelectedDay.getCalendar())
                && isCurrentMonthDay(calendar) && isActiveDay(calendar);
    }

    private void onClick(CalendarDay calendarDay) {
        if (mCalendarProperties.getEventDays() == null) {
            createEmptyEventDay(calendarDay);
            return;
        }

        Stream.of(mCalendarProperties.getEventDays())
                .filter(eventDate -> eventDate.getCalendar().equals(calendarDay.getCalendar()))
                .findFirst()
                .ifPresentOrElse(this::callOnClickListener, () -> createEmptyEventDay(calendarDay));
    }

    private void createEmptyEventDay(CalendarDay calendarDay) {
        EventDay eventDay = new EventDay(calendarDay.getCalendar());
        callOnClickListener(eventDay);
    }

    private void callOnClickListener(EventDay eventDay) {
        boolean enabledDay = mCalendarProperties.getDisabledDays().contains(eventDay.getCalendar())
                || !isBetweenMinAndMax(eventDay.getCalendar());

        eventDay.setEnabled(enabledDay);
        mCalendarProperties.getOnDayClickListener().onDayClick(eventDay);
    }
}
