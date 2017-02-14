package ldemyanenko.com.testappddapp.db;


import android.content.Context;

import java.util.Collection;
import java.util.List;

import ldemyanenko.com.testappddapp.dto.Course;
import ldemyanenko.com.testappddapp.dto.Student;

public interface DBInterface {


    void putStudentArray(Student[] students,Catchable load);

    List<String> getAllDistinctCourses();

    Collection<? extends Student> getStudentList(int page);
    Collection<? extends Student> getFilteredStudentList(int page, String course, int mark);

    List<Course> getCoursesByStudent(Student student);

    interface Catchable{
      void whenCatch();
  }
}

