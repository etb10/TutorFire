package compsci290.edu.duke.tutorfire.JSONParsers;

import org.json.JSONException;
import org.json.JSONObject;

import compsci290.edu.duke.tutorfire.WebServiceInterface.RequestFailedException;

/**
 * Created by harrisonlundberg on 4/29/17.
 *
 * This class is used to parse the json response from the GetMyAverageRating request.
 */

public class GetMyAverageRatingParser extends ResponseParser {

    //private GetMyAverageRatingParser() {}

    /**
     * This method converts the json response from the GetMyAverageRating request into
     * a double which is the tutor's average rating.
     *
     * @param json The response from the GetMyAverageRating request
     * @return The average rating of the tutor is returned, null if the tutor is not yet rated by any students
     * @throws JSONException
     * @throws RequestFailedException
     */
    public static Double parse(String json) throws JSONException, RequestFailedException{
        verifyRequestSucceeded(json);
        JSONObject all = new JSONObject(json);
        String avg_rating_str = all.getString("avg_rating");
        if (avg_rating_str.equals("null")) {
            return null;
        } else {
            return Double.parseDouble(avg_rating_str);
        }
    }
}
