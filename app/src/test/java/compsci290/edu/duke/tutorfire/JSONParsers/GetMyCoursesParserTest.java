package compsci290.edu.duke.tutorfire.JSONParsers;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import compsci290.edu.duke.tutorfire.DataClasses.Course;
import compsci290.edu.duke.tutorfire.DataClasses.Tutor;
import compsci290.edu.duke.tutorfire.WebServiceInterface.RequestFailedException;

import static org.junit.Assert.*;

/**
 * Created by LinLin on 4/20/2017.
 */
public class GetMyCoursesParserTest extends  GetMyCoursesParser{

    private static Course createCourse(JSONObject j) throws JSONException {
        //Extract fields and instantiate course object
        String subject = j.getString("subject");
        String course = j.getString("course");
        return new Course(subject, course);
    }

    @Test
    public void parse() throws Exception, RequestFailedException {
        String json = "{'results':[{'tutor_uid':'1','name':'Alex1','photo_url':'sampleURL1','chat_hash':'sampleCHAT1','subject':'CS','course':'101'},{'tutor_uid':'2','name':'Alex2','photo_url':'sampleURL2','chat_hash':'sampleCHAT2','subject':'CS','course':'201'},{'tutor_uid':'3','name':'Alex3','photo_url':'sampleURL3','chat_hash':'sampleCHAT3','subject':'CS','course':'301'}";
        String courseString1 = "{'tutor_uid':'1','name':'Alex1','photo_url':'sampleURL1','chat_hash':'sampleCHAT1','subject':'CS','course':'101'}";
        String courseString2 = "{'tutor_uid':'2','name':'Alex2','photo_url':'sampleURL2','chat_hash':'sampleCHAT2','subject':'CS','course':'201'}";
        String courseString3 = "{'tutor_uid':'3','name':'Alex3','photo_url':'sampleURL3','chat_hash':'sampleCHAT3','subject':'CS','course':'301'}";
        JSONObject j1 = new JSONObject(courseString1);
        JSONObject j2 = new JSONObject(courseString2);
        JSONObject j3 = new JSONObject(courseString3);
        Course course1 = createCourse(j1);
        Course course2 = createCourse(j2);
        Course course3 = createCourse(j3);

        List<Course> test1 = new ArrayList<>();
        test1.add(course1);
        test1.add(course2);
        test1.add(course3);

        List<Course> test2 = parse(json);

        assertEquals(test1, test2);
    }

}