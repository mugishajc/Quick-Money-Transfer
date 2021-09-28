package auca.didine.quickmoneytransfer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import auca.didine.quickmoneytransfer.singleton.RESTApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etemaillogin,etpasswordlogin;
    private Button btnlogin;
private TextView tv_signup;
private ProgressDialog progressDialog;
    private String email,password;

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        tv_signup=findViewById(R.id.tv_signup);
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                LoginActivity.this.finish();
            }
        });

        progressDialog=new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Authenticating user ....");
        progressDialog.setCanceledOnTouchOutside(false);

        etemaillogin=findViewById(R.id.etemaillogin);
        etpasswordlogin=findViewById(R.id.etPasswordlogin);
        btnlogin=findViewById(R.id.btnLogin);

        btnlogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnLogin:
                email=etemaillogin.getText().toString().trim();
                password=etpasswordlogin.getText().toString().trim();
                if (email.isEmpty()||password.isEmpty()){
                    Toast.makeText(this, "Email or password is missing", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(view, "All Fields are required", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else {
                    callloginapi();

                }


                break;
        }
    }



    // create API
    public void callloginapi() {
        // create user body object
        User obj = new User();
        obj.setEmail(email);
        obj.setPassword(password);
        progressDialog.show();

        Map<String, String> param = new HashMap<>();
        param.put("email", email);
        param.put("password", password);

        in(param);
    }

    private void in(final Map<String, String> user) {
        Call<ResponseBody> request = RESTApiClient.getInstance().getApi().loginUser(user);

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Response
                if (Integer.toString(response.code()).equals("200")) {

                    try {
                        String jsondata = response.body().string();

                        if (jsondata != null) {

                            progressDialog.dismiss();
                            JSONObject reader = new JSONObject(jsondata);

                            JSONObject data = reader.getJSONObject("user");
                            String id = data.getString("id");
                            String name = data.getString("name");
                            String nid = data.getString("nid");
                            String email = data.getString("email");
                            String telephone = data.getString("telephone");
                            String country = data.getString("country");
                            String compte = data.getString("compte");
                            String currency = data.getString("currency");
                            String role_id = data.getString("role_id");
                            String created_at = data.getString("created_at");


                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);

                            intent.putExtra("id", id);
                            intent.putExtra("name", name);
                            intent.putExtra("nid", nid);
                            intent.putExtra("email", email);
                            intent.putExtra("telephone", telephone);
                            intent.putExtra("country", country);
                            intent.putExtra("compte", compte);
                            intent.putExtra("currency", currency);
                            intent.putExtra("role_id", role_id);
                            intent.putExtra("created_at", created_at);
                            startActivity(intent);

                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "system Error", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else if (Integer.toString(response.code()).equals("401")){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder b=new AlertDialog.Builder(LoginActivity.this);
                    b.setTitle("Important Information");
                    b.setMessage("Sorry ,Your email or password are incorrect. consult our nearest agent or contact system DBA directly");
                    b.setCancelable(false);
                    b.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(LoginActivity.this, "Yes,Noted kbx ", Toast.LENGTH_SHORT).show();
                        }
                    });

                    b.show();

                }else{
                    progressDialog.dismiss();

                    Toast.makeText(LoginActivity.this, "Please check your internet connection or Error occured on server side", Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //failure
                progressDialog.dismiss();

                Toast.makeText(LoginActivity.this, "Registration failed" + t.getCause(), Toast.LENGTH_LONG).show();

            }
        });
    }




    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "press back button again to exit", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }

}