package ldemyanenko.com.testappddapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ldemyanenko.com.testappddapp.dto.Course;
import ldemyanenko.com.testappddapp.dto.Student;


public class DBHelper extends SQLiteOpenHelper {
    private static String StudentCourse_TABLE= "StudentCourse";
    // Labels Table Columns names
    public static final String StudentCourse_KEY_RunningID = "RunningID";
    public static final String StudentCourse_KEY_StudID = "StudentId";
    public static final String StudentCourse_KEY_CourseId = "CourseId";
    public static final String StudentCourse_KEY_Mark = "Mark";
    final String LOG_TAG = "myLogs";


    public DBHelper(Context context) {
        super(context, "myDB", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        Log.e(LOG_TAG, "create table "+ Student.TABLE +"("
                + Student.KEY_Id+" string primary key,"
                + Student.KEY_FName+" text,"
                + Student.KEY_LName+" text,"
                + Student.KEY_BDate+" text"
                + ");");

        db.execSQL("create table "+ Student.TABLE +"("
                + Student.KEY_Id+" string primary key,"
                + Student.KEY_FName+" text,"
                + Student.KEY_LName+" text,"
                + Student.KEY_BDate+" text"
                + ");");

        db.execSQL("create table "+ Course.TABLE +"("
                + Course.KEY_CourseId+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + Course.KEY_Name+" text"
                + ");");

        db.execSQL("CREATE TABLE " + StudentCourse_TABLE  + "("
                + StudentCourse_KEY_RunningID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + StudentCourse_KEY_StudID + " TEXT, "
                + StudentCourse_KEY_CourseId + " TEXT, "
                + StudentCourse_KEY_Mark + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ Student.TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ Course.TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ StudentCourse_TABLE);

        onCreate(db);
    }


    public void deleteStudent(Student student){

    }
    public void insertStudent(SQLiteDatabase db,Student student){
        ContentValues values = new ContentValues();
        values.put(Student.KEY_Id, student.getId());
        values.put(Student.KEY_FName, student.getFirstName());
        values.put(Student.KEY_LName, student.getLastName());
        values.put(Student.KEY_BDate, student.getBirthday());

        db.insert(Student.TABLE, null, values);
        for(Course course:student.getCourses()){
            course.setId(insertCourse(db,course));
            insertStudentCourse(db,course,student.getId());
        }
    }

    private void insertStudentCourse(SQLiteDatabase db,Course course, String studentId) {
        ContentValues values = new ContentValues();
        values.put(StudentCourse_KEY_StudID, studentId);
        values.put(StudentCourse_KEY_CourseId, course.getId());
        values.put(StudentCourse_KEY_Mark, course.getMark());

        // Inserting Row
        db.insert(StudentCourse_TABLE, null, values);
    }

    public int insertCourse(SQLiteDatabase db,Course course){
        int courseId;
        ContentValues values = new ContentValues();
        values.put(Course.KEY_Name, course.getName());

        // Inserting Row
        courseId=(int)db.insert(Course.TABLE, null, values);
        return courseId;

    }
    public void updateStudent(Student student){

    }
    public void insertStudents(SQLiteDatabase db,Student[] students){
        insertStudents(db,Arrays.asList(students));
    }
    public void insertStudents(SQLiteDatabase db,List<Student> students){
        for(Student student:students){
            insertStudent(db,student);
        }
    }
    public List<Course> getCoursesByStudent(Student student){
        String selectQuery = getCourseByStudentSQL(student);
        return getCourses(selectQuery);

    }

    @NonNull
    private String getCourseByStudentSQL(Student student) {
        return " SELECT "  + Course.TABLE.concat(".").concat(Course.KEY_Name)
                + ", " +StudentCourse_TABLE.concat(".").concat(StudentCourse_KEY_Mark)
                + " FROM " + StudentCourse_TABLE
                + " INNER JOIN " + Course.TABLE + " ON " + Course.TABLE.concat(".").concat(Course.KEY_CourseId) + "=  "+StudentCourse_TABLE.concat(".").concat(StudentCourse_KEY_CourseId)
                + " where " + StudentCourse_KEY_StudID + " ='" + student.getId()+"'";
    }

    @NonNull
    private List<Course> getCourses(String selectQuery) {
        List<Course> studentCourseLists = new ArrayList<Course>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();


        Log.d(LOG_TAG, selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                studentCourseLists.add(new Course(cursor.getString(cursor.getColumnIndex(Course.KEY_Name)), cursor.getInt(cursor.getColumnIndex(StudentCourse_KEY_Mark))));
            } while (cursor.moveToNext());
        }

        cursor.close();
        //DatabaseManager.getInstance().closeDatabase();

        return studentCourseLists;
    }

