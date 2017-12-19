package compsci290.edu.duke.tutorfire.DataClasses;

import android.os.Parcelable;

import java.util.List;

/**
 * Created by harrisonlundberg on 4/27/17.
 *
 * This interface is implemented by Student and Tutor. It allows for both to be passed into the
 * ProfileActivity in exactly the same way. All of these methods must be implemented by the
 * subclasses, however, not all return non-null results if they are not applicable. For example,
 * calling getAverageRating() on a Student object simply returns null.
 */

public interface Person extends Parcelable {
    String getUid();
    String getName();
    String getPhotoUrl();
    String getChatHash();
    List<Course> getCourseList();
    void addCourse(Course course);
    Integer getRating();
    Double getAverageRating();
}
