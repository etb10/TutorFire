package compsci290.edu.duke.tutorfire.SharedActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import compsci290.edu.duke.tutorfire.ChatActivity;
import compsci290.edu.duke.tutorfire.DataClasses.Course;
import compsci290.edu.duke.tutorfire.DataClasses.Person;
import compsci290.edu.duke.tutorfire.DataClasses.Tutor;
import compsci290.edu.duke.tutorfire.FirebaseHelper;
import compsci290.edu.duke.tutorfire.R;
import compsci290.edu.duke.tutorfire.StudentModeActivities.MyTutorsListActivity;
import compsci290.edu.duke.tutorfire.StudentModeActivities.TutorListReceiver;
import compsci290.edu.duke.tutorfire.WebServiceInterface.StudentServices;
import compsci290.edu.duke.tutorfire.WebServiceInterface.TutorServices;

/**
 * created by Alex Boldt in 4/27/16
 * Profile Activity displays a profile of a tutor or student
 */


public class ProfileActivity extends GeneralActivity implements TutorListReceiver, AverageTutorRatingReceiver{


    /**
     * enum of profile states in order to give correct UI given a state
     * NEWFOUNDTUTOR : We have never seen this Tutor before/they are not in MyTutors.
     * PRIORFOUNDTUTOR : This Tutor is in MyTutors and we have found them again through another class
     * MYTUTOR : This Tutor is in my Tutor and all classes of interest are shared between the Tutor and Student
     * STUDENT : Student profile viewable by Tutors
     */
    public enum ProfileState{
        NEWFOUNDTUTOR, PRIORFOUNDTUTOR, MYTUTOR, STUDENT
    }


    /**
     *Data structures for managing profile
     */
    private ProfileState currentState;
    private boolean isTutor;
    private String mUid;
    private String mName;
    private String mPhotoUrl;
    private String mChatLineHash;
    private static String IllegalStateMessage;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private Person mPerson;
    private Course newCourse;
    private Double currentAvgRating;
    private boolean startedUp = false;

    public boolean isRatingSet;
    public Integer currentUserRating;


    private Resources res;

    /**
     * UI elements
     */
    private TextView nameView;
    private FloatingActionButton chatButton;
    private TextView ratingView;
    private LinearLayout ratingLayout;
    private FloatingActionButton addButton;
    private Button removeButton;
    private TextView sharedCoursesGuide;
    private TextView allCoursesGuide;
    private RecyclerView sharedCoursesView;
    private RecyclerView allCoursesView;
    private Button submitRatingButton;
    private LinearLayout raterLayout;

    private MediaPlayer mAddTutorPlayer;
    private MediaPlayer mRateTutorPlayer;
    private MediaPlayer mRemoveTutorPlayer;

    private ImageView[] stars;


    /**
     * static finals
     */
    private static final String TAG = "ProfileActivity";
    private static final int MIN_RATING = 0;
    private static final int MAX_RATING = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //get resources and authentication state
        res = getResources();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        //instantiate UI elements
        chatButton = (FloatingActionButton) findViewById(R.id.chatButton);
        addButton = (FloatingActionButton) findViewById(R.id.addToMyTutorsButton);
        removeButton = (Button) findViewById(R.id.removeFromMyTutorsButton);
        sharedCoursesView = (RecyclerView) findViewById(R.id.sharedCoursesView);
        LinearLayoutManager sharedCourseManager = new LinearLayoutManager(this);
        sharedCoursesView.setLayoutManager(sharedCourseManager);
        sharedCoursesView.setHasFixedSize(true);
        allCoursesView = (RecyclerView) findViewById(R.id.allCoursesView);
        LinearLayoutManager allCourseManager = new LinearLayoutManager(this);
        allCoursesView.setLayoutManager(allCourseManager);
        allCoursesView.setHasFixedSize(true);
        sharedCoursesGuide = (TextView) findViewById(R.id.sharedCoursesGuideView);
        allCoursesGuide = (TextView) findViewById(R.id.allCoursesGuideView);
        nameView = (TextView) findViewById(R.id.profileNameView);
        ratingView = (TextView) findViewById(R.id.ratingView);
        ratingLayout = (LinearLayout) findViewById(R.id.ratingLayout);
        raterLayout = (LinearLayout) findViewById(R.id.raterLayout);
        submitRatingButton = (Button) findViewById(R.id.submitRatingButton);

