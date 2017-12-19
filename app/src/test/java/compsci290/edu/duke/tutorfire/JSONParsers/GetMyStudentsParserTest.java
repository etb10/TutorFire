package compsci290.edu.duke.tutorfire.JSONParsers;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import compsci290.edu.duke.tutorfire.DataClasses.Course;
import compsci290.edu.duke.tutorfire.DataClasses.Student;
import compsci290.edu.duke.tutorfire.DataClasses.Tutor;
import compsci290.edu.duke.tutorfire.WebServiceInterface.RequestFailedException;

import static org.junit.Assert.*;

/**
 * Created by LinLin on 4/20/2017.
 */
public class GetMyStudentsParserTest extends GetMyStudentsParser{

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

    @Test
    public void parse() throws Exception, RequestFailedException {
        String json = "{'results':[{'student_uid':'1','name':'Alex1','photo_url':'sampleURL1','chat_hash':'sampleCHAT','subject':'CS','course':'101'},{'student_uid':'2','name':'Alex2','photo_url':'sampleURL2','chat_hash':'sampleCHAT2','subject':'CS','course':'201'},{'student_uid':'3','name':'Alex3','photo_url':'sampleURL3','chat_hash':'sampleCHAT3','subject':'CS','course':'301'}";
        String studentString1 = "{'student_uid':'1','name':'Alex1','photo_url':'sampleURL1','chat_hash':'sampleCHAT','subject':'CS','course':'101'}";
        String studentString2 = "{'student_uid':'2','name':'Alex2','photo_url':'sampleURL2','chat_hash':'sampleCHAT','subject':'CS','course':'201'}";
        String studentString3 = "{'student_uid':'3','name':'Alex3','photo_url':'sampleURL3','chat_hash':'sampleCHAT','subject':'CS','course':'301'}";

        Map<String, Student> studentMap = new HashMap<String, Student>();

        JSONObject j1 = new JSONObject(studentString1);
        JSONObject j2 = new JSONObject(studentString2);
        JSONObject j3 = new JSONObject(studentString3);

        Student student1 = new Student(j1.getString("student_uid"),j1.getString("name"),j1.getString("photo_url"),j1.getString("chat_hash"));
        Student student2 = new Student(j2.getString("student_uid"),j2.getString("name"),j2.getString("photo_url"),j2.getString("chat_hash"));
        Student student3 = new Student(j3.getString("student_uid"),j3.getString("name"),j3.getString("photo_url"),j3.getString("chat_hash"));
        studentMap.put(student1.getUid(), student1);
        studentMap.put(student2.getUid(), student1);
        studentMap.put(student3.getUid(), student1);

        List<Student> test1 = (List<Student>) studentMap.values();

        List<Student> test2 = parse(json);

        assertEquals(test1, test2);
    }

}