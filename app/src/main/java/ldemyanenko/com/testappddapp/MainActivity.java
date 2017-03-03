package ldemyanenko.com.testappddapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ldemyanenko.com.testappddapp.db.DBInterface;
import ldemyanenko.com.testappddapp.db.sqlite.SQLiteDB;
import ldemyanenko.com.testappddapp.dto.Student;
import ldemyanenko.com.testappddapp.dto.User;
import ldemyanenko.com.testappddapp.request.HttpOkRequest;
import ldemyanenko.com.testappddapp.request.RequestInterface;
import ldemyanenko.com.testappddapp.request.VolleyRequest;
import ldemyanenko.com.testappddapp.request.retrofit.RetrofitRequest;
import ldemyanenko.com.testappddapp.ui.EndlessRecyclerViewScrollListener;
import ldemyanenko.com.testappddapp.ui.FilterDialog;
import ldemyanenko.com.testappddapp.ui.StudentListAdapter;


public class MainActivity extends BaseActivity {
    final static String baseUrl = "https://ddapp-sfa-api-dev.azurewebsites.net/";
    final static String apiUrl ="api/test/students";
    private final HashMap<String, String> additionalHeaders = new HashMap<>();
    private DBInterface dbI;
    private RequestInterface reqI;
    private RecyclerView listView;
    private ArrayList<Student> students=new ArrayList<Student>();
    private StudentListAdapter adapter;
    private View filterIcon;

    private FilterDialog filterFialog;
    private String filterCourse;
    private int filterMark;
    private DatabaseReference mDatabase;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //reqI=new VolleyRequest();
        //reqI=new RetrofitRequest();
        reqI=new HttpOkRequest();

        //dbI = new RealmDB(this);
        dbI = new SQLiteDB(this);
        //dbI = new OrmLiteDB(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        showProgressDialog();
        getUser();
        initiateViews();


        filterIcon = findViewById(R.id.filter_icon);
        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterFialog.show();
            }
        });
    }

    private void getUser() {
        FirebaseDatabase.getInstance().getReference().child("users").child(getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        user = dataSnapshot.getValue(User.class);
                        requestAndDisplayStudentList();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void updateUser() {
        FirebaseDatabase.getInstance().getReference().child("users").child(getUid()).child("reqCount").setValue(user.getReqCount());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbI.close();
    }

    private void requestAndDisplayStudentList() {

        reqI.doRequest(this, baseUrl,apiUrl, new RequestInterface.Catchable() {
            @Override
            public void onCatch(List<Student> response) {
                Student[] array = new Student[response.size()];
                for(int i = 0; i < response.size(); i++){
                    array[i] = response.get(i);
                }
                Log.e(MainActivity.this.getClass().getSimpleName(), response.toString());
                //make db transaction out of ui tread
                new AsyncTask<Student, Integer, Long> (){

                    @Override
                    protected Long doInBackground(Student... students) {
                        user.addRequestCount();
                        updateUser();
                        dbI.putStudentArray(students);
                        return 1l;
                    }

                    protected void onPostExecute(Long result) {
                        students.addAll(dbI.getStudentList(0));
                        adapter = new StudentListAdapter(students,MainActivity.this, dbI);
                        listView.setAdapter(adapter);
                        filterIcon.setVisibility(View.VISIBLE);
                        List<String> spinnerArray = dbI.getAllDistinctCourses();
                        filterFialog.prepare(spinnerArray);
                        Log.e(MainActivity.this.getClass().getSimpleName(), students.toString());
                        hideProgressDialog();
                    }


                }.execute(array);

            }
            @Override
            public void onError(String error) {
                Log.e(MainActivity.this.getClass().getSimpleName(), error);
                Toast.makeText(getApplicationContext(),"Connection Erroe",Toast.LENGTH_SHORT);
                hideProgressDialog();
            }
        });
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
        adapter.notifyItemInserted(students.size() - 1);
//        listView.post(new Runnable() {
//            public void run() {
//                adapter.notifyItemInserted(students.size() - 1);
//            }
//        });

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
