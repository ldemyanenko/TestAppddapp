package ldemyanenko.com.testappddapp.dto;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import ldemyanenko.com.testappddapp.db.sqlite.DBHelper;

@DatabaseTable(tableName = "course")
public class Course extends RealmObject {
    public static final String TABLE = "course";
    public static final String KEY_CourseId = "CourseId";
    public static final String KEY_Name = "Name";
    @Index
    @DatabaseField(columnName= KEY_Name)
    private String name;
    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private Student student;

    @DatabaseField(dataType = DataType.INTEGER,columnName= DBHelper.StudentCourse_KEY_Mark)
    private int mark;
    private String index;
    @DatabaseField(generatedId =true,dataType = DataType.INTEGER,columnName= KEY_CourseId)
    private int id;

    public Course(String name, int mark) {
        this.name=name;
        this.mark=mark;
    }
    public Course(){

    }

    public String getName() {
        return name;
    }

    public int getMark() {
        return mark;
    }

    public void setIndex() {
        this.index=createIndex(name,mark);

    }
    public static String createIndex(String name, int mark){
        return name+"__"+mark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
