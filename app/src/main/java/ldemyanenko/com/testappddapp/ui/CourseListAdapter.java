package ldemyanenko.com.testappddapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import ldemyanenko.com.testappddapp.R;
import ldemyanenko.com.testappddapp.dto.Course;

public class CourseListAdapter extends ArrayAdapter<Course> {
    public CourseListAdapter( Context context,List<Course> courses) {
        super(context, R.layout.course_list_item,courses);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Course course = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.course_list_item, null);
        }
        ((TextView) convertView.findViewById(R.id.name))
                .setText(course.getName());
        ((TextView) convertView.findViewById(R.id.mark))
                .setText(String.valueOf(course.getMark()));
        return convertView;
    }
}
