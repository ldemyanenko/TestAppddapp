package ldemyanenko.com.testappddapp.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ldemyanenko.com.testappddapp.R;
import ldemyanenko.com.testappddapp.dto.Course;
import ldemyanenko.com.testappddapp.dto.Student;


public class CoursesDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Button ok;
    private ListView listView;
    private CourseListAdapter adapter;
    List<Course> courses=new ArrayList<Course>();
    private TextView average;

    public CoursesDialog(Activity a) {
        super(a);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.courses_dialog_title);
        setContentView(R.layout.cources_dialog);
        ok = (Button) findViewById(R.id.btn_ok);
        listView = (ListView) findViewById(R.id.course_list);
        adapter = new CourseListAdapter(getContext(), courses);
        listView.setAdapter(adapter);
        ok.setOnClickListener(this);
        average = (TextView) findViewById(R.id.average);
        setAverage();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    public void setStudent(Student student) {
        courses.clear();
        courses.addAll(student.getCourses());
        if(adapter!=null){
            adapter.notifyDataSetChanged();
            setAverage();
        }

    }

    private void setAverage() {
        float averageTemp=0;
        int courseCount=0;
        for(Course course:courses){
            averageTemp=averageTemp+course.getMark();
            courseCount++;
        }
        average.setText(String.valueOf(courseCount==0?0:averageTemp/courseCount));
    }
}