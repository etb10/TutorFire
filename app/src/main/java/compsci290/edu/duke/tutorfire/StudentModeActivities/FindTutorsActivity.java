package compsci290.edu.duke.tutorfire.StudentModeActivities;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import compsci290.edu.duke.tutorfire.CoursesDatabase.CourseDBInterface;
import compsci290.edu.duke.tutorfire.R;

import compsci290.edu.duke.tutorfire.SharedActivities.GeneralActivity;

import compsci290.edu.duke.tutorfire.TimeSelector.WeeklySchedule;

/**
 * A set of spinner to choose a subject, course number, day, start time and duration for finding tutors
 */

public class FindTutorsActivity extends GeneralActivity implements AdapterView.OnItemSelectedListener {

    private String selected_subject;
    private String selected_course;
    private String sel_display_day;
    private String selected_day;
    private String selected_start_time;
    private String selected_duration;

    private Spinner[] spinners;

    private LinearLayout[] linearLayouts;

    private FloatingActionButton search_button;

    private Resources res;
    private final static String TAG = "FindTutorsActivity";
    private final static String NONE = "None";
    private final static String SPINNERS_BUNDLE_KEY = "spinnys";
    private final static int NUM_SPINNERS = 5;
    private final static Map<String, String> dotwMap;
    static{
        dotwMap = new HashMap<>();
        for(int i = 0; i < WeeklySchedule.daysOfWeek.length; i++){
            dotwMap.put(WeeklySchedule.daysOfWeekDisplay[i], WeeklySchedule.daysOfWeek[i]);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_tutors);

        res = getResources();

        //place linear layouts for spinners in array
        linearLayouts = new LinearLayout[NUM_SPINNERS];
        linearLayouts[0] = (LinearLayout) findViewById(R.id.subjectLayout);
        linearLayouts[1] = (LinearLayout) findViewById(R.id.courseNumberLayout);
        linearLayouts[2] = (LinearLayout) findViewById(R.id.dayLayout);
        linearLayouts[3] = (LinearLayout) findViewById(R.id.startTimeLayout);
        linearLayouts[4] = (LinearLayout) findViewById(R.id.durationLayout);

        search_button = (FloatingActionButton) findViewById(R.id.search_button);
        search_button.setVisibility(View.GONE);

        //initialize spinners array
        spinners = new Spinner[NUM_SPINNERS];
        spinners[0] = (Spinner) findViewById(R.id.subject_spinner);
        spinners[1] = (Spinner) findViewById(R.id.course_spinner);
        spinners[2] = (Spinner) findViewById(R.id.day_spinner);
        spinners[3] = (Spinner) findViewById(R.id.start_time_spinner);
        spinners[4] = (Spinner) findViewById(R.id.duration_spinner);

        //get old spinner state after re-orientation
        if (savedInstanceState != null){
            Parcelable[] bundled_spinners = savedInstanceState.getParcelableArray(SPINNERS_BUNDLE_KEY);
            for (int i = 0; i < spinners.length; i++){
                spinners[i].onRestoreInstanceState(bundled_spinners[i]);

            }
        } else{
            for (int i = 1; i < linearLayouts.length; i++){
                linearLayouts[i].setVisibility(View.GONE);
            }
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        // Specify the layout to use when the list of choices appears
        ArrayAdapter<CharSequence> subject_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        subject_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        List<String> subject_list = CourseDBInterface.getSubjectList(this);
        subject_list.add(0, "None");
        subject_adapter.addAll(subject_list);
        // Apply the adapter to the spinner
        spinners[0].setAdapter(subject_adapter);
        spinners[0].setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> day_adapter = ArrayAdapter.createFromResource(this,
                R.array.day_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        day_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinners[2].setAdapter(day_adapter);
        spinners[2].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sel_display_day = (String) parent.getItemAtPosition(position);
                selected_day = dotwMap.get(sel_display_day);
                if (!sel_display_day.equals(NONE)) {
                    linearLayouts[3].setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //nothing happens
            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> start_time_adapter = ArrayAdapter.createFromResource(this,
                R.array.start_times_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        start_time_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinners[3].setAdapter(start_time_adapter);
        spinners[3].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_start_time = (String) parent.getItemAtPosition(position);
                if (!selected_start_time.equals(NONE)) {
                    linearLayouts[4].setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //nothing happens
            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> duration_adapter = ArrayAdapter.createFromResource(this,
                R.array.durations_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        duration_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinners[4].setAdapter(duration_adapter);
        spinners[4].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_duration = (String) parent.getItemAtPosition(position);
                if (!selected_duration.equals(NONE)) {
                    search_button.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //nothing happens
            }
        });

    }

    /**
     * Send search arguments to the FoundTutorsActivity to get matching tutors
     * @param v View that was pressed
     */
    public void beginFoundTutorsActivity(View v) {
        Intent intent = new Intent(this, FoundTutorsActivity.class);
        String[] SearchQuery = new String[] {selected_subject, selected_course,
                selected_day, selected_start_time, selected_duration};
        Log.d(TAG, String.format("Query passed to FoundTutorsActivity: %s", Arrays.toString(SearchQuery)));
        intent.putExtra(res.getString(R.string.TutorSearchIntentKey), SearchQuery);
        startActivity(intent);
    }

    /**
     * Save spinner state to Bundle
     * @param outState Bundle to save
     */
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        Parcelable[] bundled_spinners = new Parcelable[NUM_SPINNERS];
        for (int i = 0; i < spinners.length; i++){
            bundled_spinners[i] = spinners[i].onSaveInstanceState();
        }
        //outState.putIntArray(SPINNERS_BUNDLE_KEY, spinner_pos_array);
        outState.putParcelableArray(SPINNERS_BUNDLE_KEY, bundled_spinners);
    }

    /**
     * Listener interface for the subject spinner
     * @param parent Spinner
     * @param view Text views inside spinner
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected_subject = (String) parent.getItemAtPosition(position);
        if (!selected_subject.equals(NONE)) {
            linearLayouts[1].setVisibility(View.VISIBLE);
        }

        //Now set course number spinner to active and fill it with appropriate course numbers

        Log.d(TAG, String.format("subject spinner view: %s", view));

        List<String> courseNumbersList = CourseDBInterface.getCourseNumbersList(this, selected_subject);
        courseNumbersList.add(0, "None");
        ArrayAdapter<CharSequence> course_adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        course_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        course_adapter.addAll(courseNumbersList);
        spinners[1].setAdapter(course_adapter);



        spinners[1].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_course = (String) parent.getItemAtPosition(position);
                if (!selected_course.equals(NONE)) {
                    linearLayouts[2].setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * nada
     * @param parent
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Nothing happens
    }
}
