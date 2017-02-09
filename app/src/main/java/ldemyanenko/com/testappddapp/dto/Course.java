package ldemyanenko.com.testappddapp.dto;


import io.realm.RealmObject;
import io.realm.annotations.Index;

public class Course extends RealmObject {
    @Index
    private String name;
    private int mark;
    private String index;

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
}
