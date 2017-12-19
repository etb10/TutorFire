package compsci290.edu.duke.tutorfire.JSONParsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import compsci290.edu.duke.tutorfire.DataClasses.Course;
import compsci290.edu.duke.tutorfire.WebServiceInterface.RequestFailedException;

/**
 * Created by harrisonlundberg on 4/14/17.
 *
 * This class is used to parse the json response from the GetMyCourses request.
 */

public class GetMyCoursesParser extends ResponseParser {

    //private GetMyCoursesParser() {}

    /**
     * This method parses the json response from the GetMyCourses request and converts it into
     * a list of Course objects.
     *
     * @param json The response from the GetMyCourses request
     * @return The list of courses this tutor teaches
     * @throws JSONException
     * @throws RequestFailedException
     */
    public static List<Course> parse(String json) throws JSONException, RequestFailedException {
        verifyRequestSucceeded(json);
        JSONObject all = new JSONObject(json);
        JSONArray results = all.getJSONArray("results");
        List<Course> courseList = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            courseList.add(createCourse(results.getJSONObject(i)));
        }
        return courseList;
    }

    /**
     * Converts one JSONObject representing a single course into a course object
     *
     * @param j A JSONObject representing one course
     * @return Returns a course object of the passed in json
     * @throws JSONException
     */
    private static Course createCourse(JSONObject j) throws JSONException {
        //Extract fields and instantiate course object
        String subject = j.getString("subject");
        String course = j.getString("course");
        return new Course(subject, course);
    }
}
