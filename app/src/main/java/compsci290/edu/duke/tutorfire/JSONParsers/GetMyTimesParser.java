package compsci290.edu.duke.tutorfire.JSONParsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import compsci290.edu.duke.tutorfire.DataClasses.Time;
import compsci290.edu.duke.tutorfire.WebServiceInterface.RequestFailedException;

/**
 * Created by harrisonlundberg on 4/19/17.
 *
 * This class is used to parse the json response from the GetMyTimes request.
 */

public class GetMyTimesParser extends ResponseParser {
    //private GetMyTimesParser() {}

    /**
     * This method is used convert the response from the GetMyTimes into a list of Time objects,
     * which represents when the tutor is free.
     *
     * @param json The response from the GetMyTimes request
     * @return The list of Time objects represented in the json string
     * @throws JSONException
     * @throws RequestFailedException
     */
    public static List<Time> parse(String json) throws JSONException, RequestFailedException {
        verifyRequestSucceeded(json);
        JSONObject all = new JSONObject(json);
        JSONArray results = all.getJSONArray("results");
        List<Time> timesList = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            timesList.add(createTime(results.getJSONObject(i)));
        }
        return timesList;
    }

    /**
     * This method converts a JSONObject representing one time slot into a Time object.
     *
     * @param jsonObject Represents one Time slot
     * @return The time object representing the JSONObject
     * @throws JSONException
     */
    private static Time createTime(JSONObject jsonObject) throws JSONException {
        String day = jsonObject.getString("day");
        int start_time = jsonObject.getInt("start_time");
        int end_time = jsonObject.getInt("end_time");
        return new Time(day, start_time, end_time);
    }
}
