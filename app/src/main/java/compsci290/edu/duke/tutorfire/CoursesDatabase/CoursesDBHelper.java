package compsci290.edu.duke.tutorfire.CoursesDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import compsci290.edu.duke.tutorfire.CoursesDatabase.CoursesDBContract.CourseList;

/**
 * Created by harrisonlundberg on 4/28/17.
 *
 * This class is used to manage our Courses database.
 */

public class CoursesDBHelper extends SQLiteOpenHelper {

    //TODO Should we remove this variable to allow for multiple versions of DB?
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "coursesdb.db";


    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + CourseList.TABLE_NAME + " ("
                + CourseList._ID + " INTEGER PRIMARY KEY, "
                + CourseList.SUBJECT + " TEXT, "
                + CourseList.COURSE + " TEXT)";

    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS" + CourseList.TABLE_NAME;

    public CoursesDBHelper(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }
}
