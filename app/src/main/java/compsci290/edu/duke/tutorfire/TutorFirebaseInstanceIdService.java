package compsci290.edu.duke.tutorfire;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * For Firebase messaging
 * Created by alex_boldt on 4/7/17.
 */

public class TutorFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private final static String TOKEN = "Token";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TOKEN, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        //sendRegistrationToServer(refreshedToken);
    }
}
