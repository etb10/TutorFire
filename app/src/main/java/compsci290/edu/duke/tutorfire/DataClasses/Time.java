package compsci290.edu.duke.tutorfire.DataClasses;

import compsci290.edu.duke.tutorfire.TimeSelector.WeeklySchedule;

/**
 * Created by harrisonlundberg on 4/19/17.
 *
 * This class encapsulates a Time period during which a Tutor is free. It includes the day,
 * starting time, and ending time.
 */

public class Time {
    private String mDay;
    private int mStartHour;
    private int mEndHour;

    public Time(String day, int start, int end) {
        mDay = day;
        mStartHour = start;
        mEndHour = end;
    }


    /**
     *
     * @return The day of this Time period
     */
    public String getDay() {
        return mDay;
    }

    /**
     *
     * @return The hour when this Time period starts
     */
    public int getStartHour() {
        return mStartHour;
    }

    /**
     *
     * @return The hour when this Time period ends
     */
    public int getEndHour() {
        return mEndHour;
    }

    /**
     * This method is called when we need to represent the day as an integer.
     *
     * @return The integer representation of the day of this time period
     */
    public int getIntDayOfWeek() {
        for(int i = 0; i < WeeklySchedule.daysOfWeek.length; i++) {
            // if the dayOfWeek equals corresponding entry, then return i
            if(mDay.equals(WeeklySchedule.daysOfWeek[i])) {
                return i;
            }
        }
        return 0;
    }

    /**
     * This method is used when we need to compare two Time periods for equality.
     *
     * @param o The Object to which this Time period is compared
     * @return Whether or not the two objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (o instanceof Time) {
            Time castO = (Time) o;
            return mDay.equals(castO.mDay) && mStartHour == castO.mStartHour && mEndHour == castO.mEndHour;
        } else {
            return false;
        }
    }
}
