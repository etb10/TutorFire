package compsci290.edu.duke.tutorfire.SharedActivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import compsci290.edu.duke.tutorfire.DataClasses.Course;
import compsci290.edu.duke.tutorfire.R;
import compsci290.edu.duke.tutorfire.WebServiceInterface.TutorServices;

/**
 * Created by Alex Boldt on 4/27/17.
 * RecyclerView adapter for list of courses in Profiles
 */

public class ProfileCourseRVAdapter extends RecyclerView.Adapter<ProfileCourseRVAdapter.ProfileCourseViewHolder> {

    public static class ProfileCourseViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView courseName;

        ProfileCourseViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.profileCourseCardView);
            courseName = (TextView)itemView.findViewById(R.id.course_RV_name);
        }
    }


    public List<Course> courseList;

    public ProfileCourseRVAdapter(List<Course> list){
        this.courseList = list;
        Log.d("RV", "Creating Course List of Length " + list.size());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ProfileCourseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.profile_course, viewGroup, false);
        ProfileCourseViewHolder cvh = new ProfileCourseViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(final ProfileCourseViewHolder courseViewHolder, final int i) {
        Log.d("RV","name id: " + courseViewHolder.itemView.findViewById(R.id.course_RV_name));
        courseViewHolder.courseName.setText(courseList.get(i).toString());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

}


