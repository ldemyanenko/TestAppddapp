package ldemyanenko.com.testappddapp.ui;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import ldemyanenko.com.testappddapp.R;
import ldemyanenko.com.testappddapp.dto.Student;


public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ViewHolder> {
    private final List<Student> students;
    CoursesDialog dialog;

    public StudentListAdapter(List<Student> students, Activity activity) {
        this.students=students;
        dialog =new CoursesDialog(activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Student student = students.get(position);
        holder.student = student;
        holder.name.setText(student.getFirstName()+" "+student.getLastName());
        holder.date.setText(student.getBirthday());
        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setStudent(student);
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView name;
        public final TextView date;
        private final ImageView info;
        public Student student;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = (TextView) view.findViewById(R.id.name);
            date = (TextView) view.findViewById(R.id.date);
            info = (ImageView) view.findViewById(R.id.info);
        }
    }
}
