package compsci290.edu.duke.tutorfire.WebServiceInterface;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import compsci290.edu.duke.tutorfire.DataClasses.Course;
import compsci290.edu.duke.tutorfire.DataClasses.Tutor;
import compsci290.edu.duke.tutorfire.JSONParsers.FindMyTutorsParser;
import compsci290.edu.duke.tutorfire.JSONParsers.GetMyCoursesParser;
import compsci290.edu.duke.tutorfire.JSONParsers.GetMyTutorsParser;
import compsci290.edu.duke.tutorfire.JSONParsers.ResponseParser;
import compsci290.edu.duke.tutorfire.SharedActivities.ProfileActivity;
import compsci290.edu.duke.tutorfire.SharedActivities.ProfileActivity;
import compsci290.edu.duke.tutorfire.StudentModeActivities.FindTutorsActivity;
import compsci290.edu.duke.tutorfire.StudentModeActivities.FoundTutorsActivity;
import compsci290.edu.duke.tutorfire.StudentModeActivities.MyTutorsListActivity;
import compsci290.edu.duke.tutorfire.StudentModeActivities.TutorListReceiver;
import compsci290.edu.duke.tutorfire.TutorModeActivities.TutorClassesListActivity;

/**
 * Created by harrisonlundberg on 4/13/17.
 *
 * This class contains methods needed by the Student mode activities. All methods take appropriate
 * parameters which are converted into an HTTP POST request and sent to our web service.
 *
 * Each of these methods must be called from a specific activity, because to deliver the needed
 * response, there must be the appropriate callback method available to the ResponseTransmitter.
 */

public class StudentServices {

    private StudentServices() {}

    private static final String TAG = "StudentServices";

    /**
     * This method is called when the tutor needs his/her list of tutors. When the response received,
     * it is delivered using the ResponseTransmitter that in turn calls receiveTutorList(),
     * which is part of the TutorListReceiver interface. Delivers a list of Tutor objects.
     *
     * @param c The context calling this method, the calling context must implement the TutorListReceiver interface.
     * @param uid The uid of the student requesting their list of tutors.
     */
    public static void getMyTutors(final TutorListReceiver c, String uid) {

        //create new string request and add to queue
        Map<String, String> params = new HashMap<>();
        params.put("student_uid", uid);

        UserServices.sendRequest((Context) c, ServerUrls.GET_MY_TUTORS, params, new ResponseTransmitter() {
            @Override
            public void deliverResponse(String response) throws JSONException, RequestFailedException {
                List<Tutor> myTutors = GetMyTutorsParser.parse(response);

                //Now call the public method of TutorListReceiver
                c.receiveTutorList(myTutors);
            }

            @Override
            public String getTag() {
                return "GetMyTutorsTransmitter";
            }
        });
    }

    /**
     * This method is called from FoundTutorsActivity. When the response is received, it is converted
     * into a list of Tutor objects and delivered to the calling activity.
     *
     * @param c The context of the calling activity, must be called from FoundTutorsActivity.
     * @param subject The desired subject
     * @param course The desired course number
     * @param day The desired day to meet with the tutor
     * @param startTime The desired start time of meeting
     * @param duration The desired length of the tutoring session
     */
    public static void findTutors(final FoundTutorsActivity c, String subject, String course, String day, String startTime, String duration) {
        //create new string request and add to queue
        Map<String, String> params = new HashMap<>();
        params.put("subject", subject);
        params.put("course", course);
        params.put("day", day);
        params.put("start_time", startTime);
        params.put("duration", duration);

        UserServices.sendRequest(c, ServerUrls.FIND_TUTORS, params, new ResponseTransmitter() {
            @Override
            public void deliverResponse(String response) throws JSONException, RequestFailedException {
                List<Tutor> tutorList = FindMyTutorsParser.parse(response);
                c.deliverTutorResults(tutorList);
            }

            @Override
            public String getTag() {
                return "FindTutorsTransmitter";
            }
        });
    }


