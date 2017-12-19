package compsci290.edu.duke.tutorfire.TimeSelector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import compsci290.edu.duke.tutorfire.DataClasses.Time;
import compsci290.edu.duke.tutorfire.R;

/**
 * Class representing a Fragment for Tutor's available times during each day.
 * Fragment == day of the week
 */
public class ScreenSlidePageFragment extends Fragment {

    public String mDayOfWeek;
    public static final String day = "dayofweek";
    private static final String sTAG = "ScreenSlidePageFrag";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    public ArrayList<Time> mListOfTimesOnThisDay;


    /**
     * OnCreateView
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return ViewGroup containing the new inflated View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(sTAG, "Fragment View Created");
        // instantiate user
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        // in case this was called incorrectly, get the times on this day from WeeklySchedule static
        if (mListOfTimesOnThisDay == null) {
            mListOfTimesOnThisDay = WeeklySchedule.mTimeBlocks.get(0);
        }
        // get handle to fragment_screen_slide_page ViewGroup
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        // handle to textView of the viewGroup (day of week)
        ((TextView) rootView.findViewById(R.id.pager_day_of_week)).setText(mDayOfWeek);
        // Populate ListView with list of times for the day and int index of
        ((ListView) rootView.findViewById(R.id.times_fragment_list_view)).setAdapter(new TimesListAdapter(getContext(), mListOfTimesOnThisDay));
        return rootView;
    }
}


