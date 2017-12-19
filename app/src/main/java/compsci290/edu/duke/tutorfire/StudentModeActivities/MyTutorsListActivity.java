package compsci290.edu.duke.tutorfire.StudentModeActivities;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.List;

import compsci290.edu.duke.tutorfire.DataClasses.Tutor;
import compsci290.edu.duke.tutorfire.R;
import compsci290.edu.duke.tutorfire.SharedActivities.GeneralActivity;
import compsci290.edu.duke.tutorfire.SharedActivities.ProfileActivity;
import compsci290.edu.duke.tutorfire.SharedActivities.ProfileActivity;
import compsci290.edu.duke.tutorfire.WebServiceInterface.StudentServices;

/**
 * Created by Alex Boldt in mid April 2017...
 * List of My Tutors in a RecyclerView
 */
public class MyTutorsListActivity extends GeneralActivity implements TutorListReceiver{

    //public final static String TUTOR_DATA = "compsci290.edu.duke.tutorfire.StudentModeActivities.TUTOR_DATA";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private TextView mNoTutorsText;
    private Button mNoTutorsButton;

    private RecyclerView mRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tutors_list);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        // Set "No tutors" visibility to GONE
        mNoTutorsText = (TextView) findViewById(R.id.no_tutors_text);
        mNoTutorsText.setVisibility(View.GONE);

        mNoTutorsButton = (Button) findViewById(R.id.find_tutors_button);
        mNoTutorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFindTutorActivity(v);
            }
        });
        mNoTutorsButton.setVisibility(View.GONE);

        mRV = (RecyclerView) findViewById(R.id.my_tutors_rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        mRV.setLayoutManager(llm);
    }

    /**
     * Callback from OnStart() -> StudentServices.getMyTutors()
     * Sets the adapter for the Recycler View with data from the Tutors
     * @param tutors
     */
    public void receiveTutorList(List<Tutor> tutors){
        if(tutors.size() == 0) {
            mNoTutorsText.setVisibility(View.VISIBLE);
            mNoTutorsButton.setVisibility(View.VISIBLE);
        } else {
            mNoTutorsText.setVisibility(View.GONE);
            mNoTutorsButton.setVisibility(View.GONE);
            TutorListAdapter tutorListAdapter = new TutorListAdapter(this, tutors);
            mRV.setAdapter(tutorListAdapter);
        }
    }

    /**
     * Launch new instance of FindTutorsActivity when button is clicked
     * @param v
     */
    public void launchFindTutorActivity(View v) {
        startActivity(new Intent(this, FindTutorsActivity.class));
    }

    /**
     * launch StudentMainActivity when done
     */
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, StudentMainActivity.class));
    }

    @Override
    protected void onStart(){
        super.onStart();
        StudentServices.getMyTutors(this, mFirebaseUser.getUid());
    }
}
