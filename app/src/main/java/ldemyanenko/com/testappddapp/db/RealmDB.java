package ldemyanenko.com.testappddapp.db;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import ldemyanenko.com.testappddapp.dto.Course;
import ldemyanenko.com.testappddapp.dto.Student;


public class RealmDB implements DBInterface {
    private Realm realm;

    public  RealmDB(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded() // temp, for debugging purposes. Needed to decide on this later before production launch
                .build();
        Realm.deleteRealm(realmConfiguration); // temp, for debugging purposes. Needed to decide on this later before production launch
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void putStudentArray(final Student[] response,final Catchable load) {
        reIndex(response);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(Arrays.asList(response));
            load.whenCatch();

            }
        });
    }
    @NonNull
    public List<String> getAllDistinctCourses() {
        RealmResults<Course> result = realm.where(Course.class).distinct("name");
        List<String> spinnerArray = new ArrayList<String>();
        for (Course item : result) {
            spinnerArray.add(item.getName());
        }
        return spinnerArray;
    }

    @Override
    public Collection<? extends Student> getStudentList(int page) {
        return getFilteredStudentList(page,null,0);
    }

    @Override
    public Collection<? extends Student> getFilteredStudentList(int page, String course, int mark) {
        if(course==null) {
            //RealmResults are lazy loaded, so subList will query only the needed items
            return realm.where(Student.class).findAll().subList((page) * 20 + 1, (page + 1) * 20);
        }else{
            return realm.where(Student.class).equalTo("courses.index",Course.createIndex(course,mark)).findAll().subList((page) * 20 + 1, (page + 1) * 20);

        }
    }

    @Override
    public List<Course> getCoursesByStudent(Student student) {
        return student.getCourses();
    }

    //realm doesn't have joins or sub-queries, so ist's a way for making complex fields conditions
    private void reIndex(Student[] response) {
        for(int i=0; i<response.length;i++){
            for (Course item:response[i].getCourses()){
                item.setIndex();
            }
        }

    }
}
