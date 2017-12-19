package compsci290.edu.duke.tutorfire.JSONParsers;

import org.json.JSONException;
import org.json.JSONObject;

import compsci290.edu.duke.tutorfire.WebServiceInterface.RequestFailedException;

/**
 * Created by harrisonlundberg on 4/29/17.
 *
 * This class is used to parse the json response of the AddNewUser request.
 */

public class AddNewUserParser extends ResponseParser {
    //private AddNewUserParser() {}

    /**
     * This method parses the json response of the AddNewUser request and returns a boolean
     * telling whether or not the current user is a new user.
     *
     * @param json The response received from the web service
     * @return True is returned if the user is new, false if they are a returning user
     * @throws JSONException
     * @throws RequestFailedException
     */
    public static boolean parse(String json) throws JSONException, RequestFailedException {
        verifyRequestSucceeded(json);
        JSONObject all = new JSONObject(json);
        String new_user_str = all.getString("new_user");
        if (new_user_str.equals("1")) {
            return true;
        } else {
            return false;
        }
    }

}
