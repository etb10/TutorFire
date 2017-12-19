package compsci290.edu.duke.tutorfire.JSONParsers;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

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
public class GetMyTutorsParserTest extends GetMyTutorsParser{

    private static Tutor addTutor(Map<String, Tutor> tutorMap, JSONObject t) throws JSONException {
        String uid = t.getString("tutor_uid");
        String name = t.getString("name");
        String photo_url = t.getString("photo_url");
        String chat_hash = t.getString("chat_hash");
        String subject = t.getString("subject");
        String course = t.getString("course");
        if (tutorMap.containsKey(uid)) {
            Tutor currentTutor = tutorMap.get(uid);
            currentTutor.addCourse(new Course(subject, course));
        } else {
            tutorMap.put(uid, new Tutor(uid, name, photo_url, chat_hash));
        }
        return new Tutor(uid, name, photo_url, chat_hash);
    }

    @Test
    public void parse() throws Exception, RequestFailedException {
        String json = "{'results':[{'tutor_uid':'1','name':'Alex1','photo_url':'sampleURL1','chat_hash':'sampleCHAT','subject':'CS','course':'101'},{'tutor_uid':'2','name':'Alex2','photo_url':'sampleURL2','chat_hash':'sampleCHAT2','subject':'CS','course':'201'},{'tutor_uid':'3','name':'Alex3','photo_url':'sampleURL3','chat_hash':'sampleCHAT3','subject':'CS','course':'301'}";
        String tutorString1 = "{'tutor_uid':'1','name':'Alex1','photo_url':'sampleURL1','chat_hash':'sampleCHAT','subject':'CS','course':'101'}";
        String tutorString2 = "{'tutor_uid':'2','name':'Alex2','photo_url':'sampleURL2','chat_hash':'sampleCHAT','subject':'CS','course':'201'}";
        String tutorString3 = "{'tutor_uid':'3','name':'Alex3','photo_url':'sampleURL3','chat_hash':'sampleCHAT','subject':'CS','course':'301'}";

        Map<String, Tutor> tutorMap = new HashMap<>();

        JSONObject j1 = new JSONObject(tutorString1);
        JSONObject j2 = new JSONObject(tutorString2);
        JSONObject j3 = new JSONObject(tutorString3);

        Tutor tutor1 = new Tutor(j1.getString("tutor_uid"),j1.getString("name"),j1.getString("photo_url"),j1.getString("chat_hash"));
        Tutor tutor2 = new Tutor(j2.getString("tutor_uid"),j2.getString("name"),j2.getString("photo_url"),j2.getString("chat_hash"));
        Tutor tutor3 = new Tutor(j3.getString("tutor_uid"),j3.getString("name"),j3.getString("photo_url"),j3.getString("chat_hash"));
        tutorMap.put(tutor1.getUid(), tutor1);
        tutorMap.put(tutor2.getUid(), tutor2);
        tutorMap.put(tutor3.getUid(), tutor3);

        List<Tutor> test1 = (List<Tutor>) tutorMap.values();

        List<Tutor> test2 = parse(json);

        assertEquals(test1, test2);
    }

}