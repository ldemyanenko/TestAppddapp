package ldemyanenko.com.testappddapp.db.ormlite;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;


public class  HelperFactory{

    private static OrmHelper databaseHelper;

    public static OrmHelper getHelper(){
        return databaseHelper;
    }
    public static void setHelper(Context context){
        databaseHelper = OpenHelperManager.getHelper(context, OrmHelper.class);
    }
    public static void releaseHelper(){
        OpenHelperManager.releaseHelper();
        databaseHelper = null;
    }
}