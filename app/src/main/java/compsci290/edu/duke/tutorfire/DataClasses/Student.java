package compsci290.edu.duke.tutorfire.DataClasses;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harrisonlundberg on 4/14/17.
 *
 * This class encapsulates a single Student, and is used while in Tutor mode. It implements
 * the Person and Parcelable interfaces so that Student objects can be passed into ProfileActivity.
 */

public class Student implements Person {
    private String mUid;
    private String mName;
    private String mPhotoUrl;
    private String mChatHash;
    // this is the list of courses taught to the student by the tutor (user)
    private List<Course> mCoursesYouTeachMe;

    public Student(String uid, String name, String photo_url, String chat_hash) {
        mUid = uid;
        mName = name;
        mPhotoUrl = photo_url;
        mChatHash = chat_hash;
        mCoursesYouTeachMe = new ArrayList<>();
    }

    protected Student(Parcel in) {
        mUid = in.readString();
        mName = in.readString();
        mPhotoUrl = in.readString();
        mChatHash = in.readString();
        mCoursesYouTeachMe = new ArrayList<>();
        int sizeOfList = in.readInt();
        for (int i = 0; i < sizeOfList; i++) {
            Course c = in.readParcelable(Course.class.getClassLoader());
            mCoursesYouTeachMe.add(c);
        }
    }

    /**
     *
     * @return The uid of the student object
     */
    @Override
    public String getUid() {
        return mUid;
    }

    /**
     *
     * @return The name of the student object
     */
    @Override
    public String getName() {
        return mName;
    }

    /**
     *
     * @return The photo url of the student object
     */
    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    /**
     *
     * @return The chat hash identifying the chat between the current user and this student
     */
    public String getChatHash() {
        return mChatHash;
    }

    /**
     *
     * @return The list of courses the current tutor teaches to this student
     */
    @Override
    public List<Course> getCourseList() {
        return mCoursesYouTeachMe;
    }

    /**
     * This method has to be implemented because it is part of the Person interface, but it
     * should never be called.
     *
     * @return Always null
     */
    @Override
    public Integer getRating() {
        return null;
    }

    /**
     * This method has to be implemented because it is part of the Person interface, but it
     * should never be called.
     *
     * @return Always null
     */
    @Override
    public Double getAverageRating() {
        return null;
    }

    /**
     * This method is used in GetMyStudentsParser to create a Student object.
     *
     * @param c The course to be added to this student's list of courses taught by the current tutor
     */
    public void addCourse(Course c) {
        mCoursesYouTeachMe.add(c);
    }

    /**
     * This method is called when we need to get the list of Courses that the tutor teaches this
     * student.
     *
     * @return The list of courses the current tutor teaches this student
     */
    public List<Course> getCourses() {
        return mCoursesYouTeachMe;
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUid);
        dest.writeString(mName);
        dest.writeString(mPhotoUrl);
        dest.writeString(mChatHash);
        dest.writeInt(mCoursesYouTeachMe.size());
        for (Course c : mCoursesYouTeachMe) {
            dest.writeParcelable(c, flags);
        }
    }
}
