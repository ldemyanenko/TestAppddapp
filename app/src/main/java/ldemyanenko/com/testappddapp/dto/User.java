package ldemyanenko.com.testappddapp.dto;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    private String id;
    private int reqCount;
    private String firstName;
    private String lastName;

    public User(){

    }

    public User(String firstName, String lastName, String email) {
        this.firstName=firstName;
        this.lastName=lastName;

    }

    public String getId() {
        return id;
    }

    public int getReqCount() {
        return reqCount;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setReqCount(int reqCount) {
        this.reqCount = reqCount;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void addRequestCount() {
        reqCount++;
    }
}
