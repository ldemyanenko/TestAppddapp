package ldemyanenko.com.testappddapp.dto;


import io.realm.RealmObject;
import io.realm.annotations.Index;

public class Course extends RealmObject {
    public static final String TABLE = "course";
    public static final String KEY_CourseId = "CourseId";
    public static final String KEY_Name = "Name";
    @Index
    private String name;
    private int mark;
    private String index;
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
}
