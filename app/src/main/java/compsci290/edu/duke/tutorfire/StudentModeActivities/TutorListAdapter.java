package compsci290.edu.duke.tutorfire.StudentModeActivities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import compsci290.edu.duke.tutorfire.ChatActivity;
import compsci290.edu.duke.tutorfire.DataClasses.Tutor;
import compsci290.edu.duke.tutorfire.R;
import compsci290.edu.duke.tutorfire.SharedActivities.ProfileActivity;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by alex_boldt on 4/14/17.
 * Adapter for list of Tutors in MyTutors
 */

public class TutorListAdapter extends RecyclerView.Adapter<TutorListAdapter.TutorViewHolder> {

    private Context mContext;
    private List<Tutor> mTutors;

    public class TutorViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView tutorName;
        TextView courseNames;
        CircleImageView profileImageView;
        ImageView chatButton;

        public TutorViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.tutor_cv);
            tutorName = (TextView) itemView.findViewById(R.id.tutor_RV_name);
            courseNames = (TextView) itemView.findViewById(R.id.tutor_RV_courses);
            profileImageView = (CircleImageView) itemView.findViewById(R.id.tutorProfileImageView);
            chatButton = (ImageView) itemView.findViewById(R.id.tutorProfile_chatButton);
        }
    }

    public TutorListAdapter(Context c, List<Tutor> tutorList){
        mContext = c;
        mTutors = tutorList;
    }


    @Override
    public TutorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.tutor_rv, parent, false);
        return new TutorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TutorViewHolder holder, int position) {
        //Update all the fields in holder to correspond to data in position
        final Tutor currentTutor = mTutors.get(position);

        holder.tutorName.setText(currentTutor.getName());

        int numCourses = currentTutor.getCourseList().size();
        if (numCourses == 1) {
            holder.courseNames.setText("1 Course");
        } else {
            holder.courseNames.setText("" + numCourses + " Courses");
        }

        // Create chat button on listView
        final String chatHash = currentTutor.getChatHash();
        // if there is no chat hash
        if (!ProfileActivity.isChatValid(chatHash)){
            // hide button
            holder.chatButton.setVisibility(View.GONE);
        } else{
            // chat hash is valid, so create on Click Listener
            holder.chatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Open chat with other user
                    Intent chat_intent = new Intent(mContext, ChatActivity.class);
                    chat_intent.putExtra(mContext.getResources().getString(R.string.ChatHashIntentKey), chatHash);
                    mContext.startActivity(chat_intent);
                }
            });
        }



        // Glide to get picture to load
        String currentPhotoUrl = currentTutor.getPhotoUrl();
        if (ProfileActivity.isPhotoUrlValid(currentPhotoUrl)) { //order matters
            // Use Glide to Bring in profile picture with bounded size
            Glide.with(mContext)
                    .load(currentPhotoUrl)
                    .override(200,200)
                    .fitCenter()
                    .into(holder.profileImageView);
        }


        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTutorProfile(currentTutor);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mTutors.size();
    }

    private void openTutorProfile(Tutor t) {
        Resources res = mContext.getResources();
        Intent intent = new Intent(mContext, ProfileActivity.class);
        intent.putExtra(res.getString(R.string.PersonDataProfileIntentKey) , t);
        intent.putExtra(res.getString(R.string.IsTutorProfileIntentKey), true);
        //intent.putExtra(res.getString(R.string.PersonDataProfileIntentKey), studentList.get(i));
        mContext.startActivity(intent);
    }

}