    /**
     * This method is called from ProfileActivity to fetch the list of all the courses that a
     * tutor teaches. Delivers a list of Course objects.
     * @param c The context of the calling activity, must be called from ProfileActivity
     * @param tutor_uid The uid of the tutor whose courses you want
     */
    public static void getAllTutorCourses(final ProfileActivity c, String tutor_uid) {
        Log.d(TAG, "Calling getMyCourses on " + c.toString());
        Map<String, String> params = new HashMap<>();
        params.put("uid", tutor_uid);

        UserServices.sendRequest(c, ServerUrls.GET_MY_COURSES, params, new ResponseTransmitter() {
            @Override
            public void deliverResponse(String response) throws JSONException, RequestFailedException {
                List<Course> courseList = GetMyCoursesParser.parse(response);
                c.deliverAllCoursesList(courseList);
            }

            @Override
            public String getTag() {
                return "GetAllTutorCourses";
            }
        });
    }

    /**
     * This method is called from ProfileActivity when the student wants to remove the given
     * tutor from his/her list of tutors.
     *
     * @param c The context of the calling activity, must be ProfileActivity
     * @param tutor_uid The uid of the tutor to be removed
     * @param student_uid The uid of the student
     */
    public static void removeFromMyTutors(final ProfileActivity c, String tutor_uid, String student_uid) {

        Map<String, String> params = new HashMap<>();
        params.put("tutor_uid", tutor_uid);
        params.put("student_uid", student_uid);

        UserServices.sendRequest(c, ServerUrls.REMOVE_FROM_MY_TUTORS, params, new ResponseTransmitter() {
            @Override
            public void deliverResponse(String response) throws JSONException, RequestFailedException {
                ResponseParser.verifyRequestSucceeded(response);

                c.finishRemovingTutor();
            }

            @Override
            public String getTag() {
                return "RemoveFromMyTutors";
            }
        });
    }

    /**
     * This method is called from ProfileActivity when a student wants to add the given
     * tutor to his/her list of tutors, associated with the given course.
     *
     * @param c The context of the calling activity, must be ProfileActivity
     * @param tutor_uid The uid of the tutor to be added
     * @param student_uid The uid of the student adding this tutor
     * @param chat_hash The chat_hash of the student-tutor's chat
     * @param course The course in which the tutor will teach the student
     */
    public static void addToMyTutors(final ProfileActivity c, String tutor_uid, String student_uid, String chat_hash, Course course) {
        //Implement this

        Map<String, String> params = new HashMap<>();
        params.put("tutor_uid", tutor_uid);
        params.put("student_uid", student_uid);
        params.put("chat_hash", chat_hash);
        params.put("subject", course.getSubject());
        params.put("course", course.getCourse());

        UserServices.sendRequest(c, ServerUrls.ADD_TO_MY_TUTORS, params, new ResponseTransmitter() {
            @Override
            public void deliverResponse(String response) throws JSONException, RequestFailedException {
                ResponseParser.verifyRequestSucceeded(response);
                c.tutorAddSuccess();
            }

            @Override
            public String getTag() {
                return "AddToMyTutorsTransmitter";
            }
        });
    }

    /**
     * This method is called when the student wants to rate the given tutor.
     *
     * @param c The context of the calling activity, must be ProfileActivity
     * @param tutor_uid The uid of the tutor to be rated
     * @param student_uid The uid of the student rating the tutor
     * @param rating The rating to be given to the tutor
     */
    public static void setTutorRating(final ProfileActivity c, String tutor_uid, String student_uid, int rating) {
        Map<String, String> params = new HashMap<>();
        params.put("tutor_uid", tutor_uid);
        params.put("student_uid", student_uid);
        params.put("rating", rating + "");

        UserServices.sendRequest(c, ServerUrls.SET_TUTOR_RATING, params, new ResponseTransmitter() {
            @Override
            public void deliverResponse(String response) throws JSONException, RequestFailedException {
                ResponseParser.verifyRequestSucceeded(response);
                c.confirmRating();
            }

            @Override
            public String getTag() {
                return "SetTutorRatingTransmitter";
            }
        });
    }
}
