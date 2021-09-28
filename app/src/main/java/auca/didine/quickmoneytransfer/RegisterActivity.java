package auca.didine.quickmoneytransfer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import auca.didine.quickmoneytransfer.singleton.RESTApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
private Spinner spcountry;
private String selectedcountry,names,email,telephone,nid,password;
private Button btnregister;
private EditText etemail,etnames,ettelephone,etnid,etpassword;
private ProgressDialog progressDialog;
private TextView tvlogin;

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog=new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Registering new Account....");
        progressDialog.setCanceledOnTouchOutside(false);

        etemail=findViewById(R.id.etemail);
        etnames=findViewById(R.id.etnames);
        ettelephone=findViewById(R.id.ettelephone);
        etnid=findViewById(R.id.etnid);
        etpassword=findViewById(R.id.etpassword);
        btnregister=findViewById(R.id.btnregister);
        btnregister.setOnClickListener(this);
        spcountry=findViewById(R.id.spcountry);
        spcountry.setOnItemSelectedListener(this);
        List<String> list = new ArrayList<String>();

        list.add("RWANDA");
        list.add("UGANDA");
        list.add("KENYA");
        list.add("TANZANIA");
        list.add("BURUNDI");
        list.add("SOUTH SUDAN");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spcountry.setAdapter(dataAdapter);


        tvlogin=findViewById(R.id.tvlogin);
        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                RegisterActivity.this.finish();
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        selectedcountry=adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnregister:

                names=etnames.getText().toString().trim();
                telephone=ettelephone.getText().toString().trim();
                nid=etnid.getText().toString().trim();
                email=etemail.getText().toString().trim();
                password=etpassword.getText().toString().trim();

                if (names.isEmpty()
                        ||password.isEmpty()
                        ||telephone.isEmpty()
                        ||nid.isEmpty()||password.isEmpty()){
                    AlertDialog.Builder builder=new AlertDialog.Builder(RegisterActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle("Important information");
                    builder.setMessage("Please All fields are required to be filled");
                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }else {
                    callcreateapi();

                }
                break;

        }

    }


    // create API
    public void callcreateapi() {
        // create user body object
        User obj = new User();
        obj.setNames(names);
        obj.setTelephone(telephone);
        obj.setNid(nid);
        obj.setEmail(email);
        obj.setPassword(password);
        obj.setCountry(selectedcountry);
        progressDialog.show();

        Map<String, String> param = new HashMap<>();
        param.put("name", names);
        param.put("telephone", telephone);
        param.put("nid", nid);
        param.put("email", email);
        param.put("password", password);
        param.put("country", selectedcountry);


        login(param);
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
    private void login(final Map<String, String> user) {
        Call<ResponseBody> request = RESTApiClient.getInstance().getApi().createUser(user);

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Response



                        Toast.makeText(RegisterActivity.this, names +"'s account created successfully ", Toast.LENGTH_LONG).show();

                        progressDialog.dismiss();

                etnames.getText().clear();
                ettelephone.getText().clear();
                etnid.getText().clear();
                etpassword.getText().clear();
                etemail.getText().clear();

                progressDialog.dismiss();


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //failure
                progressDialog.dismiss();

                Toast.makeText(RegisterActivity.this, "Registration failed" + t.getCause(), Toast.LENGTH_LONG).show();

            }
        });
    }



}