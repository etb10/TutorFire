package compsci290.edu.duke.tutorfire.TutorModeActivities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import compsci290.edu.duke.tutorfire.DataClasses.Course;
import compsci290.edu.duke.tutorfire.R;
import compsci290.edu.duke.tutorfire.SharedActivities.GeneralActivity;
import compsci290.edu.duke.tutorfire.WebServiceInterface.TutorServices;

/**
 * Activity for RecyclerView of Courses
 */
public class TutorClassesListActivity extends GeneralActivity {

    private final static String sTAG = "TutClassesListActivity";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private List<Course> mCourses;

    private TextView mNoClassesText;

    private RecyclerView rv;

    /**
     * onCreate - Instantiate user and layout
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_classes_list);
        // Init user
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        // Hide "No courses" text
        mNoClassesText = (TextView) findViewById(R.id.no_tutors_text);
        mNoClassesText.setVisibility(View.GONE);
        // get handle to recycler view
        rv=(RecyclerView)findViewById(R.id.courses_rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        // Set FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beginAddCourseActivity(view);

            }
        });

    }

    /**
     * Override onBackPressed to return to TutorMainActivity
     */
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, TutorMainActivity.class));
    }

    /**
     * onResume - default
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.d(sTAG, "Resumed");
    }

    /**
     * Callback from TutorServices
     * if 0 courses, display prompt
     * if >0 courses, set adapter
     * @param myCourses
     */
    public void setMyCourses(List<Course> myCourses) {
        Log.d(sTAG, "setMyCourses with length of " + myCourses.size());
        // 0 courses, show "No courses" dialog
        if(myCourses.size() == 0) {
            mNoClassesText.setVisibility(View.VISIBLE);
        } else {
            // courses sent to adapter
            mNoClassesText.setVisibility(View.GONE);
            CourseRVAdapter adapter = new CourseRVAdapter(myCourses);
            rv.setAdapter(adapter);
        }
    }

    /**
     * Get courses onStart
     */
    @Override
    protected void onStart(){
        super.onStart();
        TutorServices.getMyCourses(this, mFirebaseUser.getUid());
    }

    /**
     * launch instance of AddCourseActivity
     * @param v
     */
    protected void beginAddCourseActivity(View v) {
        startActivity(new Intent(this, AddCourseActivity.class));
    }

    /**
     * release MediaPlayer
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        CourseRVAdapter adapter = (CourseRVAdapter) rv.getAdapter();
        adapter.releasePlayer();
    }
}