        //instantiate stars layout touch listener
        raterLayout.setOnTouchListener(new StarSliderOnTouchListener(this));

        stars = new ImageView[MAX_RATING];
        stars[0] = (ImageView) findViewById(R.id.star1);
        stars[1] = (ImageView) findViewById(R.id.star2);
        stars[2] = (ImageView) findViewById(R.id.star3);
        stars[3] = (ImageView) findViewById(R.id.star4);
        stars[4] = (ImageView) findViewById(R.id.star5);


        //extract passed person for profile from intent
        Intent intent = getIntent();
        mPerson = intent.getParcelableExtra(res.getString(R.string.PersonDataProfileIntentKey));
        newCourse = intent.getParcelableExtra(res.getString(R.string.CourseIntentKey));
        mUid = mPerson.getUid();
        mName = mPerson.getName();
        mPhotoUrl = mPerson.getPhotoUrl();
        isTutor = intent.getBooleanExtra(res.getString(R.string.IsTutorProfileIntentKey), true);
        currentAvgRating = mPerson.getAverageRating();
        currentUserRating = mPerson.getRating();

        //fill stars with last rating student gave this tutor
        if (currentUserRating != null) {
            fillStars(currentUserRating);
        }

        //if this profile is of a tutor, get all of the tutors of this student to find their chat hash
        //and see if the new course being added is already shared
        //otherwise complete UI start up
        if(isTutor){
            StudentServices.getMyTutors(this, mFirebaseUser.getUid());
        }else{
            completeStartUp();
        }
    }

    /**
     * Finish data extraction from passed person and set UI for the current state
     */
    public void completeStartUp(){
        mChatLineHash = mPerson.getChatHash();
        currentUserRating = mPerson.getRating();
        Log.d(TAG, String.format("Current average rating of %s: %s", mName, currentAvgRating));
        //set name
        nameView.setText(mName);
        if( !(currentAvgRating == null || currentAvgRating < MIN_RATING || currentAvgRating > MAX_RATING)) {
            ratingView.setText(String.format("%.1f", currentAvgRating));
        }

        Log.d(TAG, String.format("Person Data: %s, %s, %s, %s, %s", mUid, mName, mPhotoUrl, mChatLineHash, newCourse));

        //set profile picture
        ImageView profPic = (ImageView) findViewById(R.id.profileImageView);
        if (isPhotoUrlValid(mPhotoUrl)) { //order matters
            Glide.with(ProfileActivity.this)
                    .load(mPhotoUrl)
                    .override(300,300)
                    .fitCenter()
                    .into(profPic);
        }

        //set is ratingSet, to prevent the user from sending rating unintentionally, is later set by the StarSlider listener
        isRatingSet = false;

        //read by deliverAverageRating to re-draw average rating after initial startup
        startedUp = true;

        refreshUI();
    }

    /**
     * Given a profile state, set the UI to reflect relevant information
     */
    public void refreshUI(){
        manageProfileState();
        flushUI();
        switch (currentState){
            case NEWFOUNDTUTOR:
                createNewFoundTutorUI();
                break;
            case PRIORFOUNDTUTOR:
                createPriorFoundTutorUI();
                break;
            case MYTUTOR:
                createMyTutorUI();
                break;
            case STUDENT:
                createStudentUI();
                break;
            default:
                IllegalStateMessage = String.format("FATAL: ILLEGAL PROFILE STATE with ChatLineHash: %s, newCourse: %s, isTutor: %s", mChatLineHash, newCourse, isTutor);
                Log.e(TAG, IllegalStateMessage);
                break;
        }
    }

    /**
     * Set the profile state enum based on state of current metadata
     * NEWFOUNDTUTOR : No chat exists and a new course exists
     * PRIORFOUNDTUTOR : Chat exists and a new course exists
     * MYTUTOR : Chat exists and new course does not exist
     * STUDENT : Chat exists and is not a Tutor
     * Otherwise, log an error message with the illegal state
     */
    public void manageProfileState(){
        if (!isChatValid(mChatLineHash) && isTutor && newCourse != null){
            currentState = ProfileState.NEWFOUNDTUTOR;
        }else if (isChatValid(mChatLineHash) && isTutor && newCourse != null){
            currentState = ProfileState.PRIORFOUNDTUTOR;
        }else if (isChatValid(mChatLineHash) && isTutor && newCourse == null){
            currentState = ProfileState.MYTUTOR;
        }else if (isChatValid(mChatLineHash) && !isTutor){
            currentState = ProfileState.STUDENT;
        }else{
            IllegalStateMessage = String.format("FATAL: ILLEGAL PROFILE STATE with ChatLineHash: %s, newCourse: %s, isTutor: %s", mChatLineHash, newCourse, isTutor);
            Log.e(TAG, IllegalStateMessage);
        }
        Log.d(TAG, String.format("Current profile state data: ChatLineHash: %s, newCourse: %s, isTutor: %s", mChatLineHash, newCourse, isTutor));
        Log.d(TAG, String.format("Switching state to %s", currentState));
    }

    /**
     * Clear the UI of any view that depends on profile state, called whenever state changes
     */
    public void flushUI(){
        ratingLayout.setVisibility(View.GONE);
        chatButton.setVisibility(View.GONE);
        addButton.setVisibility(View.GONE);
        removeButton.setVisibility(View.GONE);
        sharedCoursesView.setVisibility(View.GONE);
        allCoursesView.setVisibility(View.GONE);
        sharedCoursesGuide.setVisibility(View.GONE);
        allCoursesGuide.setVisibility(View.GONE);
        raterLayout.setVisibility(View.GONE);
        submitRatingButton.setVisibility(View.GONE);
    }

    /**
     * Rating
     * Add FAB
     * All Courses
     */
    public void createNewFoundTutorUI(){
        ratingLayout.setVisibility(View.VISIBLE);
        addButton.setVisibility(View.VISIBLE);
        allCoursesGuide.setText("All the courses I teach:");
        allCoursesGuide.setVisibility(View.VISIBLE);
        allCoursesView.setVisibility(View.VISIBLE);
        StudentServices.getAllTutorCourses(this, mUid);
    }

    /**
     * Rating
     * Add FAB
     * Shared Courses
     * All Courses
     */
    public void createPriorFoundTutorUI(){
        ratingLayout.setVisibility(View.VISIBLE);
        addButton.setVisibility(View.VISIBLE);
        sharedCoursesGuide.setText("Courses this Tutor already teaches you:");
        sharedCoursesGuide.setVisibility(View.VISIBLE);
        sharedCoursesView.setVisibility(View.VISIBLE);
        allCoursesGuide.setText("All of the courses this Tutor teaches:");
        allCoursesGuide.setVisibility(View.VISIBLE);
        allCoursesView.setVisibility(View.VISIBLE);
        populateSharedCoursesView();
        StudentServices.getAllTutorCourses(this, mUid);
    }

    /**
     * Rating
     * Chat FAB
     * Remove FAB
     * Shared Courses
     * Star Rater
     * Submit Rating Button
     */
    public void createMyTutorUI(){
        ratingLayout.setVisibility(View.VISIBLE);
        chatButton.setVisibility(View.VISIBLE);
        removeButton.setVisibility(View.VISIBLE);
        sharedCoursesGuide.setText("Courses this Tutor teaches you:");
        sharedCoursesGuide.setVisibility(View.VISIBLE);
        sharedCoursesView.setVisibility(View.VISIBLE);
        raterLayout.setVisibility(View.VISIBLE);
        submitRatingButton.setVisibility(View.VISIBLE);
        populateSharedCoursesView();
    }

    /**
     * Chat FAB
     * Shared Courses
     */
    public void createStudentUI(){
        chatButton.setVisibility(View.VISIBLE);
        sharedCoursesGuide.setText("Courses you teach this Student:");
        sharedCoursesGuide.setVisibility(View.VISIBLE);
        sharedCoursesView.setVisibility(View.VISIBLE);
        populateSharedCoursesView();
    }

    /**
     * Callback from StudentServices.getMyTutors(), then gets Average Rating
     * @param tutors List of Tutors
     */
    public void receiveTutorList(List<Tutor> tutors){
        for (Tutor t : tutors){
            if (t.getUid().equals(mUid)){
                //mChatLineHash = t.getChatHash();
                mPerson = t;
                if (newCourse != null){
                    for (Course c : mPerson.getCourseList()){
                        if (newCourse.equals(c)){
                            String ex_str = String.format("You already share %s with %s", newCourse, mName);
                            Toast.makeText(this, ex_str, Toast.LENGTH_SHORT).show();
                            newCourse = null;
                            break;
                        }
                    }
                }
                break;
            }
        }
        TutorServices.getMyAverageRating(this, mUid);
    }

    /**
     * Add this tutor to student MyTutor list, calls into StudentServices.addToMyTutors()
     * @param v View that was pressed
     */
    public void addToMyTutors(View v){
        if (newCourse == null){
            String err_str = "Attempting to add to myTutors without a new course";
            Log.d(TAG, String.format(err_str));
            Toast.makeText(this, err_str, Toast.LENGTH_SHORT).show();
            return;
        }

        //If this is a new tutor, create a new chat in the Firebase Database, else use the current chat
        if(currentState == ProfileState.NEWFOUNDTUTOR) {
            mChatLineHash = FirebaseHelper.createNewChat(mFirebaseUser.getDisplayName(), mFirebaseUser.getUid(),
                    mName, mUid);
            Log.d(TAG, String.format("New chat hash created: %s", mChatLineHash));
        } else{
            Log.d(TAG, String.format("Keeping old chat hash: %s", mChatLineHash));
        }

        StudentServices.addToMyTutors(this, mUid, mFirebaseUser.getUid(), mChatLineHash, newCourse);
    }

    /**
     *Callback from addToMyTutors() -> StudentServices.addToMyTutors(), pops a toast,
     * add the new Course to the person object and then deletes the new Course, then refresh UI to MyTutor mode
     */
    public void tutorAddSuccess(){
        Toast.makeText(this, "Tutor Added Successfully!", Toast.LENGTH_SHORT).show();
        mPerson.addCourse(newCourse);
        newCourse = null;
        refreshUI();
        mAddTutorPlayer = MediaPlayer.create(this, R.raw.poke7);
        mAddTutorPlayer.start();

    }

    /**
     * Begin removeFromMyTutors which calls a dialog
     * @param v View that was pressed
     */
    public void removeFromMyTutors(View v) {
        Log.d(TAG, "RemoveFromMyTutors called");
        removeTutorDialog(this);
    }

    /**
     * Open a dialog to confirm the removal of this tutor
     * @param context this activity
     */
    private void removeTutorDialog(final ProfileActivity context) {
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_AppCompat_Light_DarkActionBar))
                .setTitle("Remove Tutor")
                .setMessage("Are you sure you want to remove this Tutor?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        FirebaseHelper.removeChatLine(mChatLineHash);
                        StudentServices.removeFromMyTutors(context, mUid, mFirebaseUser.getUid());
                        startActivity(new Intent(context, MyTutorsListActivity.class));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //nah
                        return;
                    }
                }).setCancelable(true)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     *Callback from StudentServices.removeFromMyTutors()
     * Pops a toast and finishes the activity
     */
    public void finishRemovingTutor() {
        //Display toast with tutor removed successfully message

        mRemoveTutorPlayer = MediaPlayer.create(this, R.raw.poke4);
        mRemoveTutorPlayer.start();

        String remove_message = "TutorFired!";
        Log.d(TAG, remove_message);
        Toast.makeText(this, remove_message, Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * populates the RecyclerView of shared courses
     */
    private void populateSharedCoursesView() {
        Log.d(TAG, mPerson.getCourseList().size() + "");
        ProfileCourseRVAdapter adapter = new ProfileCourseRVAdapter(mPerson.getCourseList());
        sharedCoursesView.setAdapter(adapter);
    }

    /**
     * Callback from StudentServices.getAllTutorCourses, then sets the RecyclerView for all courses
     * @param courseList
     */
    public void deliverAllCoursesList(List<Course> courseList) {
        ProfileCourseRVAdapter adapter = new ProfileCourseRVAdapter(courseList);
        allCoursesView.setAdapter(adapter);
    }


    /**
     * Fills a star given a star's index and a boolean of whether to fill it or empty it
     * @param star_index in 1-indexed position of the star to fill, so star n is the nth star, which
     *                   signifies a rating of n stars
     * @param fill boolean if true, fill the star, else make it empty
     */
    public void fillStar(int star_index, boolean fill){
        if (fill){
            stars[star_index-1].setImageResource(R.drawable.ic_star_white_24dp);
        } else{
            stars[star_index-1].setImageResource(R.drawable.ic_star_border_white_24dp);
        }
    }

    /**
     * Given a rating, fill the appropriate amount of stars beginning from the left
     * @param rating an int, must be greater than MIN_RATING and less or equal to MAX_RATING
     */
    public void fillStars(int rating){
        if (rating <= MIN_RATING || rating > MAX_RATING){
            return;
        }
        for(int i = 1; i <= rating; i++){
            fillStar(i, true);
        }
        for(int j = rating + 1; j <= MAX_RATING; j++){
            fillStar(j, false);
        }
    }

    /**
     * Submit a rating to the DB based on currentUserRating, which is written by the StarSliderOnTouchListener
     * @param v View that was pressed
     */
    public void submitRating(View v){
        if (currentUserRating == null || currentUserRating == -1 || !isRatingSet){
            Toast.makeText(this, "Please set a rating", Toast.LENGTH_SHORT).show();
            return;
        }else if (currentUserRating >= MIN_RATING && currentUserRating <= MAX_RATING && isRatingSet){
            StudentServices.setTutorRating(this, mUid, mFirebaseUser.getUid(), currentUserRating);
        }else{
            String exec_string = String.format("Illegal current user rating: %s", currentUserRating);
            Toast.makeText(this, exec_string, Toast.LENGTH_SHORT).show();
            Log.e(TAG, exec_string);
            return;
        }
    }

    /**
     * Callback from sumbmitTutorRating() -> StudentServices.setTutorRating()
     */
    public void confirmRating(){
        mRateTutorPlayer = MediaPlayer.create(this, R.raw.poke6);
        mRateTutorPlayer.start();
        Toast.makeText(this, String.format("Rating sent: %s", currentUserRating), Toast.LENGTH_SHORT).show();
        TutorServices.getMyAverageRating(this, mUid);
    }

    /**
     *
     * @param v
     */
    public void goToChat(View v){
        if(!isChatValid(mChatLineHash)){
            String chat_exc_str =  String.format("Chat line for users: %s, %s does not exist", mFirebaseUser.getDisplayName(), mName);
            Log.d(TAG, chat_exc_str);
            Toast.makeText(this, chat_exc_str, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent chat_intent = new Intent(this, ChatActivity.class);
        chat_intent.putExtra(res.getString(R.string.ChatHashIntentKey), mChatLineHash);
        startActivity(chat_intent);
    }


    /**
     *
     * @param chatHash a string representing the url of a chat between two users
     * @return true if the hash is valid by Firebase standards
     */
    public static boolean isChatValid(String chatHash){
        return !(chatHash == null || "".equals(chatHash) || !chatHash.startsWith("-"));
    }

    /**
     *
     * @param photoUrl a string representing the internet url of a photo
     * @return true if photoUrl is a valid identifier
     */
    public static boolean isPhotoUrlValid(String photoUrl){
        return (photoUrl != null && photoUrl.length() > 0 && photoUrl.startsWith("https://"));
    }

    @Override
    public void deliverAverageRating(Double rating) {
        currentAvgRating = rating;
        if(startedUp){
            if( !(currentAvgRating == null || currentAvgRating < MIN_RATING || currentAvgRating > MAX_RATING)) {
                ratingView.setText(String.format("%.1f", currentAvgRating));
            }
        } else{
            completeStartUp();
        }
    }

    @Override
    /**
     * Goes back to MyTutors list is the tutor was added
     */
    public void onBackPressed() {
        if (currentState == ProfileState.MYTUTOR){
            startActivity(new Intent(this, MyTutorsListActivity.class));
        } else{
            super.onBackPressed();
        }
    }

    /**
     * bing bada boom
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAddTutorPlayer != null) {
            mAddTutorPlayer.release();
            mAddTutorPlayer = null;
        }
        if (mRateTutorPlayer != null) {
            mRateTutorPlayer.release();
            mRateTutorPlayer = null;
        }
        if (mRemoveTutorPlayer != null) {
            mRemoveTutorPlayer.release();
            mRemoveTutorPlayer = null;
        }
    }
}
