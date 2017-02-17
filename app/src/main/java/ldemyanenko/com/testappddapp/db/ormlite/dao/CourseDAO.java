package ldemyanenko.com.testappddapp.db.ormlite.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import ldemyanenko.com.testappddapp.dto.Course;
import ldemyanenko.com.testappddapp.dto.Student;

public class CourseDAO extends BaseDaoImpl<Course, Integer> {
    public CourseDAO(ConnectionSource connectionSource, Class<Course> courseClass) throws SQLException {
        super(connectionSource, courseClass);

    }

    public List<Course> getCourseListDistinct()  throws SQLException{
        QueryBuilder<Course,Integer> queryBuilder = queryBuilder();
        queryBuilder.distinct().selectColumns(Course.KEY_Name);
        PreparedQuery<Course> preparedQuery = queryBuilder.prepare();
        List<Course> list =query(preparedQuery);
        return list;
    }
}
