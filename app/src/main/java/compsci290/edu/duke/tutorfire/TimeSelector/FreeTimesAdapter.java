package compsci290.edu.duke.tutorfire.TimeSelector;

import java.util.ArrayList;
import java.util.List;

import compsci290.edu.duke.tutorfire.DataClasses.Time;

/**
 * Created by harrisonlundberg on 4/27/17.
 */

public class FreeTimesAdapter {

    /** README !!! THE LIST OF TIMES PASSED IN MUST BE GROUPED BY DAY AND SORTED BY START TIME?
        Given a list of free times, each 1 hour long, coalesce adjacent ones and send to DB
     *
     */
    public static List<Time> combineFreeTimes(List<Time> dividedTimeList) {
        ArrayList<Time> timeList = new ArrayList<>();


        //First add first entry into timeList
        if (dividedTimeList.size() > 0) {
            timeList.add(dividedTimeList.get(0));
            //Now loop through rest of list, one by one trying to see if current Time will join with previous time
            for (int i = 1; i < dividedTimeList.size(); i++) {
                Time lastTime = timeList.get(timeList.size() - 1);
                Time nextTime = dividedTimeList.get(i);
                if (lastTime.getDay().equals(nextTime.getDay()) && lastTime.getEndHour() == nextTime.getStartHour()) {
                    //Join the two
                    Time newTime = new Time(lastTime.getDay(), lastTime.getStartHour(), nextTime.getEndHour());
                    timeList.set(timeList.size() - 1, newTime);
                } else {
                    timeList.add(nextTime);
                }
            }
        }
        return timeList;
    }

    /** Given a list of free times, of varying length, break up into one hour chunks
     *
     * @param timeList
     * @return
     */
    public static List<Time> divideFreeTimes(List<Time> timeList) {
        List<Time> dividedTimeList = new ArrayList<>();
        //Iterate over the list, for every Time if its length is more than one, call divide function
        for (Time currentTime : timeList) {
            if (currentTime.getEndHour() - currentTime.getStartHour() > 1) {
                //Create new entries
                for (int i = currentTime.getStartHour(); i < currentTime.getEndHour(); i++) {
                    dividedTimeList.add(new Time(currentTime.getDay(), i, i + 1));
                }
            } else {
                dividedTimeList.add(currentTime);
            }
        }

        return dividedTimeList;
    }

}
