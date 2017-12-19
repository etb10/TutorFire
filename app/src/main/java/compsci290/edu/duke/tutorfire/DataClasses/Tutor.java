package compsci290.edu.duke.tutorfire.DataClasses;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import compsci290.edu.duke.tutorfire.SharedActivities.ProfileActivity;
import compsci290.edu.duke.tutorfire.StudentModeActivities.MyTutorsListActivity;
import compsci290.edu.duke.tutorfire.WebServiceInterface.StudentServices;

/**
 * Created by harrisonlundberg on 4/14/17.
 *
 * This class encapsulates a single Tutor, and is used while in Student mode. It implements
 * the Person and Parcelable interfaces so that Tutor objects can be passed into ProfileActivity.
 */


public class Tutor implements Person {

    private String mUid;
    private String mName;
    private String mPhotoUrl;
    private String mChatHash;
    private Double mAvgRating;
    private Integer mUserRating;

    //This is a list of courses the given tutor teaches the current student
    private List<Course> mCoursesITeachYou;

    public Tutor(String uid, String name, String photo_url, String chat_hash) {
        mUid = uid;
        mName = name;
        mPhotoUrl = photo_url;
        mChatHash = chat_hash;
        mCoursesITeachYou = new ArrayList<>();
    }

    public Tutor(String uid, String name, String photo_url, String chat_hash, Double avg_rating) {
        this(uid, name, photo_url, chat_hash);
        mAvgRating = avg_rating;
    }

    public Tutor(String uid, String name, String photo_url, String chat_hash, Integer student_rating) {
        this(uid, name, photo_url, chat_hash);
        mUserRating = student_rating;
    }

    protected Tutor(Parcel in) {
        mUid = in.readString();
        mName = in.readString();
        mPhotoUrl = in.readString();
        mChatHash = in.readString();
        mUserRating = in.readInt();
        mAvgRating = in.readDouble();
        mCoursesITeachYou = new ArrayList<>();
        int sizeOfList = in.readInt();
        for (int i = 0; i < sizeOfList; i++) {
            Course c = in.readParcelable(Course.class.getClassLoader());
            mCoursesITeachYou.add(c);
        }
    }

    public static final Creator<Tutor> CREATOR = new Creator<Tutor>() {
        @Override
        public Tutor createFromParcel(Parcel in) {
            return new Tutor(in);
        }

        @Override
        public Tutor[] newArray(int size) {
            return new Tutor[size];
        }
    };

    /**
     *
     * @return The uid of the Tutor object
     */
    public String getUid() {
        return mUid;
    }

    /**
     *
     * @return The name of the Tutor object
     */
    public String getName() {
        return mName;
    }

    /**
     *
     * @return The photo url for this Tutor
     */
    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    /**
     *
     * @return The chat hash identifying the chat between the current student and this Tutor
     */
    public String getChatHash() {
        return mChatHash;
    }

    /**
     *
     * @return The list of courses this tutor teaches the current student
     */
    @Override
    public List<Course> getCourseList() {
        return mCoursesITeachYou;
    }

    /**
     *
     * @return The rating the current user previously gave this tutor, null if no previous rating
     */
    @Override
    public Integer getRating() {
        return mUserRating;
    }

    /**
     *
     * @return The average rating of this tutor
     */
    @Override
    public Double getAverageRating() {
        return mAvgRating;
    }


    /**
     * This method is used in GetMyTutorsParser to create a Tutor object.
     *
     * @param c The course to be added to this tutor's list of courses they teach the current student
     */
    public void addCourse(Course c) {
        mCoursesITeachYou.add(c);
    }


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
        if (mUserRating == null){
            dest.writeInt(new Integer(-1));
        }else{
            dest.writeInt(mUserRating);
        }
        if (mAvgRating == null){
            dest.writeDouble(new Double(6));
        }else{
            dest.writeDouble(mAvgRating);
        }
        dest.writeInt(mCoursesITeachYou.size());
        for (Course c : mCoursesITeachYou) {
            dest.writeParcelable(c, flags);
        }
    }
}
