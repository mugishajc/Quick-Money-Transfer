package auca.didine.quickmoneytransfer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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

public class TransactionActivity extends AppCompatActivity {
    private String id, jsonresponse;
    private ProgressDialog progressDialog;
private TextView tvtesttrans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        getSupportActionBar().hide();

        tvtesttrans=findViewById(R.id.tvtesttrans);

        progressDialog = new ProgressDialog(TransactionActivity.this);
        progressDialog.setMessage("Loading Transaction...");
        progressDialog.setCanceledOnTouchOutside(false);
        Intent io = getIntent();
        id = io.getStringExtra("id");
        calltransactionapi();
    }

    // create API
    public void calltransactionapi() {
        // create user body object
        Money obj = new Money();
        obj.setId(id);
        progressDialog.show();

        Map<String, String> param = new HashMap<>();
        param.put("client", id);

        trans(param);
    }

    private void trans(final Map<String, String> user) {
        Call<ResponseBody> request = RESTApiClient.getInstance().getApi().gettransaction(user);

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Response
                if (response.isSuccessful()) {
                    Log.i("onSuccess", response.body().toString());

                    String resStr = null;
                    try {
                        resStr = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject json = new JSONObject(resStr);

                        jsonresponse = json.toString();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (Integer.toString(response.code()).equals("200")) {

                     //   displayrtransaction();

                        Toast.makeText(TransactionActivity.this, "data hh"+jsonresponse, Toast.LENGTH_SHORT).show();
                        tvtesttrans.setText(jsonresponse);
                    } else {
                        Toast.makeText(TransactionActivity.this, "Error occured,check your internet connection ", Toast.LENGTH_LONG).show();
                    }

                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(TransactionActivity.this, "No data is found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //failure
                progressDialog.dismiss();
                Toast.makeText(TransactionActivity.this, "error" + t.getLocalizedMessage().toLowerCase(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayrtransaction() {
        try {
            JSONObject object = new JSONObject(jsonresponse);
            JSONArray array = object.getJSONArray("found");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                JSONObject jsonObject1 = jsonObject.getJSONObject("category");


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}