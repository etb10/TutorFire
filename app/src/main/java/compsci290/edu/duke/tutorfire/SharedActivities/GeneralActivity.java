package compsci290.edu.duke.tutorfire.SharedActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import compsci290.edu.duke.tutorfire.MainActivity;
import compsci290.edu.duke.tutorfire.R;

/**
 * Created by harrisonlundberg on 4/30/17.
 */

public class GeneralActivity extends AppCompatActivity {

    private static final String TAG = "GeneralActivity";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.general_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.back_to_main:
                //do something
                Log.d(TAG, "Back to main menu pressed");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
