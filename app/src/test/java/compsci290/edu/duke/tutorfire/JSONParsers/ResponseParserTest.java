package compsci290.edu.duke.tutorfire.JSONParsers;
import org.json.JSONException;
import org.json.JSONObject;

import compsci290.edu.duke.tutorfire.WebServiceInterface.RequestFailedException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by LinLin on 4/20/2017.
 */
public class ResponseParserTest extends ResponseParser {

    @Test
    public void testThrowsRequestFailedException(){
        boolean thrown = false;
        String test1 = "{'success':0,'message':'this test failed'}";

        try{
            verifyRequestSucceeded(test1);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (RequestFailedException e){
            thrown = true;
        }

        assertTrue(thrown);
    }

}