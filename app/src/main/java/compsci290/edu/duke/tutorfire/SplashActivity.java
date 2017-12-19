package compsci290.edu.duke.tutorfire;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import compsci290.edu.duke.tutorfire.MainActivity;

/**
 * Created by ssaurel on 02/12/2016.
 */
public class SplashActivity extends AppCompatActivity {

    public static final String fromSplash = "comingFromSplashActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SplashActivity", "SPLASH!");



        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(fromSplash, true);
        startActivity(intent);
        finish();
    }
}