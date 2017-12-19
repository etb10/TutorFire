package compsci290.edu.duke.tutorfire.StudentModeActivities;


import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import compsci290.edu.duke.tutorfire.DataClasses.Course;
import compsci290.edu.duke.tutorfire.DataClasses.Tutor;
import compsci290.edu.duke.tutorfire.R;
import compsci290.edu.duke.tutorfire.SharedActivities.GeneralActivity;
import compsci290.edu.duke.tutorfire.SharedActivities.ProfileActivity;
import compsci290.edu.duke.tutorfire.SharedActivities.ProfileActivity;
import compsci290.edu.duke.tutorfire.WebServiceInterface.StudentServices;

/**
 * Activity that contains a RecyclerView of tutors that match the student's search criteria
 * Created by Alex Boldt mid April 2017
 */

public class FoundTutorsActivity extends GeneralActivity {

    private Resources res;
    private String[] SearchQuery;

    private RecyclerView mRV;
    private TextView mNoTutorsText;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private static final String TAG = "FoundTutorsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_tutors);
        res = getResources();

        //RecyclerView to hold found tutors
        mRV = (RecyclerView) findViewById(R.id.found_tutors_rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        mRV.setLayoutManager(llm);

        // Set "No tutors" visibility to GONE
        mNoTutorsText = (TextView) findViewById(R.id.no_tutors_text);
        mNoTutorsText.setVisibility(View.GONE);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
    }

    public void deliverTutorResults(List<Tutor> tutors) {
        Log.d(TAG, String.format("Number of tutors returned to FoundTutors: %d", tutors.size()));

        if(tutors.size() == 0) {
            // no tutors found, so set visibility to VISIBLE
            mNoTutorsText.setVisibility(View.VISIBLE);
        } else {
            // tutor list was greater than 0, therefore convert listView
            findAndRemoveSelf(tutors);
            Collections.sort(tutors, new TutorComparator());
            FoundTutorListAdapter adapter = new FoundTutorListAdapter(this, tutors, new Course(SearchQuery[0], SearchQuery[1]));
            mRV.setAdapter(adapter);
        }
    }

    /**
     * Get dat intent
     */
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        SearchQuery = intent.getStringArrayExtra(res.getString(R.string.TutorSearchIntentKey));
        Log.d(TAG, String.format("Search arguments passed to StudentServices.findTutors: %s", Arrays.toString(SearchQuery)));
        StudentServices.findTutors(this, SearchQuery[0], SearchQuery[1], SearchQuery[2], SearchQuery[3], SearchQuery[4]);
    }

    /**
     * Comparator to compare tutors by average rating
     * Null ratings placed at front to give new tutors exposure
     */
    private class TutorComparator implements Comparator<Tutor> {

        @Override
        public int compare(Tutor o1, Tutor o2) {
            Double avg1 = o1.getAverageRating();
            Double avg2 = o2.getAverageRating();
            if (avg1 == null) {
                if (avg2 == null) {
                    return 0;
                } else {
                    return -1;
                }
            } else if (avg2 == null) {
                return 1;
            } else {
                return avg2.compareTo(avg1);
            }
        }
    }


    /**
     * If the student matches her own criteria, remove
     * @param tutorList List of Tutors that match
     */
    private void findAndRemoveSelf(List<Tutor> tutorList) {
        String student_uid = mFirebaseUser.getUid();
        Tutor currentTutor;
        for (int i = 0; i < tutorList.size(); i++) {
            currentTutor = tutorList.get(i);
            if (currentTutor.getUid().equals(student_uid)) {
                tutorList.remove(i);
                Log.d(TAG, "Removed self from results list");
            }
        }
    }

}
