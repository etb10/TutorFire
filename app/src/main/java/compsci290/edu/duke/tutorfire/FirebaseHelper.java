package compsci290.edu.duke.tutorfire;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import static compsci290.edu.duke.tutorfire.ChatActivity.CHATS_CHILD;

/**
 * Created by alex_boldt on 4/10/17.
 */

public class FirebaseHelper {
    private static final String TAG = "FirebaseHelper";

    /**
     *
     * @param uid1 FB uid of user 1
     * @param name1 user 1's name
     * @param uid2 FB uid of
     * @param name2
     * @return the String key of the TFChatLine that is used to access the TFChatLine in the FB Databse
     */
    public static String createNewChat(String name1, String uid1, String name2, String uid2) {
        DatabaseReference mDatatbaseRef = FirebaseDatabase.getInstance().getReference();
        TFChatLine line = new TFChatLine(name1, uid1, name2, uid2);
        String hashkey = mDatatbaseRef.child(CHATS_CHILD).push().getKey();
        mDatatbaseRef.child(CHATS_CHILD).child(hashkey).setValue(line);
        TFChatMessage intro_message = new TFChatMessage("Welcome to TutorFire!", null, null, null);
        mDatatbaseRef.child(CHATS_CHILD).child(hashkey).child("messages").push().setValue(intro_message);
        return hashkey;
    }

    /**
     *
     * @param chatLineHash The chat line to delete
     * @return the Task representing the removal request
     */
    public static Task removeChatLine(String chatLineHash){
        DatabaseReference mDatatbaseRef = FirebaseDatabase.getInstance().getReference();
        Task removalTask = mDatatbaseRef.child(CHATS_CHILD).child(chatLineHash).removeValue();
        removalTask.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                String message =String.format("Chat deleted: %s", task.isSuccessful());
                Log.d(TAG, message);

            }
        });
        return removalTask;
    }





}
