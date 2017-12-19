package compsci290.edu.duke.tutorfire.TutorModeActivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
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
 * Adapted from GelatoRVAdapter by ola on 2/23/17.
 *
 * Adapts CardViews to Recycler View representing Courses
 */

public class CourseRVAdapter extends RecyclerView.Adapter<CourseRVAdapter.CourseViewHolder> {

    public static class CourseViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView courseName;
        ImageView delButton;

        CourseViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            courseName = (TextView)itemView.findViewById(R.id.course_RV_name);
            delButton = (ImageView) itemView.findViewById(R.id.fab_delete);
        }
    }


    public List<Course> courseList;
    private MediaPlayer mDeletePlayer;

    /**
     * Basic constructor
     * @param list
     */
    CourseRVAdapter(List<Course> list){
        this.courseList = list;
        Log.d("RV", "Creating Course List of Length " + list.size());
    }

    /**
     * onAttachedToRecyclerView - required for RV
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * Inflate the course object view with the appropriate viewGroup
     * @param viewGroup
     * @param i - position
     * @return CourseViewHolder - inner class Holder, populating the Card
     */
    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.course, viewGroup, false);
        CourseViewHolder cvh = new CourseViewHolder(v);
        return cvh;
    }

    /**
     * sets text and onClickListener for course's delete button
     * @param courseViewHolder - ViewHolder that represents layout file
     * @param i - position
     */
    @Override
    public void onBindViewHolder(final CourseViewHolder courseViewHolder, final int i) {
        Log.d("RV","name id: " + courseViewHolder.itemView.findViewById(R.id.course_RV_name));
        courseViewHolder.courseName.setText(courseList.get(i).toString());
        courseViewHolder.delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete(v.getContext(), i);
            }
        });
    }

    /**
     * getItemCount
     * @return - length of courseList
     */
    @Override
    public int getItemCount() {
        return courseList.size();
    }

    /**
     * Dialog for deleting a course.
     * Confirms delete, then calls TutorServices to remove from DB
     * @param context
     * @param i - position
     */
    private void confirmDelete(final Context context, final int i) {
        new AlertDialog.Builder(context)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
                        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

                        String courseName = courseList.get(i).toString();
                        TutorServices.removeFromMyCourses(context, mFirebaseUser.getUid(), courseList.get(i));
                        Log.d("RV", "Deleted a course");
                        Toast.makeText(context, "Removed " + courseName, Toast.LENGTH_SHORT).show();
                        // TODO Lin Lin add noise for removal
                        mDeletePlayer = MediaPlayer.create(context, R.raw.poke5);
                        mDeletePlayer.start();


                        Intent i = new Intent(context, TutorClassesListActivity.class);
                        context.startActivity(i);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * release MediaPlayer
     */
    public void releasePlayer() {
        if (mDeletePlayer != null) {
            mDeletePlayer.release();
            mDeletePlayer = null;
        }
    }

}


