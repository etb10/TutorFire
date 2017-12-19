package compsci290.edu.duke.tutorfire.WebServiceInterface;

/**
 * Created by harrisonlundberg on 4/14/17.
 *
 * This exception is thrown by ResponseParser if the json response does not indicate a success
 * on the web server side.
 */

public class RequestFailedException extends Throwable {
    private String mMessage;

    public RequestFailedException(String message) {
        mMessage = message;
    }

    /**
     *
     * @return The error message from the web server
     */
    public String getMessage() {
        return mMessage;
    }
}
