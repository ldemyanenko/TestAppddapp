package ldemyanenko.com.testappddapp.request;

import android.app.Activity;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;

import ldemyanenko.com.testappddapp.dto.Student;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 8760w on 03.03.2017.
 */

public class HttpOkRequest implements RequestInterface {
    @Override
    public void doRequest(Activity activity, String url, String apiUrl, final Catchable catchable) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url+apiUrl)
                .build();
        final Gson gson = new Gson();
// Get a handler that can be used to post to the main thread
        client.newCall(request).enqueue(new Callback() {

            // Parse response using gson deserializer
            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                // Process the data on the worker thread
                Student[] students = gson.fromJson(response.body().charStream(), Student[].class);
                catchable.onCatch(Arrays.asList(students));
                // Access deserialized user object here
            }
            @Override
            public void onFailure(Call call, IOException e) {
                catchable.onError(e.getMessage());
            }
        });
    }
}
