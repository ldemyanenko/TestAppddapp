package ldemyanenko.com.testappddapp.request;

import android.app.Activity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.Arrays;

import ldemyanenko.com.testappddapp.GsonRequest;
import ldemyanenko.com.testappddapp.dto.Student;

/**
 * Created by ldemyanenko on 03.03.2017.
 */

public class VolleyRequest implements RequestInterface {

    @Override
    public void doRequest(Activity activity, String url, String apiUrl, final Catchable catchable) {
        RequestQueue requestQueue = Volley.newRequestQueue(activity);

        requestQueue.add(new GsonRequest
                (url+apiUrl, Student[].class, null,new Response.Listener<Student[]>() {

                    @Override
                    public void onResponse(final Student[] response) {
                     catchable.onCatch(Arrays.asList(response));

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                     catchable.onError(error.getMessage());
                    }
                }));
        requestQueue.start();

    }
}
