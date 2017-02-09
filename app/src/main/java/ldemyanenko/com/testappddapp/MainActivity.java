package ldemyanenko.com.testappddapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import ldemyanenko.com.testappddapp.dto.Course;
import ldemyanenko.com.testappddapp.dto.Student;
import ldemyanenko.com.testappddapp.ui.EndlessRecyclerViewScrollListener;
import ldemyanenko.com.testappddapp.ui.FilterDialog;
import ldemyanenko.com.testappddapp.ui.StudentListAdapter;


public class MainActivity extends AppCompatActivity {
    final static String url = "https://ddapp-sfa-api-dev.azurewebsites.net/api/test/students";
    private final HashMap<String, String> additionalHeaders = new HashMap<>();
    private RequestQueue requestQueue;
    private Realm realm;
    private RecyclerView listView;
    private ArrayList<Student> students=new ArrayList<Student>();
    private StudentListAdapter adapter;
    private View filterIcon;

    private FilterDialog filterFialog;
    private String filterCourse;
    private int filterMark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateDataBase();
        initiateViews();
        requestAndDisplayStudentList();
        filterIcon = findViewById(R.id.filter_icon);
        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterFialog.show();
            }
        });
    }

    private void requestAndDisplayStudentList() {
        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(new GsonRequest
                (url, Student[].class, additionalHeaders,new Response.Listener<Student[]>() {

                    @Override
                    public void onResponse(final Student[] response) {
                        Log.e(MainActivity.this.getClass().getSimpleName(), response.toString());
                        reIndex(response);
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {

                                realm.copyToRealmOrUpdate(Arrays.asList(response));

                                queryStudents(0);
                                adapter = new StudentListAdapter(students,MainActivity.this);
                                listView.setAdapter(adapter);
                                filterIcon.setVisibility(View.VISIBLE);
                                RealmResults<Course> result = realm.where(Course.class).distinct("name");
                                List<String> spinnerArray = new ArrayList<String>();
                                for (Course item : result) {
                                    spinnerArray.add(item.getName());
                                }
                                filterFialog.prepare(spinnerArray);
                                Log.e(MainActivity.this.getClass().getSimpleName(), students.toString());

                            }
                        });

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(MainActivity.this.getClass().getSimpleName(), error.toString());

                    }
                }));
        requestQueue.start();
    }

    private void initiateViews() {
        listView=(RecyclerView)findViewById(R.id.main_list);
        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(layout);
        listView.addOnScrollListener( new EndlessRecyclerViewScrollListener(layout){
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromDB(page);

            }
        });
        filterFialog =new FilterDialog(MainActivity.this);
        filterFialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    private void initiateDataBase() {
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded() // temp, for debugging purposes. Needed to decide on this later before production launch
                .build();
        Realm.deleteRealm(realmConfiguration); // temp, for debugging purposes. Needed to decide on this later before production launch
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();
    }

    private void loadNextDataFromDB(int page) {
        queryStudents(page);
        listView.post(new Runnable() {
            public void run() {
                adapter.notifyItemInserted(students.size() - 1);
            }
        });

    }

    private void queryStudents(int page) {
        if(filterCourse==null) {
            //RealmResults are lazy loaded, so subList will query only the needed items
            students.addAll(realm.where(Student.class).findAll().subList((page) * 20 + 1, (page + 1) * 20));
        }else{
            students.addAll(realm.where(Student.class).equalTo("courses.index",filterCourse+"__"+filterMark).findAll().subList((page) * 20 + 1, (page + 1) * 20));

        }
    }

    private List<Student> filter(RealmResults<Student> index) {
        List<Student> list=new ArrayList<Student>();
        for(Student item:index){
            boolean added=false;
            for(Course course:item.getCourses()){
                if(course.getName().equals(filterCourse)&& course.getMark()==filterMark ){
                    added=true;
                    break;
                }
            }
            if(added){
                list.add(item);
            }
        }
        return list;
    }
    //realm doesn't have joins or sub-queries, so ist's a way for making complex fields conditions
    private void reIndex(Student[] response) {
        for(int i=0; i<response.length;i++){
            for (Course item:response[i].getCourses()){
                item.setIndex();
            }
        }

    }


    public void setFilter(String filterCourse, int filterMark) {
        this.filterCourse=filterCourse;
        this.filterMark=filterMark;
        students.clear();
        queryStudents(0);
        listView.post(new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void clearFilter() {
        setFilter(null,filterMark);
    }
}
