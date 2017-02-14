package ldemyanenko.com.testappddapp.dto;


import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import ldemyanenko.com.testappddapp.dto.Course;

public class Student extends RealmObject{
    public static final String TABLE = "students";
    public static final String KEY_FName = "FirstName";
    public static final String KEY_LName = "LastName";
    public static final String KEY_BDate = "BirthDay";
    public static String KEY_Id="Id";

    private String id;
    private String firstName;
    private String lastName;
    private String birthday;
    RealmList<Course> courses;

    public Student(String id, String fName, String lName, String bDay) {
        this.id=id;
        this.firstName=fName;
        this.lastName=lName;
        this.birthday=bDay;
    }
    public Student(){

    }
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
