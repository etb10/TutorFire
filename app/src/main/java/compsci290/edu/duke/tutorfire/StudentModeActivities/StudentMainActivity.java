package compsci290.edu.duke.tutorfire.StudentModeActivities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import compsci290.edu.duke.tutorfire.CoursesDatabase.CourseDBInterface;
import compsci290.edu.duke.tutorfire.MainActivity;
import compsci290.edu.duke.tutorfire.R;
import compsci290.edu.duke.tutorfire.SharedActivities.GeneralActivity;
import compsci290.edu.duke.tutorfire.SignInActivity;
import compsci290.edu.duke.tutorfire.TutorModeActivities.TutorMainActivity;

/**
 * Created by Alex Boldt in the Stone Age
 * Displays profile picture, name, a button to access My Tutors or Find a New Tutor
 */

public class StudentMainActivity extends GeneralActivity {

    private TextView nameView;
    private ImageView profPic;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        mContext = this;

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        nameView = (TextView) findViewById(R.id.profileNameView);
        profPic = (ImageView) findViewById(R.id.profileImageView);

        // create photo profile picture
        Uri photoUrl = mFirebaseUser.getPhotoUrl();
        if (photoUrl != null ) { //order matters
            Glide.with(StudentMainActivity.this)
                    .load(photoUrl)
                    .override(300,300)
                    .fitCenter()
                    .into(profPic);
        }

        // populate name view
        if(mFirebaseUser.getDisplayName() != null) {
            nameView.setText(mFirebaseUser.getDisplayName());
        }

    }

    /**
     * On back pressed, go to Main Activity
     */
    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContext, MainActivity.class));
    }

    /**
     * Go to MyTutorsList Activity
     * @param v View that pressed
     */
    public void beginMyTutorListActivity(View v){
        startActivity(new Intent(this, MyTutorsListActivity.class));
    }

    /**
     * Go to FindTutors Activity if the Course database is initialized
     * @param v
     */
    public void beginFindTutorsActivity(View v){
        if (CourseDBInterface.isInitialized) {
            startActivity(new Intent(this, FindTutorsActivity.class));
        } else {
            Toast.makeText(this, "Course List Database Is Not Yet Initialized", Toast.LENGTH_SHORT).show();
        }
    }
}
