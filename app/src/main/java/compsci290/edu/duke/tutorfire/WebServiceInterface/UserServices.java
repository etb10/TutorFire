package compsci290.edu.duke.tutorfire.WebServiceInterface;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import compsci290.edu.duke.tutorfire.JSONParsers.AddNewUserParser;
import compsci290.edu.duke.tutorfire.JSONParsers.ResponseParser;
import compsci290.edu.duke.tutorfire.MainActivity;

/**
 * Created by harrisonlundberg on 4/13/17.
 *
 * This class contains methods which are used by both Tutor and Student mode activities.
 */

public class UserServices {
    private UserServices() {}

    private static final String TAG = "UserServices";

    /**
     * This method is called every time a user signs into the app. Even if the user is already in
     * the database of users, we call this method without worrying, because the constraints
     * in the database schema prevent there from being duplicate user entries.
     * We must call this method every time to ensure that the user is included in the database,
     * so that they have access to all of the Student and Tutor services.
     *
     * @param c The context of the calling activity
     * @param uid The uid of the user to be added to the database
     * @param name The name of the user to be added
     * @param photo_url The photo_url of the user to be added
     */
    public static void addNewUser(final MainActivity c, String uid, String name, String photo_url) {
        //TODO Update context of the calling activity and add in callback to notify whether
        //this is a new user or nah

        //create new string request and add to queue
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("name", name);
        params.put("photo_url", photo_url);

        sendRequest(c, ServerUrls.ADD_NEW_USER, params, new ResponseTransmitter() {
            @Override
            public void deliverResponse(String response) throws JSONException, RequestFailedException {
                //Just parse output and make sure there is no error
                boolean isNewUser = AddNewUserParser.parse(response);

                Log.d(TAG, isNewUser + "");
                c.userServicesIsNewUser(isNewUser);

            }

            @Override
            public String getTag() {
                return "AddNewUser";
            }

        });
    }

    /**
     * This method is called by every other service method in this package that needs to send a
     * POST request to the web server. The url passed in determines which PHP script on the server
     * will handle this request. The ResponseTransmitter passed in determines how the response
     * will be handled once it is received from the web server.
     *
     * @param c The context of the calling activity
     * @param url The url to which the request will be sent
     * @param params The parameters to be sent along with the POST request
     * @param transmitter The appropriate ResponseTransmitter to be used when the response is received
     */
    protected static void sendRequest(Context c, String url, Map<String, String> params, final ResponseTransmitter transmitter) {

        RequestQueue queue = Volley.newRequestQueue(c);
        StringPostRequest r = new StringPostRequest(url, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, response);
                //We need to process this json, and return the list of tutor objects to the activity
                try {
                    transmitter.deliverResponse(response);
                } catch (RequestFailedException e) {
                    //Just log the error right now
                    Log.e(TAG, "Error with server request - " + transmitter.getTag() + " " + e.getMessage());

                } catch (JSONException e) {
                    //Log that JSON was malformed, this shouldn't happen since we are only using our
                    //own webservice
                    Log.e(TAG, "JSONException in " + transmitter.getTag() + " " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //log the error
                Log.e(TAG, error.getMessage());
            }
        });

        queue.add(r);
    }
}
