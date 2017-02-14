package ldemyanenko.com.testappddapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.Collection;
import java.util.List;

import ldemyanenko.com.testappddapp.dto.Course;
import ldemyanenko.com.testappddapp.dto.Student;



public class SQLiteDB implements DBInterface {
    public static final int PAGE_SIZE = 20;
    private final DBHelper dbHelper;

    public SQLiteDB(Context context){
        dbHelper = new DBHelper(context);
        DatabaseManager.initializeInstance(dbHelper);
    }
    @Override
    public void putStudentArray(final Student[] students, final Catchable load) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.beginTransaction();
        dbHelper.insertStudents(db,students);
        db.setTransactionSuccessful();
        db.endTransaction();
        load.whenCatch();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//
//            }
//        }).start();

       // db.close();
    }

    @Override
    public List<String> getAllDistinctCourses() {
        return dbHelper.getAllDistinctCourses();
    }

    @Override
    public Collection<? extends Student> getStudentList(int page) {
        return dbHelper.getStudents(page,PAGE_SIZE);
    }

    @Override
    public Collection<? extends Student> getFilteredStudentList(int page, String course, int mark) {
        return course==null?dbHelper.getStudents(page, PAGE_SIZE):dbHelper.getFilteredStudents(page, PAGE_SIZE,course,mark);
    }

    @Override
    public List<Course> getCoursesByStudent(Student student) {
        return dbHelper.getCoursesByStudent(student);
    }
}
