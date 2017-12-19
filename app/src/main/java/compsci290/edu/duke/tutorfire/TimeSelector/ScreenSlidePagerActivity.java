package compsci290.edu.duke.tutorfire.TimeSelector;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import compsci290.edu.duke.tutorfire.DataClasses.Time;
import compsci290.edu.duke.tutorfire.R;
import compsci290.edu.duke.tutorfire.TutorModeActivities.TutorMainActivity;
import compsci290.edu.duke.tutorfire.WebServiceInterface.TutorServices;

/**
 * Main Activity for MyTimes
 */
public class ScreenSlidePagerActivity extends AppCompatActivity {

    protected static final int NUM_PAGES = 7;
    private static final String sTAG = "ScreenSlidePagerAct";
    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private Context mContext;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FloatingActionButton mFAB;
    public static WeeklySchedule mWeeklySchedule;
    public static List<Time> mTimeList;
    public static boolean changed = false;

    /**
     * Instantiates User
     * Creates layout, and either launches TutorServices or directly passes to the SSPAdapter
     * @param savedInstanceState
     */
    private MediaPlayer mSaveChangesPlayer;

    public static HashMap<Integer, ScreenSlidePageFragment> sspFrags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(sTAG, "onCreate");
        // basic layout commands
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        // Instantiate Context, User, and Changed = false
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mContext = getApplicationContext();
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        // if the WeeklySchedule was not recreated during this Instance of the application
        if(!WeeklySchedule.updated) {
            // call tutor services and correctly populate the WeeklySchedule
            TutorServices.getMyTimes(this, mFirebaseUser.getUid());
        } else {
            // if WeeklySchedule is up to date, just call the adapter without using student services
            mPager.setAdapter(mPagerAdapter);
        }
        // Make Toast for people to Swipe
        Toast.makeText(mContext, "Swipe Between Days", Toast.LENGTH_SHORT).show();
        // Instantiate FAB
        mFAB = (FloatingActionButton) findViewById(R.id.fab);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FAB click adds times to the database
                beginAddTimesToDB(mContext, mFirebaseUser, mFirebaseAuth);
                // returns user to the TutorMainActivity
                startActivity(new Intent(mContext, TutorMainActivity.class));
            }
        });

    }

    /**
     * During screen rotation, resets the Activity to Monday.
     * This was done because re-initializing the Fragment kept returning null, so new Activity helped
     * Preserves changes made to times before flipping
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(sTAG, "DESTROY ACTIVITY");
        if (mSaveChangesPlayer != null) {
            mSaveChangesPlayer.release();
            mSaveChangesPlayer = null;
        }
        startActivity(new Intent(this, ScreenSlidePagerActivity.class));
    }

    /**
     * Packages and sends List<Time> to database
     *
     * Called from ScreenSlidePageFragment
     *
     * @param c
     * @param fbAuth
     * @param fbUser
     */
    public void beginAddTimesToDB(Context c, FirebaseUser fbUser, FirebaseAuth fbAuth) {
        List<Time> toAdd = new ArrayList<>();
        List<Time> toDelete = new ArrayList<>();
        // iterate through mSelected Times Array
        for(int i = 0; i < WeeklySchedule.mSelectedTimesArray.length; i++) {
            for(int j = 0; j < WeeklySchedule.mSelectedTimesArray[0].length; j++) {
                // if the time at that location was selected
                if(WeeklySchedule.mSelectedTimesArray[i][j]) {
                    // add the corresponding time to it to be added
                    toAdd.add(WeeklySchedule.mTimeBlocks.get(i).get(j));
                }
            }
        }
        // process the List of adjacent blocks into contiguous
        toAdd = FreeTimesAdapter.combineFreeTimes(toAdd);
        // pass the lists to TutorServices
        TutorServices.addToMyTimes(c, fbUser.getUid(), toAdd);
        // TutorServices.removeFromMyTimes(c, fbUser.getUid(), toDelete);

        Toast.makeText(c, "Available Times Updated", Toast.LENGTH_SHORT).show();

        // Give toast and sound after sending times to DB
        Toast.makeText(c, "Available Times Updated", Toast.LENGTH_SHORT).show();
        // reset changed to false because database was updated
        changed = false;

        mSaveChangesPlayer = MediaPlayer.create(c, R.raw.poke9);
        mSaveChangesPlayer.start();
    }


    /**
     * populates list of times to the listView by calling adapter on mPagerAdapter
     * Data is stored in WeeklySchedule static variable
     * @param timeList - list of Time objects, passed from TutorServices
     */
    public void setTimesList(List<Time> timeList) {
        Log.d(sTAG, "setTimesList called from UserServices");
        mTimeList = FreeTimesAdapter.divideFreeTimes(timeList);
        // create new WeeklySchedule given the timeList
            // static therefore all instances are the same
        mWeeklySchedule = new WeeklySchedule(mTimeList);
        // sets updated field true since we pulled from online
        WeeklySchedule.updated = true;
        // set adapter, which calls on WeeklySchedule
        mPager.setAdapter(mPagerAdapter);
    }

    /**
     * onBackPressed overrode to either give dialog or go back to MainActivity
     * Prevents loop of Fragments being retraced
     */
    @Override
    public void onBackPressed() {
        // check if changes were made (add boolean)
        if(changed) {
            saveChangesDialog(mContext);
        } else {
            //  no changes, so use back as normal
            Intent i = new Intent(mContext, TutorMainActivity.class);
            mContext.startActivity(i);
        }
    }

    /**
     * Create dialog as user tries to exit the times list
     * @param context
     */
    private void saveChangesDialog(final Context context) {
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme))
                .setTitle("Return to Main")
                .setMessage("Save changes to your times?")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
                        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                        // add courses (same as if was hitting save button)
                        beginAddTimesToDB(context, mFirebaseUser, mFirebaseAuth);
                        Log.d("RV", "Deleted a course");
                        // New Intent to Main Activity
                        Intent i = new Intent(context, TutorMainActivity.class);
                        changed = false;
                        context.startActivity(i);
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Intent back to MainActivity
                        Intent i = new Intent(context, TutorMainActivity.class);
                        changed = false;
                        context.startActivity(i);
                    }
                }).setCancelable(true)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }



}
