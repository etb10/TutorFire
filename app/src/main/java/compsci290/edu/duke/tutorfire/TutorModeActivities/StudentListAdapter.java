package compsci290.edu.duke.tutorfire.TutorModeActivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import compsci290.edu.duke.tutorfire.ChatActivity;
import compsci290.edu.duke.tutorfire.DataClasses.Course;
import compsci290.edu.duke.tutorfire.DataClasses.Student;
import compsci290.edu.duke.tutorfire.R;
import compsci290.edu.duke.tutorfire.SharedActivities.ProfileActivity;
import compsci290.edu.duke.tutorfire.WebServiceInterface.TutorServices;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ola on 2/23/17.
 */

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.StudentViewHolder> {

    public static class StudentViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView studentName;
        TextView courseNames;
        CircleImageView profileImageView;
        ImageView chatButton;

        StudentViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            profileImageView = (CircleImageView) itemView.findViewById(R.id.studentProfileImageView);
            studentName = (TextView)itemView.findViewById(R.id.student_RV_name);
            courseNames = (TextView)itemView.findViewById(R.id.student_RV_courses);
            chatButton = (ImageView) itemView.findViewById(R.id.studentProfile_chatButton);
        }
    }

    private static final String sTAG = "StudentListAdapter";
    public List<Student> studentList;
    public Context mContext;

    StudentListAdapter(List<Student> list){
        this.studentList = list;
        Log.d("RV", "Creating Course List of Length " + list.size());

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        View v = LayoutInflater.from(mContext).inflate(R.layout.student_rv, viewGroup, false);
        StudentViewHolder svh = new StudentViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(final StudentViewHolder studentViewHolder, final int i) {
        Log.d("RV","name id: " + studentViewHolder.itemView.findViewById(R.id.student_RV_name));
        studentViewHolder.studentName.setText(studentList.get(i).getName());

        // populate Student object display with courses as list, or if null just blank string
        int strLng = studentList.get(i).getCourses().size();
        if(strLng == 1) {
            Log.d("StudentsRV", "courseName was length " + strLng + "so print with no s");
            studentViewHolder.courseNames.setText("" + strLng + " Course");
        } else {
            Log.d("StudentsRV", "courseName was length " + strLng + "so print with an s");
            studentViewHolder.courseNames.setText("" + strLng + " Courses");
        }

        // Set chat icon to either GONE or visible with listener
        final String chatHash = studentList.get(i).getChatHash();
        if (chatHash == null || "".equals(chatHash) || !chatHash.startsWith("-")){
            studentViewHolder.chatButton.setVisibility(View.GONE);

        } else{
            // add click listener to chatButton
            studentViewHolder.chatButton.setOnClickListener(new View.OnClickListener() {
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
        String mPhotoUrl = studentList.get(i).getPhotoUrl();
        if (mPhotoUrl != null && mPhotoUrl.length() > 0 && mPhotoUrl.startsWith("https://")) { //order matters
            // Use Glide to Bring in profile picture with bounded size
            Glide.with(mContext)
                    .load(mPhotoUrl)
                    .override(200,200)
                    .fitCenter()
                    .into(studentViewHolder.profileImageView);
        }

        studentViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStudentProfile(i);
            }
        });

    }

    private void openStudentProfile(int i) {
        Resources res = mContext.getResources();
        Intent intent = new Intent(mContext, ProfileActivity.class);
        intent.putExtra(res.getString(R.string.PersonDataProfileIntentKey) , studentList.get(i));
        intent.putExtra(res.getString(R.string.IsTutorProfileIntentKey), false);
        //intent.putExtra(res.getString(R.string.PersonDataProfileIntentKey), studentList.get(i));
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }


}