    public List<Student> getStudents(int page, int pageSize) {
       return getStudentsList(getStudentsQuery(page, pageSize));
    }

    public List<Student> getStudentsList(String sqlQuery){
        List<Student> studentLists = new ArrayList<Student>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Log.d(LOG_TAG, sqlQuery);
        Cursor cursor = db.rawQuery(sqlQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                studentLists.add(new Student(cursor.getString(cursor.getColumnIndex(Student.KEY_Id))
                        ,cursor.getString(cursor.getColumnIndex(Student.KEY_FName))
                        , cursor.getString(cursor.getColumnIndex(Student.KEY_LName))
                        ,cursor.getString(cursor.getColumnIndex(Student.KEY_BDate))
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        //DatabaseManager.getInstance().closeDatabase();

        return studentLists;

    }

    @NonNull
    private String getStudentsQuery(int page, int pageSize) {
        return " SELECT "  + Student.TABLE.concat(".").concat(Student.KEY_Id)
                + ", " +Student.TABLE.concat(".").concat(Student.KEY_FName)
                + ", " +Student.TABLE.concat(".").concat(Student.KEY_LName)
                + ", " +Student.TABLE.concat(".").concat(Student.KEY_BDate)
                + " FROM " + Student.TABLE
                + " LIMIT "+pageSize+" OFFSET "+(page)*pageSize;
    }

    public List<Student> getFilteredStudents(int page, int pageSize, String course, int mark){
        return getStudentsList(getFilteringStudentsQuery(page, pageSize, course,mark));
        }

    @NonNull
    private String getFilteringStudentsQuery(int page, int pageSize, String course, int mark) {
        return " SELECT "  + Student.TABLE.concat(".").concat(Student.KEY_Id)
                + ", " +Student.TABLE.concat(".").concat(Student.KEY_FName)
                + ", " +Student.TABLE.concat(".").concat(Student.KEY_LName)
                + ", " +Student.TABLE.concat(".").concat(Student.KEY_BDate)
                + " FROM " + Student.TABLE
                + " where "
                + Student.TABLE.concat(".").concat(Student.KEY_Id) +" in (SELECT "+StudentCourse_TABLE.concat(".").concat(StudentCourse_KEY_StudID)+" from "+StudentCourse_TABLE+" inner join "+Course.TABLE+" on "+StudentCourse_TABLE.concat(".").concat(StudentCourse_KEY_CourseId)+"="+Course.TABLE.concat(".").concat(Course.KEY_CourseId)
                + " where "+Course.TABLE.concat(".").concat(Course.KEY_Name)+"='"+course+"' and "+StudentCourse_TABLE.concat(".").concat(StudentCourse_KEY_Mark)+"="+mark+" ) "
                + " LIMIT "+pageSize+" OFFSET "+(page)*pageSize;
    }

    public List<String> getAllDistinctCourses() {
        String selectQuery = getAllDistinctCoursesSQL();
        List<Course> courses = getCourses(selectQuery);
        List<String> spinnerArray = new ArrayList<String>();
        for (Course item : courses) {
            spinnerArray.add(item.getName());
        }
        return spinnerArray;
    }

    private String getAllDistinctCoursesSQL() {
        return " SELECT DISTINCT "  + Course.KEY_Name+" ,0 "+StudentCourse_KEY_Mark+" FROM "
                +  Course.TABLE ;
    }


}
