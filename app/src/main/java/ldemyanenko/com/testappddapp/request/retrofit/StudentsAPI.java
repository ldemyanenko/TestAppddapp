package ldemyanenko.com.testappddapp.request.retrofit;

import java.util.List;

import ldemyanenko.com.testappddapp.dto.Student;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by ldemyanenko on 03.03.2017.
 */

public interface StudentsAPI {
    @GET("/api/test/students")
    Call<List<Student>> getData();
}
