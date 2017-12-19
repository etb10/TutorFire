package compsci290.edu.duke.tutorfire.CoursesDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.renderscript.ScriptGroup;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Created by harrisonlundberg on 4/28/17.
 *
 * This contains the verifyInitialized method which ensures that the local courses database
 * is ready when the user tries to access it either through FindTutors or MyCourses.
 */

public class CourseDBInitializer {
    private CourseDBInitializer() {}

    private static final String TAG = "CourseDBInitializer";


    /**
     * This method just creates an async task to verify that the Courses database is initialized.
     * We use an async task to avoid having an unresponsive UI.
     *
     * @param c The context of the calling activity
     */
    public static void verifyInitialized(Context c) {

        new InitializeAsyncTask().execute(c);

    }
}
