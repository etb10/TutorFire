package compsci290.edu.duke.tutorfire.WebServiceInterface;

import android.content.Context;

import org.json.JSONException;

/**
 * Created by harrisonlundberg on 4/14/17.
 *
 * This interface is implemented by classes that manage the response our Volley HTTP request
 * receives from the web server.
 */

public interface ResponseTransmitter {

    void deliverResponse(String response) throws JSONException, RequestFailedException;

    /**
     *
     * @return The specific name of the ResponseTransmitter - used for debugging.
     */
    String getTag();
}
