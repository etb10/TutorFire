package compsci290.edu.duke.tutorfire.TutorModeActivities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import compsci290.edu.duke.tutorfire.CoursesDatabase.CourseDBInterface;
import compsci290.edu.duke.tutorfire.DataClasses.Course;
import compsci290.edu.duke.tutorfire.R;
import compsci290.edu.duke.tutorfire.SharedActivities.GeneralActivity;
import compsci290.edu.duke.tutorfire.WebServiceInterface.TutorServices;

public class AddCourseActivity extends GeneralActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private String selected_subject;
    private String selected_course;

    private FloatingActionButton finish_button;

    private static final String subjectTAG = "selected_subject_spinner";
    private static final String courseTAG = "selected_course_spinner";

    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        Spinner subject_spinner = (Spinner) findViewById(R.id.courses_subject_spinner);

        List<String> subjectList = CourseDBInterface.getSubjectList(this);
        ArrayAdapter<CharSequence> subject_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        subject_adapter.addAll(subjectList);
        subject_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subject_spinner.setAdapter(subject_adapter);

        subject_spinner.setOnItemSelectedListener(this);

        // set OnClick Listener for Finish Adding Course
        finish_button = (FloatingActionButton) findViewById(R.id.finish_adding_button);
        finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer = MediaPlayer.create(v.getContext(), R.raw.poke4);
                mPlayer.start();
                addCourse();
            }
        });
    }

    /**
     * populates the Spinners of the view based on the values in the R.array folder
     *
     * @param ident - int ID of view
     * @param valuesArray - int ID of array with values
     * @param tag - static final String to identify which value is being clicked
     */
    private void populateSpinner(int ident, int valuesArray, final String tag) {
        Spinner subject_spinner = (Spinner) findViewById(ident);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> subject_adapter = ArrayAdapter.createFromResource(this,
                valuesArray, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        subject_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        subject_spinner.setAdapter(subject_adapter);
        subject_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(tag.equals(subjectTAG)) {
                    selected_subject = (String) parent.getItemAtPosition(position);
                }
                else if(tag.equals(courseTAG)){
                    selected_course = (String) parent.getItemAtPosition(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * adds a course to the database using the selected subject and course number
     */
    private void addCourse() {
        // adds a course to the database
        Course newCourse = new Course(selected_subject, selected_course);
        TutorServices.addToMyCourses(this, mFirebaseUser.getUid(), newCourse);
        // Toast when completed adding the course
        Toast.makeText(this, "Added " + newCourse.toString(), Toast.LENGTH_SHORT).show();
        // new TutorClassesListActivity with updated courses
        startActivity(new Intent(this, TutorClassesListActivity.class));
    }

    /**
     * Queries the local SQLite database to obtain course subjects and numbers
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected_subject = (String) parent.getItemAtPosition(position);
        //Now set course number spinner to active and fill it with appropriate course numbers
        List<String> courseNumbersList = CourseDBInterface.getCourseNumbersList(this, selected_subject);
        ArrayAdapter<CharSequence> course_adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        course_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        course_adapter.addAll(courseNumbersList);
        Spinner c_spinner = (Spinner) findViewById(R.id.courses_courses_spinner);
        c_spinner.setAdapter(course_adapter);
        // OnClick for Spinner
        c_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_course = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * nothing happens when nothing in spinner is selected
     * @param parent
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Nothing happens
    }


    /**
     * onDestroy
     * releases the media player when done
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}
