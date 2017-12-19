package compsci290.edu.duke.tutorfire.SharedActivities;

import android.content.Context;

/**
 * Created by harrisonlundberg on 4/29/17.
 * Interface for Activities that receive Average Tutor Ratings
 */

public interface AverageTutorRatingReceiver {
    void deliverAverageRating(Double rating);
}
