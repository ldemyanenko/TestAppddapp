package ldemyanenko.com.testappddapp.request.retrofit;

import android.app.Activity;

import java.util.List;

import ldemyanenko.com.testappddapp.dto.Student;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;
import ldemyanenko.com.testappddapp.request.RequestInterface;
import retrofit2.Retrofit;

/**
 * Created by ldemyanenko on 03.03.2017.
 */

public class RetrofitRequest implements RequestInterface {

    @Override
    public void doRequest(Activity activity, String url, String apiUrl, final Catchable catchable) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url) //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        StudentsAPI studentsAPI = retrofit.create(StudentsAPI.class);

        studentsAPI.getData().enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                catchable.onCatch(response.body());
            }
            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                catchable.onError(t.getMessage());
            }
        });

    }
}
