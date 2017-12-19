package compsci290.edu.duke.tutorfire.DataClasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by harrisonlundberg on 4/14/17.
 *
 * This class encapsulates a single course: a subject and course number. It is parcelable
 * so that it can be passed along with an intent between activities.
 */

public class Course implements Parcelable {
    private String mSubject;
    private String mCourse;

    public Course(String subject, String course) {
        mSubject = subject;
        mCourse = course;
    }

    protected Course(Parcel in) {
        mSubject = in.readString();
        mCourse = in.readString();
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public String getSubject() {
        return mSubject;
    }

    public String getCourse() {
        return mCourse;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSubject);
        dest.writeString(mCourse);
    }

    /**
     * This method is used when we want to display a given course in the UI.
     *
     * @return A string representation of this Course object
     */
    public String toString(){
        return String.format("%s %s", mSubject, mCourse);
    }

    /**
     * This method is used to determine whether or not two Course objects represent the same course.
     * It is called in ProfileActivity when we determine if the given tutor already teaches the
     * student in the specified course.
     *
     * @param o The object to which this Course is being compared.
     * @return Whether or not two Course objects are equal.
     */
    @Override
    public boolean equals(Object o){
        if (!(o instanceof Course)) {
            return false;
        }
        Course c = (Course) o;
        if(this.mSubject.equals(c.getSubject()) && this.mCourse.equals(c.getCourse())){
            return true;
        }
        return false;
    }

}
