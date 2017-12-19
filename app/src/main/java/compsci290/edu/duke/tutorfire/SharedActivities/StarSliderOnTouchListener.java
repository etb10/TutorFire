package compsci290.edu.duke.tutorfire.SharedActivities;

import android.graphics.Rect;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by alex_boldt on 4/29/17.
 */

public class StarSliderOnTouchListener implements View.OnTouchListener {

    final ProfileActivity profile;

    /**
     * Set the context this listener will write to
     * @param profile ProfileActivity
     */
    StarSliderOnTouchListener(ProfileActivity profile){
        this.profile = profile;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        //get linear layout where stars are held
        LinearLayout layout = (LinearLayout) v;

        for(int i =0; i< layout.getChildCount(); i++)
        {
            View view = layout.getChildAt(i); //get star
            //get the bounding rectangle around the star
            Rect outRect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            //if the motion event (the touch) falls within the bounding rectangle, fill the stars up
            //to and including that star
            if(outRect.contains((int)event.getX(), (int)event.getY()))
            {
                profile.fillStars(i + 1);
                profile.currentUserRating = i + 1;
                profile.isRatingSet = true;
                return true;
            }
        }
        return false;
    }

}
