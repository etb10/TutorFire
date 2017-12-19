package compsci290.edu.duke.tutorfire.CoursesDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by harrisonlundberg on 4/28/17.
 *
 * This class has methods that can be used to access the local Courses database.
 */

public class CourseDBInterface {
    private CourseDBInterface() {}

    public static boolean isInitialized;

    /**
     * This method is used to get a list of all of the Subjects offered at Duke.
     *
     * @param c The context of the calling activity
     * @return The list of Subjects in the Courses DB
     */
    public static List<String> getSubjectList(Context c) {
        //open db
        CoursesDBHelper mHelper = new CoursesDBHelper(c);
        SQLiteDatabase db = mHelper.getReadableDatabase();
        //Query for all subjects
        Cursor results = db.query(true, CoursesDBContract.CourseList.TABLE_NAME, new String[]{CoursesDBContract.CourseList.SUBJECT}, null, null, null, null, null, null);

        List<String> resultList = new ArrayList<>();

        while(results.moveToNext()) {
            resultList.add(results.getString(results.getColumnIndex(CoursesDBContract.CourseList.SUBJECT)));
        }

        results.close();
        db.close();
        return resultList;
    }

    /**
     * This method is used to get all the course numbers offered for a given subject.
     *
     * @param c The context of the calling activity
     * @param subject The subject for which you want all the course offerings
     * @return The list of course numbers offered for this subject
     */
    public static List<String> getCourseNumbersList(Context c, String subject) {
        //open db
        CoursesDBHelper mHelper = new CoursesDBHelper(c);
        SQLiteDatabase db = mHelper.getReadableDatabase();

        String[] projection = new String[]{CoursesDBContract.CourseList.COURSE};
        String selection = CoursesDBContract.CourseList.SUBJECT + " = ?";
        String[] selectionArgs = new String[]{subject};


        Cursor results = db.query(CoursesDBContract.CourseList.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        List<String> resultList = new ArrayList<>();

        while(results.moveToNext()) {
            resultList.add(results.getString(results.getColumnIndex(CoursesDBContract.CourseList.COURSE)));
        }

        results.close();
        db.close();
        Collections.sort(resultList);
        return resultList;
    }
}

