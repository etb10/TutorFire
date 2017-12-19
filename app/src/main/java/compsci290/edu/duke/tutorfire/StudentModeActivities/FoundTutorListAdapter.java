package compsci290.edu.duke.tutorfire.StudentModeActivities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import compsci290.edu.duke.tutorfire.DataClasses.Course;
import compsci290.edu.duke.tutorfire.DataClasses.Tutor;
import compsci290.edu.duke.tutorfire.R;
import compsci290.edu.duke.tutorfire.SharedActivities.ProfileActivity;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by harrisonlundberg on 4/28/17.
 * RecyclerView Adapter for RecyclerView of matching tutors in FoundTutorsActivity
 */

public class FoundTutorListAdapter extends RecyclerView.Adapter<FoundTutorListAdapter.TutorHolder> {

    private Context mContext;
    private List<Tutor> mFoundTutors;
    private Course mCourse;

    public class TutorHolder extends RecyclerView.ViewHolder{

        private CardView mCV;
        private CircleImageView mProfileImageView;
        private TextView mNameView;
        private TextView mAverageRating;

        public TutorHolder(View itemView) {
            super(itemView);
            mCV = (CardView) itemView.findViewById(R.id.found_tutor_cv);
            mProfileImageView = (CircleImageView) itemView.findViewById(R.id.foundTutorProfileImageView);
            mNameView = (TextView) itemView.findViewById(R.id.found_tutor_RV_name);
            mAverageRating = (TextView) itemView.findViewById(R.id.found_tutor_RV_average_rating);
        }
    }

    public FoundTutorListAdapter(Context c, List<Tutor> tutorList, Course course) {
        mContext = c;
        mFoundTutors = tutorList;
        mCourse = course;
    }

    @Override
    public TutorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.found_tutor_rv, parent, false);
        return new TutorHolder(v);
    }

    @Override
    public void onBindViewHolder(TutorHolder holder, int position) {
        //Set all the appropriate fields of the holder with those of specified tutor

        final Tutor currentTutor = mFoundTutors.get(position);

        holder.mNameView.setText(currentTutor.getName());

        Double avgRating = currentTutor.getAverageRating();
        String avgRatingStr;
        if (avgRating == null) {
            avgRatingStr = "Not yet rated";
        } else {
            avgRatingStr = String.format("Average rating of %.1f", currentTutor.getAverageRating());
        }
        holder.mAverageRating.setText(avgRatingStr);

        // Glide to get picture to load
        String currentPhotoUrl = currentTutor.getPhotoUrl();
        if (ProfileActivity.isPhotoUrlValid(currentPhotoUrl)) { //order matters
            // Use Glide to Bring in profile picture with bounded size
            Glide.with(mContext)
                    .load(currentPhotoUrl)
                    .override(200,200)
                    .fitCenter()
                    .into(holder.mProfileImageView);
        }

        //Set on click listener
        holder.mCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Send to new activity and pass it both the tutor object and the course object
                Intent i = new Intent(mContext, ProfileActivity.class);
                Resources res = mContext.getResources();
                i.putExtra(res.getString(R.string.PersonDataProfileIntentKey), currentTutor);
                i.putExtra(res.getString(R.string.CourseIntentKey), mCourse);
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mFoundTutors.size();
    }
}
