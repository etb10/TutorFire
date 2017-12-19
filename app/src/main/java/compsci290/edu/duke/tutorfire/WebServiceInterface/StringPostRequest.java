package compsci290.edu.duke.tutorfire.WebServiceInterface;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Created by harrisonlundberg on 4/13/17.
 *
 * StringRequest is overriden so that getParams now returns all of the parameters that need
 * to be sent to the web service. Our web service responds to POST requests with varying number
 * of parameters depending on what service needed specifically.
 */



public class StringPostRequest extends StringRequest {

    private Map<String, String> mParams;


    /**
     *
     * @param url The url to which this request is sent
     * @param parameters The parameters for this POST request
     * @param listener
     * @param errorListener
     */
    public StringPostRequest(String url, Map<String, String> parameters, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, listener, errorListener);
        mParams = parameters;
    }

    @Override
    /**
     * @return Key-pairs of parameters for the POST request
     */
    public Map<String, String> getParams() {
        return mParams;
    }
}
