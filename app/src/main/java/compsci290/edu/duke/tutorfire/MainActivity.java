package compsci290.edu.duke.tutorfire;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import compsci290.edu.duke.tutorfire.CoursesDatabase.CourseDBInitializer;
import compsci290.edu.duke.tutorfire.CoursesDatabase.CourseDBInterface;
import compsci290.edu.duke.tutorfire.StudentModeActivities.StudentMainActivity;
import compsci290.edu.duke.tutorfire.TutorModeActivities.TutorMainActivity;
import compsci290.edu.duke.tutorfire.WebServiceInterface.UserServices;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    public static final String ANONYMOUS = "anonymous";

    private static final String TAG = "MainActivity";
    private SharedPreferences mSharedPreferences;
    private GoogleApiClient mGoogleApiClient;
    private String last_chat;


    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String mUsername;
    private String mPhotoUrl;
    private Resources res;

    //private static final String LINE_REF_EXTRA = "line_reference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        res = getResources();

        /*
        UserServices.addNewUser(this, "5", "Tiffany");
        StudentServices.getMyTutors(this, "1");
        StudentServices.findTutors(this, "CS", "101", "M", "15", "1");
        */

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Set default username is anonymous.
        mUsername = ANONYMOUS;

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }

        //set global initialized var to false
        CourseDBInterface.isInitialized = false;
        CourseDBInitializer.verifyInitialized(this);

        UserServices.addNewUser(this, mFirebaseUser.getUid(), mUsername, mPhotoUrl);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };




        /*
        List<String> subjectList = CourseDBInterface.getSubjectList(this);
        for (String s : subjectList) {
            Log.d(TAG, s);
        }

        List<String> courseList = CourseDBInterface.getCourseNumbersList(this, "MATH");
        for (String s : courseList) {
            Log.d(TAG, s);
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mUsername = ANONYMOUS;
                startActivity(new Intent(this, SignInActivity.class));
                return true;
            case R.id.show_uid_menu:
                Toast.makeText(this, "Current Firebase Uid: " + mFirebaseUser.getUid() + " " +
                        mFirebaseUser.getDisplayName() + " " + mFirebaseUser.getEmail() , Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void beginStudentMode (View v){
        //Go to student mode main activity
        startActivity(new Intent(this, StudentMainActivity.class));
    }

    public void beginTutorMode (View v){
        // Go to tutor mode main activity
        startActivity(new Intent(this, TutorMainActivity.class));
    }


    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public void userServicesIsNewUser(boolean isNew) {
//        if(isNew) {
//            // TODO go to splash screen
//            Toast.makeText(this, "New User - do Splash", Toast.LENGTH_SHORT).show();
//        } else {
//            // do nothing
//            Toast.makeText(this, "Old User - continue", Toast.LENGTH_SHORT).show();
//        }
    }

}
