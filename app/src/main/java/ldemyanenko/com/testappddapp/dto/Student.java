package ldemyanenko.com.testappddapp.dto;


import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import ldemyanenko.com.testappddapp.dto.Course;

public class Student extends RealmObject{

    private String id;
    private String firstName;
    private String lastName;
    private String birthday;
    RealmList<Course> courses;

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public List<Course> getCourses() {
        return courses;
    }
}
