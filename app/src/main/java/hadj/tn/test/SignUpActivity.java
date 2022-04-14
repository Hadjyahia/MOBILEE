package hadj.tn.test;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import hadj.tn.test.Model.User;
import hadj.tn.test.util.API;
import hadj.tn.test.util.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    EditText editTextEmail,editTextPhone,editTextPassword,editTextUsername;
    Spinner editTextRoleUser;
    ImageView backToSignIn;
    String email;

    String[] users = { "Person", "Hospital", "Blood bank" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextRoleUser = (Spinner) findViewById(R.id.appUserRole);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editTextRoleUser.setAdapter(adapter);
        editTextRoleUser.setOnItemSelectedListener(this);

        initializeComponents();
    }

    private void initializeComponents() {

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPhone = (EditText) findViewById(R.id.phone);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextUsername = (EditText) findViewById(R.id.username);
        editTextRoleUser = (Spinner) findViewById(R.id.appUserRole);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editTextRoleUser.setAdapter(adapter);
        editTextRoleUser.setOnItemSelectedListener(this);

        soup.neumorphism.NeumorphButton signUp = (soup.neumorphism.NeumorphButton) findViewById(R.id.signup);

        RetrofitClient retrofitClient = new RetrofitClient();
        API userApi = retrofitClient.getRetrofit().create(API.class);

        backToSignIn = findViewById(R.id.backToSignIn);
        backToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,Sign_InActivity.class);
                startActivity(intent);
            }
        });
        signUp.setOnClickListener(View -> {
            email = String.valueOf(editTextEmail.getText());
            String phone = String.valueOf(editTextPhone.getText());
            String password = String.valueOf(editTextPassword.getText());
            String username = String.valueOf(editTextUsername.getText());
            String appUserRole = editTextRoleUser.getSelectedItem().toString();
            
            Boolean check = validationInfo(email, phone, password, username);
            if (check){

                User user = new User();
                user.setEmail(email);
                user.setPhone(phone);
                user.setPassword(password);
                user.setUsername(username);
                user.setAppUserRole(appUserRole);

                userApi.createUser(user)
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                if (response.code() == 500) {
                                    Toast.makeText(SignUpActivity.this, "Email already taken", Toast.LENGTH_LONG).show();
                                }
                                else if (response.code() == 200) {
                                    Toast.makeText(SignUpActivity.this, "Save successful", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(SignUpActivity.this,CheckEmailActivity.class);
                                    //intent.putExtra("email",email);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(SignUpActivity.this, "Save failed", Toast.LENGTH_LONG).show();

                            }
                        });
            }
        });
    }

    public int getUserId(){
        RetrofitClient retrofitClient = new RetrofitClient();
        API userApi = retrofitClient.getRetrofit().create(API.class);

        final int[] id = new int[1];
        userApi.getUser(email)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        User user = response.body();
                        id[0] = user.getId();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
        return id[0];
    }


    private Boolean validationInfo(String email, String phone, String password, String username) {

        if(username.length()==0){
            editTextUsername.requestFocus();
            editTextUsername.setError("Username cannot be empty");
            return false;

        }else if(!username.matches("([a-zA-Z]+(\\s)*(\\s[a-zA-Z]+)*)+")){
            editTextUsername.requestFocus();
            editTextUsername.setError("Enter only alphabetic characters and spaces");
            return false;
        }else if(email.length()==0){
            editTextEmail.requestFocus();
            editTextEmail.setError("Email cannot be empty");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.requestFocus();
            editTextEmail.setError("Enter valid email");
            return false;
        }else if(phone.length()==0){
            editTextPhone.requestFocus();
            editTextPhone.setError("Phone cannot be empty");
            return false;
        }else if(!phone.matches("[0-9]{8}$")){
            editTextPhone.requestFocus();
            editTextPhone.setError("Phone number has 8 digits");
            return false;
        }else if(password.length()<=5){
            editTextPassword.requestFocus();
            editTextPassword.setError("Minimum 6 characters required");
            return false;
        }
        else return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}