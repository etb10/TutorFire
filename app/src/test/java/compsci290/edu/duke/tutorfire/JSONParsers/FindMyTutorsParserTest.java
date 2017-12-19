package compsci290.edu.duke.tutorfire.JSONParsers;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import compsci290.edu.duke.tutorfire.DataClasses.Tutor;
import compsci290.edu.duke.tutorfire.WebServiceInterface.RequestFailedException;

import static org.junit.Assert.*;

/**
 * Created by LinLin on 4/17/2017.
 */
public class FindMyTutorsParserTest extends FindMyTutorsParser{

    private static Tutor createTutor(JSONObject t) throws JSONException {
        String uid = t.getString("uid");
        String name = t.getString("name");
        String photo_url = t.getString("photo_url");
        String chat_hash = t.getString("chat_hash");
        return new Tutor(uid, name, photo_url, chat_hash);
    }

    @Test
    public void parse() throws Exception, RequestFailedException {
        String json = "{'results':[{'tutor_uid':'1','name':'Alex1','photo_url':'sampleURL1','chat_hash':'sampleCHAT1','subject':'CS','course':'101'},{'tutor_uid':'2','name':'Alex2','photo_url':'sampleURL2','chat_hash':'sampleCHAT2','subject':'CS','course':'201'},{'tutor_uid':'3,'name':'Alex3','photo_url':'sampleURL3','chat_hash':'sampleCHAT3','subject':'CS','course':'301'}";
        String tutorString1 = "{'tutor_uid':'1','name':'Alex1','photo_url':'sampleURL1','chat_hash':'sampleCHAT1','subject':'CS','course':'101'}";
        String tutorString2 = "{'tutor_uid':'2','name':'Alex2','photo_url':'sampleURL2','chat_hash':'sampleCHAT2','subject':'CS','course':'201'}";
        String tutorString3 = "{'tutor_uid':'3','name':'Alex3','photo_url':'sampleURL3','chat_hash':'sampleCHAT3','subject':'CS','course':'301'}";
        JSONObject j1 = new JSONObject(tutorString1);
        JSONObject j2 = new JSONObject(tutorString2);
        JSONObject j3 = new JSONObject(tutorString3);
        Tutor tutor1 = createTutor(j1);
        Tutor tutor2 = createTutor(j2);
        Tutor tutor3 = createTutor(j3);

        List<Tutor> test1 = new ArrayList<>();
        test1.add(tutor1);
        test1.add(tutor2);
        test1.add(tutor3);

        List<Tutor> test2 = parse(json);

        assertEquals(test1, test2);
    }

}