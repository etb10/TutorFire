package compsci290.edu.duke.tutorfire.TimeSelector;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import compsci290.edu.duke.tutorfire.DataClasses.Time;

/**
 * Created by Edwin on 4/27/17.
 */

public class WeeklySchedule {

    public static final int startHour = 8;
    public static final int endHour = 24;

    /**
     * declares and instantiates days of week and all time blocks that make a week
     */
    public static final String[] daysOfWeek;
    public static final String[] daysOfWeekDisplay;
    public static final ArrayList<ArrayList<Time>> mTimeBlocks = new ArrayList<>();
    public static boolean updated = false;
    static {
        // define days of week
        daysOfWeekDisplay = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        daysOfWeek = new String[]{"Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"};
        // define hours in the day for each day
        for(int j = 0; j < daysOfWeek.length; j++) {
            ArrayList<Time> timesInDay = new ArrayList<>();
            for (int i = startHour; i < endHour; i++) {
                // add a time with j day of the week, i start time, i+1 finish time
                timesInDay.add(new Time(daysOfWeek[j], i, i+1));
            }
            mTimeBlocks.add(timesInDay);
        }
    }
    // Array for whether or not items are selected
    public static boolean[][] mSelectedTimesArray = new boolean[daysOfWeek.length][endHour-startHour];

    /**
     * Basic constructor for WeeklySchedule Object
     *
     * @param timeList - these times are ones that have been selected and downloaded from the web server
     */
    public WeeklySchedule(List<Time> timeList) {
        // use the List of Times to create the weekly schedule
        populateSelectedTimesArrayList(timeList);

    }

    /**
     * Takes the selected times List passed and populates the weekly schedule
     * @param timeList - list of Times that have been already selected by the user
     */
    private void populateSelectedTimesArrayList(List<Time> timeList) {
        // iterate through entire list of TimeObjects
        for(Time thisTime : timeList) {
            // get day of week (first index of mSelectedTimesArray)
            int dayOfWeek = getIntDayOfWeek(thisTime.getDay());
            // get start hour
            int startHour = thisTime.getStartHour();
            // populate mSelectedTimesArray
            mSelectedTimesArray[dayOfWeek][startHour - WeeklySchedule.startHour] = true;
        }
        Log.d("WeeklySchedule","Populated the selected times array with this many entries: " + timeList.size());
    }

    /**
     * Returns the integer representation of the day of the week given a string day of the week
     * @param dayOfWeek - String day of the week
     * @return - int rep of day of the week
     */
    public int getIntDayOfWeek(String dayOfWeek) {
        for(int i = 0; i < daysOfWeek.length; i++) {
            // if the dayOfWeek equals corresponding entry, then return i
            if(dayOfWeek.equals(daysOfWeek[i])) {
                return i;
            }
        }
        return 0;
    }
}



