package ldemyanenko.com.testappddapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ldemyanenko.com.testappddapp.dto.User;

import static ldemyanenko.com.testappddapp.SignInFragment.KEY_EMAIL;
import static ldemyanenko.com.testappddapp.SignInFragment.KEY_PASS;

public class RegistrationFragment extends BaseFragment implements View.OnClickListener {
    private EditText mFirstName;
    private EditText mLastName;
    private String email;
    private static final String TAG = "RegistrationFragment";
    private String password;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        email = getArguments().getString(KEY_EMAIL);
        password =getArguments().getString(KEY_PASS);
        View rootView = inflater.inflate(R.layout.fragment_registration, container, false);

        // Views
        ((TextView) rootView.findViewById(R.id.email)).setText(email);
        mFirstName = (EditText) rootView.findViewById(R.id.first_name);
        mLastName = (EditText) rootView.findViewById(R.id.last_name);
       rootView.findViewById(R.id.reg_button).setOnClickListener(this);
//        mSignUpButton = (Button) rootView.findViewById(R.id.button_sign_up);
//
//        // Click listeners
//        mSignInButton.setOnClickListener(this);
//        mSignUpButton.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.reg_button:{
              register();
            }
    }
    }

    private void register() {
                mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(getContext(), "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: Failed=" + task.getException().getMessage()); //ADD THIS

                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        // Write new user
        writeNewUser(user.getUid(), mFirstName.getText().toString(),mLastName.getText().toString(), user.getEmail());

        // Go to MainActivity
        startActivity(new Intent(getContext(), MainActivity.class));
        //finish();
    }
     //[START basic_write]
    private void writeNewUser(String userId, String name,String lastName, String email) {
        User user = new User(name,lastName, email);

        mDatabase.child("users").child(userId).setValue(user);
    }
    // [END basic_write]
}
