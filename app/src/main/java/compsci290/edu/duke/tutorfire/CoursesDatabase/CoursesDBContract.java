package compsci290.edu.duke.tutorfire.CoursesDatabase;

import android.provider.BaseColumns;

/**
 * Created by harrisonlundberg on 4/28/17.
 *
 * This class defines the schema for our Courses database.
 */

public class CoursesDBContract {
    private CoursesDBContract() {}

    public static class CourseList implements BaseColumns {
        public static final String TABLE_NAME = "CourseList";
        public static final String SUBJECT = "subject";
        public static final String COURSE = "course";
    }
}
