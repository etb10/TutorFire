package compsci290.edu.duke.tutorfire.TutorModeActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import compsci290.edu.duke.tutorfire.DataClasses.Student;
import compsci290.edu.duke.tutorfire.R;
import compsci290.edu.duke.tutorfire.SharedActivities.GeneralActivity;
import compsci290.edu.duke.tutorfire.TimeSelector.ScreenSlidePagerActivity;
import compsci290.edu.duke.tutorfire.WebServiceInterface.TutorServices;

/**
 * Activity for displaying RecyclerView of Student Objects
 * Allows for viewing student profiles through ProfileActivity
 * Allows for chatting with students directly through CardView
 */
public class MyStudentsListActivity extends GeneralActivity {

    public final static String STUDENT_DATA = "compsci290.edu.duke.tutorfire.StudentModeActivities.STUDENT_DATA";
    public final static String sTAG = "MyStudentsListActivity";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String[] student_data_arr;

    private RecyclerView rv;
    private TextView mNoTutorsText;
    private Button mCoursesButton;
    private Button mTimesButton;

    private Context mContext;

    /**
     * onCreate - Instantiates layout and user
     * Sets "No Tutors" Visibility to GONE, anticipates List.size() > 0
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_students_list);
        // Init user and context
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mContext = this;
        // get handle to recycler view
        rv=(RecyclerView)findViewById(R.id.students_rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        // Set "No students" visibility to GONE
        mNoTutorsText = (TextView) findViewById(R.id.no_tutors_text);
        mNoTutorsText.setVisibility(View.GONE);
        // Set "No students" buttons' OnClicks
        mCoursesButton = (Button) findViewById(R.id.my_courses_button);
        mCoursesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, TutorClassesListActivity.class));
            }
        });
        mCoursesButton.setVisibility(View.GONE);

        mTimesButton = (Button) findViewById(R.id.my_times_button);
        mTimesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ScreenSlidePagerActivity.class));
            }
        });
        mTimesButton.setVisibility(View.GONE);

    }

    /**
     * Override onBackPressed to always return to TutorMainActivity
     */
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, TutorMainActivity.class));
    }

    /**
     * createStudentViews
     * Launches the ProfileActivity with the parcelable Student object created from StudentListAdapter.
     * @param students - list of Tutor's students
     */
    public void createStudentViews(List<Student> students) {
        Log.d(sTAG, "createStudentViews");

        // No students in DB, show the "No Students" edge case
        if(students.size() == 0) {
            mNoTutorsText.setVisibility(View.VISIBLE);
            mTimesButton.setVisibility(View.VISIBLE);
            mCoursesButton.setVisibility(View.VISIBLE);
        } else {
            // students in DB, hide "No Students"
            mNoTutorsText.setVisibility(View.GONE);
            mTimesButton.setVisibility(View.GONE);
            mCoursesButton.setVisibility(View.GONE);
            // use adapter to populate RV
            StudentListAdapter adapter = new StudentListAdapter(students);
            rv.setAdapter(adapter);
        }
    }

    /**
     * Retrieve students from DB onStart
     */
    @Override
    protected void onStart(){
        super.onStart();
        TutorServices.getMyStudents(this, mFirebaseUser.getUid());
    }
}