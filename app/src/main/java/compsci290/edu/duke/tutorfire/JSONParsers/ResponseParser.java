package compsci290.edu.duke.tutorfire.JSONParsers;


import org.json.JSONException;
import org.json.JSONObject;

import compsci290.edu.duke.tutorfire.WebServiceInterface.RequestFailedException;

/**
 * Created by harrisonlundberg on 4/14/17.
 *
 * This class is used to parse json responses and to make sure that the web service request
 * succeeded. It is extended by all of the other JSONParser classes.
 */

public class ResponseParser {
    //private ResponseParser() {}

    /**
     * This method verifies that the request sent to the web server succeeded, and throws a
     * RequestFailedException if it did not succeed.
     *
     * @param json The json response from the web server
     * @throws JSONException
     * @throws RequestFailedException
     */
    public static void verifyRequestSucceeded(String json) throws JSONException, RequestFailedException {
        JSONObject all = new JSONObject(json);
        if (!all.getString("success").equals("1")) {
            throw new RequestFailedException(all.getString("message"));
        }
    }
}
