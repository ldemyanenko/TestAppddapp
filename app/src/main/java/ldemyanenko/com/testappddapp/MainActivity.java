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
import java.util.HashMap;
import java.util.List;

import ldemyanenko.com.testappddapp.db.DBInterface;
import ldemyanenko.com.testappddapp.db.ormlite.OrmLiteDB;
import ldemyanenko.com.testappddapp.db.realm.RealmDB;
import ldemyanenko.com.testappddapp.db.sqlite.SQLiteDB;
import ldemyanenko.com.testappddapp.dto.Student;
import ldemyanenko.com.testappddapp.ui.EndlessRecyclerViewScrollListener;
import ldemyanenko.com.testappddapp.ui.FilterDialog;
import ldemyanenko.com.testappddapp.ui.StudentListAdapter;


public class MainActivity extends AppCompatActivity {
    final static String url = "https://ddapp-sfa-api-dev.azurewebsites.net/api/test/students";
    private final HashMap<String, String> additionalHeaders = new HashMap<>();
    private DBInterface dbI;
    private RequestQueue requestQueue;
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
        //dbI = new RealmDB(this);
        //dbI = new SQLiteDB(this);
        dbI = new OrmLiteDB(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbI.close();
    }

    private void requestAndDisplayStudentList() {
        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(new GsonRequest
                (url, Student[].class, additionalHeaders,new Response.Listener<Student[]>() {

                    @Override
                    public void onResponse(final Student[] response) {
                        Log.e(MainActivity.this.getClass().getSimpleName(), response.toString());
                        dbI.putStudentArray(response,new DBInterface.Catchable() {
                            @Override
                            public void whenCatch() {

                                students.addAll(dbI.getStudentList(0));
                                adapter = new StudentListAdapter(students,MainActivity.this, dbI);
                                listView.setAdapter(adapter);
                                filterIcon.setVisibility(View.VISIBLE);
                                List<String> spinnerArray = dbI.getAllDistinctCourses();
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
        students.addAll(dbI.getFilteredStudentList(page,filterCourse,filterMark));
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
