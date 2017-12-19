package compsci290.edu.duke.tutorfire.TimeSelector;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.List;

import compsci290.edu.duke.tutorfire.DataClasses.Time;
import compsci290.edu.duke.tutorfire.R;

/**
 * Created by Edwin on 4/27/17.
 *
 * Adapts list of time into list view on a ScreenSlidePageFragment
 */

public class TimesListAdapter extends BaseAdapter {

    private Context mContext;
    private Time[] mTimesArr;
    private int mDayIndex;

    private static final String sTAG = "TimesListAdapter";

    /**
     * Constructor call
     * @param c
     * @param timeList
     */
    public TimesListAdapter(Context c, List<Time> timeList){
        Log.d(sTAG, "TimesListAdapter Constructor call");
        mContext = c;
        // convert List to Array
        mTimesArr = timeList.toArray(new Time[timeList.size()]);
        // Day of the week index is from the first item
        mDayIndex = timeList.get(0).getIntDayOfWeek();
    }


    @Override
    public int getCount() {
        return mTimesArr.length;
    }

    @Override
    public Object getItem(int position) {
        return mTimesArr[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Sets button text to startHour:00 - EndHour:00
     * Sets OnClickListener so times are updated and colored when clicked
     * @param position
     * @param convertView
     * @param parent
     * @return - Button as View
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // set Button View based
        final Button button;
        Time time = mTimesArr[position];
        if (convertView == null) {
            button = new Button(mContext);
        } else {
            button = (Button) convertView;
        }
        // populate button with values
        button.setText(time.getStartHour() + ":00 - " + time.getEndHour() + ":00");
        // if the WeeklySchedule has boolean true at the specific location, mark with opposite color
        colorButtons(position, button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle a click to the button: invert the WeeklySchedule entry
                WeeklySchedule.mSelectedTimesArray[mDayIndex][position] = !WeeklySchedule.mSelectedTimesArray[mDayIndex][position];
                // WORKING WITH UPDATED VALUES
                colorButtons(position, button);
                // update "changed" in the ScreenSlidePagerActivity
                ScreenSlidePagerActivity.changed = true;
            }
        });

        return button;
    }

    /**
     * Colors the buttons accordingly when they are clicked
     * WeeklySchedule called to determine if Time is selected or not
     * @param position
     * @param button
     */
    private void colorButtons(int position, Button button) {
        // if the currently selected block should be highlighted on
        if (WeeklySchedule.mSelectedTimesArray[mDayIndex][position]) {
            button.setBackgroundColor(ContextCompat.getColor(mContext, R.color.accent_light));
        } else { // otherwise
            button.setBackgroundColor(ContextCompat.getColor(mContext, R.color.primary_material_dark));
        }
    }

}
