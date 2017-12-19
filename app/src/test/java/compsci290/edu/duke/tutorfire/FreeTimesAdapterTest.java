package compsci290.edu.duke.tutorfire;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import compsci290.edu.duke.tutorfire.DataClasses.Time;
import compsci290.edu.duke.tutorfire.TimeSelector.FreeTimesAdapter;

import static org.junit.Assert.assertEquals;

/**
 * Created by harrisonlundberg on 4/27/17.
 */

public class FreeTimesAdapterTest {

    @Test
    public void combineFreeTimesTest() {
        //Create divided list, create combined list, assert they are equal
        List<Time> dividedList = new ArrayList<>();
        List<Time> combinedList = new ArrayList<>();
        assertEquals(combinedList, FreeTimesAdapter.combineFreeTimes(dividedList));

        dividedList.add(new Time("Mo", 0, 1));
        combinedList.add(new Time("Mo", 0, 1));
        assertEquals(combinedList, FreeTimesAdapter.combineFreeTimes(dividedList));

        dividedList.add(new Time("Mo", 1, 2));
        combinedList.remove(0);
        combinedList.add(new Time("Mo", 0, 2));
        assertEquals(combinedList, FreeTimesAdapter.combineFreeTimes(dividedList));

        dividedList.add(new Time("Tu", 2, 3));
        combinedList.add(new Time("Tu", 2, 3));
        assertEquals(combinedList, FreeTimesAdapter.combineFreeTimes(dividedList));

        dividedList.add(new Time("Wed", 0, 1));
        dividedList.add(new Time("Wed", 1, 2));
        dividedList.add(new Time("Wed", 2, 3));
        combinedList.add(new Time("Wed", 0,3));
        assertEquals(combinedList, FreeTimesAdapter.combineFreeTimes(dividedList));
    }

    @Test
    public void divideFreeTimesTest() {
        List<Time> dividedList = new ArrayList<>();
        List<Time> combinedList = new ArrayList<>();
        assertEquals(dividedList, FreeTimesAdapter.divideFreeTimes(combinedList));

        dividedList.add(new Time("Mo", 0, 1));
        combinedList.add(new Time("Mo", 0, 1));
        assertEquals(dividedList, FreeTimesAdapter.divideFreeTimes(combinedList));

        dividedList.add(new Time("Mo", 1, 2));
        combinedList.remove(0);
        combinedList.add(new Time("Mo", 0, 2));
        assertEquals(dividedList, FreeTimesAdapter.divideFreeTimes(combinedList));

        dividedList.add(new Time("Tu", 2, 3));
        combinedList.add(new Time("Tu", 2, 3));
        assertEquals(dividedList, FreeTimesAdapter.divideFreeTimes(combinedList));

        dividedList.add(new Time("Wed", 0, 1));
        dividedList.add(new Time("Wed", 1, 2));
        dividedList.add(new Time("Wed", 2, 3));
        combinedList.add(new Time("Wed", 0,3));
        assertEquals(dividedList, FreeTimesAdapter.divideFreeTimes(combinedList));
    }
}
