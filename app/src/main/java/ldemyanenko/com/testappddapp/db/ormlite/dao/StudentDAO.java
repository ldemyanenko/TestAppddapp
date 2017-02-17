package ldemyanenko.com.testappddapp.db.ormlite.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import ldemyanenko.com.testappddapp.db.ormlite.HelperFactory;
import ldemyanenko.com.testappddapp.db.sqlite.DBHelper;
import ldemyanenko.com.testappddapp.dto.Course;
import ldemyanenko.com.testappddapp.dto.Student;

public class StudentDAO extends BaseDaoImpl<Student, Integer> {
    public StudentDAO(ConnectionSource connectionSource, Class<Student> studentClass) throws SQLException {
        super(connectionSource, studentClass);

    }

    @Override
    public CreateOrUpdateStatus createOrUpdate(Student student) throws SQLException {
        student.refreshCourses();
        CreateOrUpdateStatus status = super.createOrUpdate(student);
        for(Course course: student.getCourses()) {
            course.setStudent(student);
            HelperFactory.getHelper().getCourseDAO().createOrUpdate(course);
        }
        return status;
    }

    public List<Student> getAllStudents() throws SQLException{
        return this.queryForAll();
    }
    @SuppressWarnings("deprecation")
    public List<Student> getStudentList(int page, int pageSize)  throws SQLException{
        QueryBuilder<Student,Integer> queryBuilder = queryBuilder();
        queryBuilder.offset((page)*pageSize).limit(pageSize);
        PreparedQuery<Student> preparedQuery = queryBuilder.prepare();
        List<Student> goalList =query(preparedQuery);
        return goalList;
    }
    @SuppressWarnings("deprecation")
    public List<Student> getStudentFilteredList(int page, int pageSize,String course, int mark)  throws SQLException{
        QueryBuilder<Student, Integer> studentQb = queryBuilder();
        CourseDAO courseDAO = new CourseDAO(getConnectionSource(), Course.class);
        QueryBuilder<Course, Integer> courseQb = courseDAO.queryBuilder();
        courseQb.where().eq(Course.KEY_Name, course).and().eq(DBHelper.StudentCourse_KEY_Mark,mark);
        studentQb.join(courseQb);
        studentQb.offset((page)*pageSize).limit(pageSize);
        PreparedQuery<Student> preparedQuery = studentQb.prepare();
        List<Student> goalList =query(preparedQuery);
        return goalList;
    }
}
