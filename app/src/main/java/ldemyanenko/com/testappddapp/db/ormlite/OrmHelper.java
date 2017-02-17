package ldemyanenko.com.testappddapp.db.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import ldemyanenko.com.testappddapp.db.ormlite.dao.CourseDAO;
import ldemyanenko.com.testappddapp.db.ormlite.dao.StudentDAO;
import ldemyanenko.com.testappddapp.dto.Course;
import ldemyanenko.com.testappddapp.dto.Student;

public class OrmHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = OrmHelper.class.getSimpleName();

    //имя файла базы данных который будет храниться в /data/data/APPNAME/DATABASE_NAME.db
    private static final String DATABASE_NAME ="myappname.db";

    //с каждым увеличением версии, при нахождении в устройстве БД с предыдущей версией будет выполнен метод onUpgrade();
    private static final int DATABASE_VERSION = 3;

    //ссылки на DAO соответсвующие сущностям, хранимым в БД
    private StudentDAO studentDao = null;
    private CourseDAO courseDao = null;

    public OrmHelper(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Выполняется, когда файл с БД не найден на устройстве
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource){
        try
        {
            TableUtils.createTable(connectionSource, Student.class);
            TableUtils.createTable(connectionSource, Course.class);
        }
        catch (SQLException e){
            Log.e(TAG, "error creating DB " + DATABASE_NAME);
            throw new RuntimeException(e);
        }
    }

    //Выполняется, когда БД имеет версию отличную от текущей
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer,
                          int newVer){
        try{
            //Так делают ленивые, гораздо предпочтительнее не удаляя БД аккуратно вносить изменения
            TableUtils.dropTable(connectionSource, Student.class, true);
            TableUtils.dropTable(connectionSource, Course.class, true);
            onCreate(db, connectionSource);
        }
        catch (SQLException e){
            Log.e(TAG,"error upgrading db "+DATABASE_NAME+"from ver "+oldVer);
            throw new RuntimeException(e);
        }
    }

    //синглтон для GoalDAO
    public StudentDAO getStudentDAO() throws SQLException{
        if(studentDao == null){
            studentDao = new StudentDAO(getConnectionSource(), Student.class);
        }
        return studentDao;
    }
    //синглтон для RoleDAO
    public CourseDAO getCourseDAO() throws SQLException{
        if(courseDao == null){
            courseDao = new CourseDAO(getConnectionSource(), Course.class);
        }
        return courseDao;
    }

    //выполняется при закрытии приложения
    @Override
    public void close(){
        super.close();
        studentDao = null;
        courseDao = null;
    }
}