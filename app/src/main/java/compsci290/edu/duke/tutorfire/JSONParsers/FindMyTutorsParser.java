package compsci290.edu.duke.tutorfire.JSONParsers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import compsci290.edu.duke.tutorfire.DataClasses.Tutor;
import compsci290.edu.duke.tutorfire.StudentModeActivities.FindTutorsActivity;
import compsci290.edu.duke.tutorfire.WebServiceInterface.RequestFailedException;

/**
 * Created by harrisonlundberg on 4/14/17.
 *
 * This class is used to parse the json response from the FindTutors request.
 */

public class FindMyTutorsParser extends ResponseParser {
    //private FindMyTutorsParser() {}

    private static final String TAG = "FindMyTutorsParser";

    /**
     * This method is passed the JSON response from the FindTutors request and converts
     * it to a list of Tutor objects.
     *
     * @param json The response received from the FindTutors request
     * @return The list of Tutors is returned
     * @throws JSONException
     * @throws RequestFailedException
     */
    public static List<Tutor> parse(String json) throws JSONException, RequestFailedException {
        verifyRequestSucceeded(json);
        JSONObject all = new JSONObject(json);
        JSONArray results = all.getJSONArray("results");
        List<Tutor> tutorList = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            tutorList.add(createTutor(results.getJSONObject(i)));
        }
        return tutorList;
    }

    /**
     * This method is called by parse when it needs to convert a JSONObject into one Tutor object
     *
     * @param t The JSONObject which represents one tutor
     * @return Tutor object translation of the JSONObject
     * @throws JSONException
     */
    private static Tutor createTutor(JSONObject t) throws JSONException {
        String uid = t.getString("uid");
        String name = t.getString("name");
        String photo_url = t.getString("photo_url");
        String avg_rating = t.getString("avg_rating");
        Double avg_rating_value = null;
        if (!avg_rating.equals("null")) {
            avg_rating_value = Double.valueOf(avg_rating);
            Log.d(TAG, "Value of avg_rating_value is " + avg_rating_value);
        }
        String chat_hash = "";
        return new Tutor(uid, name, photo_url, chat_hash, avg_rating_value);
    }
}
