package compsci290.edu.duke.tutorfire.StudentModeActivities;

import java.util.List;

import compsci290.edu.duke.tutorfire.DataClasses.Tutor;

/**
 * Created by alex_boldt on 4/19/17.
 * Interface for Activities that Receive lists of Tutors
 */

public interface TutorListReceiver {

    void receiveTutorList(List<Tutor> tutors);
}
