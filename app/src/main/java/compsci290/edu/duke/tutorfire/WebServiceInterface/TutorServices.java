package compsci290.edu.duke.tutorfire.WebServiceInterface;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import compsci290.edu.duke.tutorfire.DataClasses.Course;
import compsci290.edu.duke.tutorfire.DataClasses.Student;
import compsci290.edu.duke.tutorfire.DataClasses.Time;
import compsci290.edu.duke.tutorfire.JSONParsers.GetMyAverageRatingParser;
import compsci290.edu.duke.tutorfire.JSONParsers.GetMyCoursesParser;
import compsci290.edu.duke.tutorfire.JSONParsers.GetMyStudentsParser;
import compsci290.edu.duke.tutorfire.JSONParsers.GetMyTimesParser;
import compsci290.edu.duke.tutorfire.JSONParsers.ResponseParser;
import compsci290.edu.duke.tutorfire.SharedActivities.AverageTutorRatingReceiver;
import compsci290.edu.duke.tutorfire.TimeSelector.ScreenSlidePagerActivity;
import compsci290.edu.duke.tutorfire.TutorModeActivities.MyStudentsListActivity;
import compsci290.edu.duke.tutorfire.TutorModeActivities.TutorClassesListActivity;
import compsci290.edu.duke.tutorfire.TutorModeActivities.TutorMainActivity;

/**
 * Created by harrisonlundberg on 4/13/17.
 *
 * This class contains methods needed by the Tutor mode activities. All methods take appropriate
 * parameters which are converted into an HTTP POST request and sent to our web service.
 *
 * Each of these methods must be called from a specific activity, because to deliver the needed
 * response, there must be the appropriate callback method available to the ResponseTransmitter.
 */

public class TutorServices {

    private TutorServices() {}

    private static final String TAG = "TutorServices";

    /**
     * This method is called when the tutor wants his/her list of current students. When the
     * response is received, it is converted to a list of Student objects and delivered to the
     * calling activity.
     *
     * @param c The context of the calling activity, must be called from MyStudentsListActivity
     * @param uid The uid of the tutor requesting their list of students
     */
    public static void getMyStudents(final MyStudentsListActivity c, String uid) {
        //TutorActivity can either be a concrete class, or just an INTERFACE!! with some callback method we will use
        //Create a specific listener for this event

        Map<String, String> params = new HashMap<>();
        params.put("tutor_uid", uid);

        UserServices.sendRequest(c, ServerUrls.GET_MY_STUDENTS, params, new ResponseTransmitter() {
            @Override
            public void deliverResponse(String response) throws JSONException, RequestFailedException {
                List<Student> studentList = GetMyStudentsParser.parse(response);
                //Call MyStudentsListActivity.createViews method ? pass it studentList
                c.createStudentViews(studentList);
            }

            @Override
            public String getTag() {
                return "GetMyStudentsTransmitter";
            }
        });
    }

    /**
     * This method is called when the tutor wants his/her list of current courses. When the
     * response is received, it is converted to a list of Courses objects and delivered to the
     * calling activity.
     *
     * @param c The context of the calling activity, must be called from TutorClassesListActivity
     * @param uid The uid of the tutor requesting their list of courses
     */
    public static void getMyCourses(final TutorClassesListActivity c, String uid) {

        Log.d(TAG, "Calling getMyCourses on " + c.toString());
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);

