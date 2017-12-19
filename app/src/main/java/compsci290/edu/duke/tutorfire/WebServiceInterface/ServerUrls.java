package compsci290.edu.duke.tutorfire.WebServiceInterface;

/**
 * Created by harrisonlundberg on 4/13/17.
 *
 * This class acts as a container, holding all of the urls needed to send requests
 * to our web service.
 */

public class ServerUrls {
    private ServerUrls() {}

    public static final String BASE = "http://colab-sbx-96.oit.duke.edu/TutorFire/";

    public static final String ADD_NEW_USER = BASE + "AddNewUser.php";

    public static final String ADD_TO_MY_COURSES = BASE + "AddToMyCourses.php";

    public static final String ADD_TO_MY_TIMES = BASE + "AddToMyTimes.php";

    public static final String ADD_TO_MY_TUTORS = BASE + "AddToMyTutors.php";

    public static final String FIND_TUTORS = BASE + "FindTutors.php";

    public static final String GET_MY_COURSES = BASE + "GetMyCourses.php";

    public static final String GET_MY_STUDENTS = BASE + "GetMyStudents.php";

    public static final String GET_MY_TIMES = BASE + "GetMyTimes.php";

    public static final String GET_MY_TUTORS = BASE + "GetMyTutors.php";

    public static final String REMOVE_FROM_MY_COURSES = BASE + "RemoveFromMyCourses.php";

    public static final String REMOVE_FROM_MY_TIMES = BASE + "RemoveFromMyTimes.php";

    public static final String REMOVE_FROM_MY_TUTORS = BASE + "RemoveFromMyTutors.php";

    public static final String SET_TUTOR_RATING = BASE + "SetTutorRating.php";

    public static final String GET_MY_AVERAGE_RATING = BASE + "GetMyAverageRating.php";
}
