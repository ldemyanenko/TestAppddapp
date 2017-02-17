package ldemyanenko.com.testappddapp.db.ormlite;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.support.DatabaseConnection;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ldemyanenko.com.testappddapp.db.DBInterface;
import ldemyanenko.com.testappddapp.db.ormlite.dao.StudentDAO;
import ldemyanenko.com.testappddapp.db.sqlite.SQLiteDB;
import ldemyanenko.com.testappddapp.dto.Course;
import ldemyanenko.com.testappddapp.dto.Student;

public class OrmLiteDB implements DBInterface {

    private static final String TAG = OrmLiteDB.class.getSimpleName();;

    public OrmLiteDB(Context context){
        HelperFactory.setHelper(context);
    }
    @Override
    public void putStudentArray(Student[] students, Catchable load) {
        final SQLiteDatabase db = HelperFactory.getHelper().getWritableDatabase();
        db.beginTransaction();
        try {
            StudentDAO studentDAO = HelperFactory.getHelper().getStudentDAO();
            for(Student student:students){
                try {
                    studentDAO.createOrUpdate(student);
                } catch (SQLException e) {
                    Log.e(TAG,e.getMessage());

                }
            }
            db.setTransactionSuccessful();
            load.whenCatch();

        } catch (SQLException e) {
            Log.e(TAG,e.getMessage());
        }
        finally {
            db.endTransaction();
        }

    }

    @Override
    public List<String> getAllDistinctCourses() {
        try {
            List<Course> courses = HelperFactory.getHelper().getCourseDAO().getCourseListDistinct();
            List<String> spinnerArray = new ArrayList<String>();
            for (Course item : courses) {
                spinnerArray.add(item.getName());
            }
            return spinnerArray;
        } catch (SQLException e) {
            Log.e(TAG,e.getMessage());
        }
        return null;
    }

    @Override
    public Collection<? extends Student> getStudentList(int page) {
        try {
            return HelperFactory.getHelper().getStudentDAO().getStudentList(page, SQLiteDB.PAGE_SIZE);
        } catch (SQLException e) {
            Log.e(TAG,e.getMessage());
        }
        return null;
    }

    @Override
    public Collection<? extends Student> getFilteredStudentList(int page, String course, int mark) {
        try {
            if(course!=null) {
                return HelperFactory.getHelper().getStudentDAO().getStudentFilteredList(page, SQLiteDB.PAGE_SIZE, course, mark);
            } else{
                return HelperFactory.getHelper().getStudentDAO().getStudentList(page, SQLiteDB.PAGE_SIZE);
            }
        } catch (SQLException e) {
            Log.e(TAG,e.getMessage());
        }
        return null;
    }

    @Override
    public List<Course> getCoursesByStudent(Student student) {
        return student.getCourses();
    }

    @Override
    public void close() {
        HelperFactory.releaseHelper();
    }
}
