package ldemyanenko.com.testappddapp.dto;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import ldemyanenko.com.testappddapp.dto.Course;

@DatabaseTable(tableName = "students")
public class Student extends RealmObject{
    public static final String TABLE = "students";
    public static final String KEY_FName = "FirstName";
    public static final String KEY_LName = "LastName";
    public static final String KEY_BDate = "BirthDay";
    public static final String KEY_Id="Id";
    @DatabaseField(id = true, columnName=KEY_Id)
    private String id;
    @DatabaseField(columnName=KEY_FName)
    private String firstName;
    @DatabaseField(columnName=KEY_LName)
    private String lastName;
    @DatabaseField(columnName=KEY_BDate)
    private String birthday;
    @ForeignCollectionField(eager = true)
    @Ignore
    private  Collection<Course> coursesORM;
    private RealmList<Course> courses;

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
        return courses!=null?courses:new ArrayList<>(coursesORM);
    }

    public void refreshCourses() {
        coursesORM=courses;
    }
}
