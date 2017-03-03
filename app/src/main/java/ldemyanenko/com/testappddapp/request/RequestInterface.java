package ldemyanenko.com.testappddapp.request;

import android.app.Activity;

import java.util.List;

import ldemyanenko.com.testappddapp.dto.Student;

/**
 * Created by ldemyanenko on 03.03.2017.
 */

public interface RequestInterface {
    void doRequest(Activity activity, String url, String apiUrl, Catchable catchable);


    interface Catchable{
        void onCatch(List<Student> response);
        void onError(String error);
    }
}
