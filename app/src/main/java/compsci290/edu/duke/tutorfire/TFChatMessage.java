package compsci290.edu.duke.tutorfire;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by alex_boldt on 4/10/17.
 * A JSON object for entry into the firebase databse
 */

@IgnoreExtraProperties
public class TFChatMessage {
    public String text;
    public String name;
    public String photoUrl;
    public String imageUrl;
    public String id;

    public TFChatMessage() {
    }

    public TFChatMessage(String text, String name, String photoUrl, String imageUrl) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.imageUrl = imageUrl;
    }
}