        UserServices.sendRequest(c, ServerUrls.GET_MY_COURSES, params, new ResponseTransmitter() {
            @Override
            public void deliverResponse(String response) throws JSONException, RequestFailedException {
                List<Course> courseList = GetMyCoursesParser.parse(response);
                // passes courses into the listView to be adapted
                c.setMyCourses(courseList);
            }

            @Override
            public String getTag() {
                return "GetMyCoursesTransmitter";
            }
        });

    }

    /**
     * This method is called when the tutor wants his/her list of current free time slots. When the
     * response is received, it is converted to a list of Time objects and delivered to the
     * calling activity.
     *
     * @param c The context of the calling activity, must be called from ScreenSlidePagerActivity
     * @param uid The uid of the Tutor requesting their list of free times
     */
    public static void getMyTimes(final ScreenSlidePagerActivity c, String uid) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);

        UserServices.sendRequest(c, ServerUrls.GET_MY_TIMES, params, new ResponseTransmitter() {
            @Override
            public void deliverResponse(String response) throws JSONException, RequestFailedException {
                List<Time> timeList = GetMyTimesParser.parse(response);

                c.setTimesList(timeList);
            }

            @Override
            public String getTag() {
                return "GetMyTimesTransmitter";
            }
        });
    }


    //TODO This method is no longer used
    public static void addToMyTimes(Context c, String uid, String day, String start_hour, String end_hour) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("day", day);
        params.put("start_time", start_hour);
        params.put("end_time", end_hour);

        UserServices.sendRequest(c, ServerUrls.ADD_TO_MY_TIMES, params, new ResponseTransmitter() {
            @Override
            public void deliverResponse(String response) throws JSONException, RequestFailedException {
                ResponseParser.verifyRequestSucceeded(response);

                //TODO update class of calling context
                //TODO insert callback to that activity here

            }

            @Override
            public String getTag() {
                return "AddToMyTimesTransmitter";
            }
        });
    }

    /**
     * This method is called when the tutor wants to update their list of free time slots.
     * It takes a list of Time objects which are sent to the webservice and replace all of
     * the previous free times. The list of Time objects must be first be converted to a JSON
     * string before being sent.
     *
     * @param c The context of the calling activity
     * @param uid The uid of the tutor requesting their times
     * @param timeList The list of Time objects to be sent to the web server
     */
    public static void addToMyTimes(Context c, String uid, List<Time> timeList) {
        //convert timeList to json string
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        String jsonTimeListString = "";
        try {
            jsonTimeListString = convertToJSON(timeList);
        } catch (JSONException e) {
            Log.d(TAG, "Error in converting List<Time> to JSON: " + e.getMessage());
            return;
        }
        Log.d(TAG, "String sent to server: " + jsonTimeListString);
        params.put("json_time_string", jsonTimeListString);

        UserServices.sendRequest(c, ServerUrls.ADD_TO_MY_TIMES, params, new ResponseTransmitter() {
            @Override
            public void deliverResponse(String response) throws JSONException, RequestFailedException {
                ResponseParser.verifyRequestSucceeded(response);

                //TODO insert callbacks here
            }

            @Override
            public String getTag() {
                return "AddToMyTimesTransmitter";
            }
        });
    }

    /**
     * This method is used to convert a list of Time objects into a JSON string which is
     * parsed by the web service.
     *
     * @param timeList The list of Time objects to be converted to JSON
     * @return The JSON representation of the time objects passed in
     * @throws JSONException
     */
    private static String convertToJSON(List<Time> timeList) throws JSONException {
        JSONObject all = new JSONObject();
        JSONArray timesArray = new JSONArray();
        for (Time t : timeList) {
            //convert each time object into jsonobject
            JSONObject time = new JSONObject();
            time.put("day", t.getDay());
            time.put("start_time", t.getStartHour() + "");
            time.put("end_time", t.getEndHour() + "");
            timesArray.put(time);
        }
        all.put("times", timesArray);
        return all.toString();
    }

    //TODO This method should no longer be used and should be deleted
    //Right now this just deletes the whole time chunk
    //If you are wanting to just cut part of the time chunk out, we will need to change some things
    public static void removeFromMyTimes(Context c, String uid, String day, String start_hour, String end_hour) {
        //TODO implement this method
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("day", day);
        params.put("start_time", start_hour);
        params.put("end_time", end_hour);

        UserServices.sendRequest(c, ServerUrls.REMOVE_FROM_MY_TIMES, params, new ResponseTransmitter() {
            @Override
            public void deliverResponse(String response) throws JSONException, RequestFailedException {
                ResponseParser.verifyRequestSucceeded(response);

                //TODO Update the context class which calls this method and call some callback?
            }

            @Override
            public String getTag() {
                return "RemoveFromMyTimesTransmitter";
            }
        });
    }

    //TODO This method should no longer be used and should be deleted
    public static void removeFromMyTimes(Context c, String uid, List<Time> timeList) {
        //convert timeList to json string
        // same code as addToMyTimes
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        String jsonTimeListString = "";
        try {
            jsonTimeListString = convertToJSON(timeList);
        } catch (JSONException e) {
            Log.d(TAG, "Error in converting List<Time> to JSON: " + e.getMessage());
            return;
        }
        Log.d(TAG, "String sent to server: " + jsonTimeListString);
        params.put("json_time_string", jsonTimeListString);

        UserServices.sendRequest(c, ServerUrls.REMOVE_FROM_MY_TIMES, params, new ResponseTransmitter() {
            @Override
            public void deliverResponse(String response) throws JSONException, RequestFailedException {
                ResponseParser.verifyRequestSucceeded(response);

                //TODO insert callbacks here
            }

            @Override
            public String getTag() {
                return "RemoveFromMyTimesTransmitter";
            }
        });
    }

    /**
     * This method is called when a tutor wants to remove a course from their list of courses.
     *
     * @param c The context of the calling activity
     * @param uid The uid of the tutor wanting to remove this course
     * @param course The course to be removed from the tutor's list of courses
     */
    public static void removeFromMyCourses(Context c, String uid, Course course) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("subject", course.getSubject());
        params.put("course", course.getCourse());

        UserServices.sendRequest(c, ServerUrls.REMOVE_FROM_MY_COURSES, params, new ResponseTransmitter() {
            @Override
            public void deliverResponse(String response) throws JSONException, RequestFailedException {
                ResponseParser.verifyRequestSucceeded(response);

                //TODO Update the context class which calls this method and call some callback?
            }

            @Override
            public String getTag() {
                return "RemoveFromMyCoursesTransmitter";
            }
        });
    }


    /**
     * This method is called when a tutor wants to add a course to their list of courses.
     *
     * @param c The context of the calling activity
     * @param uid The uid of the tutor wanting to add this course
     * @param course The course to be added to the tutor's list of courses
     */
    public static void addToMyCourses(Context c, String uid, Course course) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("subject", course.getSubject());
        params.put("course", course.getCourse());


        UserServices.sendRequest(c, ServerUrls.ADD_TO_MY_COURSES, params, new ResponseTransmitter() {
            @Override
            public void deliverResponse(String response) throws JSONException, RequestFailedException {
                ResponseParser.verifyRequestSucceeded(response);

                //TODO Update the context class which calls this method and call some callback?

            }

            @Override
            public String getTag() {
                return "AddToMyCoursesTransmitter";
            }
        });
    }


    /**
     * This method is called when the tutor wants to know their average rating from their students.
     *
     * @param c The context of the calling activity, must be called from TutorMainActivity
     * @param uid The uid of the tutor requesting their average rating
     */
    public static void getMyAverageRating(final AverageTutorRatingReceiver c, String uid) {
        Map<String, String> params = new HashMap<>();
        params.put("tutor_uid", uid);

        UserServices.sendRequest((Context) c, ServerUrls.GET_MY_AVERAGE_RATING, params, new ResponseTransmitter() {
            @Override
            public void deliverResponse(String response) throws JSONException, RequestFailedException {
                Double avg_rating = GetMyAverageRatingParser.parse(response);

                c.deliverAverageRating(avg_rating);
            }

            @Override
            public String getTag() {
                return "GetMyAverageRatingTransmitter";
            }
        });
    }

}
