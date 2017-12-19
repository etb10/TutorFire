package compsci290.edu.duke.tutorfire.SharedActivities;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import compsci290.edu.duke.tutorfire.DataClasses.Course;

/**
 * Created by Edwin on 4/16/17.
 */

public class CourseListAdapter extends BaseAdapter {

    private Context mContext;
    private Course[] mCourseArr;

    public CourseListAdapter(Context c, List<Course> courseList) {
        mContext = c;
        mCourseArr = courseList.toArray(new Course[courseList.size()]);
    }

    @Override
    public int getCount() {
        return mCourseArr.length;
    }

    @Override
    public Object getItem(int position) {
        return mCourseArr[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        Course course = mCourseArr[position];
        if (convertView == null) {
            textView = new TextView(mContext);
        } else {
            textView = (TextView) convertView;
        }

        textView.setText(course.toString());
        return textView;
    }

}
