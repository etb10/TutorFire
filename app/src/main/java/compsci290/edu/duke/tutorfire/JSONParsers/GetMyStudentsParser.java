package compsci290.edu.duke.tutorfire.JSONParsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import compsci290.edu.duke.tutorfire.DataClasses.Course;
import compsci290.edu.duke.tutorfire.DataClasses.Student;
import compsci290.edu.duke.tutorfire.WebServiceInterface.RequestFailedException;

/**
 * Created by harrisonlundberg on 4/14/17.
 *
 * This class is used to parse the json response from the GetMyStudents request.
 */

public class GetMyStudentsParser extends ResponseParser {

    //private GetMyStudentsParser() {}

    /**
     * This method converts the json response from the GetMyStudents request into a list
     * of Student objects.
     *
     * @param response The response from the GetMyStudents request
     * @return The list of students this json string represents
     * @throws JSONException
     * @throws RequestFailedException
     */
    public static List<Student> parse(String response) throws JSONException, RequestFailedException {
        verifyRequestSucceeded(response);
        JSONObject all = new JSONObject(response);
        JSONArray results = all.getJSONArray("results");

        Map<String, Student> studentMap = new HashMap<String, Student>();
        //Loop through json string, create entry in map, takes student uid to a list of courses he teaches, and chat id
        for (int i = 0; i < results.length(); i++) {
            addStudent(studentMap, results.getJSONObject(i));
        }

        return new ArrayList<>(studentMap.values());
    }

    /**
     *
     * @param studentMap A map of uid's to Student objects
     * @param j A JSONObject to be converted into a Student object
     * @throws JSONException
     */
    private static void addStudent(Map<String, Student> studentMap, JSONObject j) throws JSONException {
        //First extract fields
        String uid = j.getString("student_uid");
        String name = j.getString("name");
        String photo_url = j.getString("photo_url");
        String chat_hash = j.getString("chat_hash");
        String subject = j.getString("subject");
        String course = j.getString("course");
        if (studentMap.containsKey(uid)) {
            Student currentStudent = studentMap.get(uid);
            currentStudent.addCourse(new Course(subject, course));
        } else {
            Student currentStudent = new Student(uid, name, photo_url, chat_hash);
            currentStudent.addCourse(new Course(subject, course));
            studentMap.put(uid, currentStudent);
        }
    }
}
