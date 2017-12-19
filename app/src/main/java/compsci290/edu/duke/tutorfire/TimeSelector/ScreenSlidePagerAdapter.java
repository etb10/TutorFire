package compsci290.edu.duke.tutorfire.TimeSelector;

/**
 * Created by Edwin on 4/27/17.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Page Adapter that represents all days of the week.
 * Passes information on to the ScreenSlidePageFragement, which represents a specific day
 */
public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    private static final String sTAG = "ScreenSlidePagerAdap";

    /**
     * Default Constructor
     * @param fm
     */
    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
        Log.d(sTAG, "ScreenSlidePagerAdapter Constructor with FragmentManager " + fm.toString());
    }


    /**
     * gets a Fragment item to be displayed on the page
     * @param position - 0-6: respective day of the week
     * @return - fragment item with day and daily schedule
     */
    @Override
    public Fragment getItem(int position) {
        Log.d(sTAG, "get Fragment position: " + position);
        // create bundle to pass position to ScreenSlidePageFragment
        Bundle b = new Bundle();
        b.putInt(ScreenSlidePageFragment.day, position);
        // return fragment with position as bundle
        ScreenSlidePageFragment ssf = new ScreenSlidePageFragment();
        ssf.setArguments(b);
        // populates the hours in the day onto the screen by making the Fragment list of times on this day
        ssf.mListOfTimesOnThisDay = WeeklySchedule.mTimeBlocks.get(position);
        // populate day of the week (Display)
        ssf.mDayOfWeek = WeeklySchedule.daysOfWeekDisplay[position];
        return ssf;
    }

    /**
     * getCount
     * @return Number of pages in ScreenSlidePagerActivity
     */
    @Override
    public int getCount() {
        // Log.d(sTAG, "getCount");
        return ScreenSlidePagerActivity.NUM_PAGES;
    }
}