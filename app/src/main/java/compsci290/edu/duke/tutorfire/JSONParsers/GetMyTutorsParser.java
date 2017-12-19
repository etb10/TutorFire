package compsci290.edu.duke.tutorfire.JSONParsers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import compsci290.edu.duke.tutorfire.DataClasses.Course;
import compsci290.edu.duke.tutorfire.DataClasses.Tutor;
import compsci290.edu.duke.tutorfire.WebServiceInterface.RequestFailedException;

/**
 * Created by harrisonlundberg on 4/14/17.
 *
 * This class is used to parse the json response from the GetMyTutors request.
 */

public class GetMyTutorsParser extends ResponseParser {
    //private GetMyTutorsParser() {}
    private static final String TAG = "GetMyTutorsParser";

    /**
     * This method converts the json response from the GetMyTutors request into a list of Tutor
     * objects.
     *
     * @param json The response from the web service
     * @return A list of tutor objects represented in the json string
     * @throws JSONException
     * @throws RequestFailedException
     */
    public static List<Tutor> parse(String json) throws JSONException, RequestFailedException {
        verifyRequestSucceeded(json);
        JSONObject all = new JSONObject(json);
        JSONArray results = all.getJSONArray("results");
        Map<String, Tutor> tutorMap = new HashMap<>();
        for (int i = 0; i < results.length(); i++) {
            addTutor(tutorMap, results.getJSONObject(i));
        }

        return new ArrayList<Tutor>(tutorMap.values());
    }


    /**
     * Used to convert a JSONObject into a single Tutor object
     *
     * @param tutorMap A map of uid's to Tutor objects
     * @param t A JSONObject representing one Tutor
     * @throws JSONException
     */
    private static void addTutor(Map<String, Tutor> tutorMap, JSONObject t) throws JSONException {
        String uid = t.getString("tutor_uid");
        String name = t.getString("name");
        String photo_url = t.getString("photo_url");
        String chat_hash = t.getString("chat_hash");
        String rating = t.getString("rating");
        String subject = t.getString("subject");
        String course = t.getString("course");
        if (tutorMap.containsKey(uid)) {
            Tutor currentTutor = tutorMap.get(uid);
            currentTutor.addCourse(new Course(subject, course));
        } else {
            Integer rating_val = null;
            if (!rating.equals("null")) {
                rating_val = Integer.valueOf(rating);
                Log.d(TAG, "rating_val is " + rating_val);
            }
            Tutor newTutor = new Tutor(uid, name, photo_url, chat_hash, rating_val);
            newTutor.addCourse(new Course(subject, course));
            tutorMap.put(uid, newTutor);

        }
    }
}
