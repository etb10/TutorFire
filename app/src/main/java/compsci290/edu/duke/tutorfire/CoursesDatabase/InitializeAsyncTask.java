package compsci290.edu.duke.tutorfire.CoursesDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by harrisonlundberg on 4/29/17.
 *
 * This class is used to verify and initialize the local SQLite database of course listings.
 * We use an async task so that the UI can stay responsive - initializing the courses DB takes
 * over 10 seconds, and we don't want the user to have to sit idly while this happens.
 * Fortunately, the database only needs to be initialized the first time the user installs
 * the app on their device.
 */

class InitializeAsyncTask extends AsyncTask<Context, Void, Void> {
    private static final String TAG = "InitializeAsyncTask";

    @Override
    protected Void doInBackground(Context... params) {
        //This method is always passed just one context
        Context c = params[0];
        CoursesDBHelper mHelper = new CoursesDBHelper(c.getApplicationContext());
        SQLiteDatabase mDB = mHelper.getWritableDatabase();

        //First check if mDB is empty
        Cursor currentDBCursor = mDB.query(CoursesDBContract.CourseList.TABLE_NAME, new String[]{CoursesDBContract.CourseList.SUBJECT}, null, null, null, null, null);
        Log.d(TAG, "Col count is " + currentDBCursor.getCount());
        if (currentDBCursor.getCount() == 0) {
            //We need to initialize the DB
            Log.d(TAG, "Column count is zero");
            initializeDB(c, mDB);
        }
        currentDBCursor.close();
        mDB.close();


        //Set global var of dbReady to true
        CourseDBInterface.isInitialized = true;

        return null;
    }

    /**
     * This method loads the SavageCourses.ballin file from the assets folder, loops through it
     * line by line, and adds each course to our SQLite database.
     *
     * NOTE: We thought SavageCourses.ballin would be a better name than Spring2017Courses.txt
     *
     * @param c The context of the calling activity
     * @param db The database to be initialized
     */
    private static void initializeDB(Context c, SQLiteDatabase db) {
        //Get file from assets
        AssetManager am = c.getAssets();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(am.open("SavageCourses.ballin")));
            int count = 0;
            String line = in.readLine();
            while (line != null) {
                //Add current line to DB
                String[] lineSplit = line.split(" ");
                ContentValues cv = new ContentValues();
                cv.put(CoursesDBContract.CourseList.SUBJECT, lineSplit[0]);
                cv.put(CoursesDBContract.CourseList.COURSE, lineSplit[1]);
                db.insert(CoursesDBContract.CourseList.TABLE_NAME, null, cv);
                line = in.readLine();
            }

            Log.d(TAG, count + " rows inserted into the DB");

            in.close();
        } catch (IOException e) {
            Log.d(TAG, "Error in opening the file!");
        }
    }
}
