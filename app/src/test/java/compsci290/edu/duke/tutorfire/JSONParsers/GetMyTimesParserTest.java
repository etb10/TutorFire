package compsci290.edu.duke.tutorfire.JSONParsers;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import compsci290.edu.duke.tutorfire.DataClasses.Time;
import compsci290.edu.duke.tutorfire.DataClasses.Tutor;
import compsci290.edu.duke.tutorfire.WebServiceInterface.RequestFailedException;

import static org.junit.Assert.*;

/**
 * Created by LinLin on 4/20/2017.
 */
public class GetMyTimesParserTest extends GetMyTimesParser{

    private static Time createTime(JSONObject jsonObject) throws JSONException {
        String day = jsonObject.getString("day");
        int start_time = jsonObject.getInt("start_time");
        int end_time = jsonObject.getInt("end_time");
        return new Time(day, start_time, end_time);
    }

    @Test
    public void parse() throws Exception, RequestFailedException {
        String json = "{'results':[{'day':'1','start_time':'12','end_time':'6'},{'day':'2,'start_time':'12','end_time':'6'},{'day':'3,'start_time':'12','end_time':'6'}";
        String timeString1 = "{'day':'1','start_time':'12','end_time':'6'}";
        String timeString2 = "{'day':'2,'start_time':'12','end_time':'6'}";
        String timeString3 = "{'day':'3,'start_time':'12','end_time':'6'}";
        JSONObject j1 = new JSONObject(timeString1);
        JSONObject j2 = new JSONObject(timeString2);
        JSONObject j3 = new JSONObject(timeString3);
        Time time1 = createTime(j1);
        Time time2 = createTime(j2);
        Time time3 = createTime(j3);

        List<Time> test1 = new ArrayList<>();
        test1.add(time1);
        test1.add(time2);
        test1.add(time3);

        List<Time> test2 = parse(json);

        assertEquals(test1, test2);
    }

}