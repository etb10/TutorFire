package compsci290.edu.duke.tutorfire;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * A Firebase JSOn object for TutorFire Chats (a Line of communication)
 * Created by alex_boldt on 4/10/17.
 */

@IgnoreExtraProperties
public class TFChatLine {
    public String User1Name;
    public String User1Uid;
    public String User2Name;
    public String User2Uid;
    public Map<String, TFChatMessage> messages = new HashMap<>();

    public TFChatLine(){
    }

    public TFChatLine (String User1Name, String User1Uid, String User2Name, String User2Uid) {
        this.User1Name = User1Name;
        this.User1Uid = User1Uid;
        this.User2Name = User2Name;
        this.User2Uid = User2Uid;
    }
}
