package compsci290.edu.duke.tutorfire.TutorModeActivities;

import android.content.Intent;
import android.net.Uri;
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
import compsci290.edu.duke.tutorfire.SharedActivities.AverageTutorRatingReceiver;
import compsci290.edu.duke.tutorfire.SharedActivities.GeneralActivity;
import compsci290.edu.duke.tutorfire.TimeSelector.ScreenSlidePagerActivity;
import compsci290.edu.duke.tutorfire.WebServiceInterface.TutorServices;

/**
 * TutorMainActivity
 *
 * Displays User's photo (Glide), their rating as a Tutor, 3 options as buttons
 */
public class TutorMainActivity extends GeneralActivity implements AverageTutorRatingReceiver {

    private TextView nameView;
    private TextView ratingView;
    private ImageView profPic;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private Double mRating;

    /**
     * Init user and layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_main);
        // init user
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        // init layout
        nameView = (TextView) findViewById(R.id.profileNameView);
        ratingView = (TextView) findViewById(R.id.ratingView);
        profPic = (ImageView) findViewById(R.id.profileImageView);
        // create photo profile picture
        Uri photoUrl = mFirebaseUser.getPhotoUrl();
        if (photoUrl != null ) { //order matters
            Glide.with(TutorMainActivity.this)
                    .load(photoUrl)
                    .override(300,300)
                    .fitCenter()
                    .into(profPic);
        }
        // populate name view
        if(mFirebaseUser.getDisplayName() != null) {
            nameView.setText(mFirebaseUser.getDisplayName());
        }
        // get average rating from DB
        TutorServices.getMyAverageRating(this, mFirebaseUser.getUid());
    }

    /**
     * checks if photoUrl is real - NOT USED IN THIS ACTIVITY
     * @param photoUrl - photo URL as String
     * @return true if real, false if not
     */
    public boolean isPhotoUrlValid(String photoUrl){
        return (photoUrl != null && photoUrl.length() > 0 && photoUrl.startsWith("https://"));
    }

    /**
     * Override onBackPressed to always return to MainActivity
     */
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    /**
     * Launch new instance of MyStudentsListActivity
     * @param v
     */
    public void beginMyStudentListActivity(View v) {
        startActivity(new Intent(this, MyStudentsListActivity.class));
    }

    /**
     * Launch new instance of TutorClassesListActivity if DB is ready
     * Usually only not ready when app is initially installed
     * @param v
     */
    public void beginMyCoursesListActivity(View v) {
        if (CourseDBInterface.isInitialized) {
            startActivity(new Intent(this, TutorClassesListActivity.class));
        } else {
            Toast.makeText(this, "Course List Database Is Not Yet Initialized", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Launch new instance of ScreenSlidePagerActivity
     * @param v
     */
    public void beginScreenSlidePagerActivity(View v) {
        startActivity(new Intent(this, ScreenSlidePagerActivity.class));
    }

    /**
     * Callback for TutorServices to deliver average rating
     * @param rating
     */
    public void deliverAverageRating(Double rating) {
        // accept rating
        mRating = rating;
        // populate rating view
        if(mRating != null) {
            ratingView.setText("Average Rating: " + mRating.toString() +"/5.0");
        } else {
            // Text remains default
        }
    }

}
