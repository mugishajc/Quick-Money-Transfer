package auca.didine.quickmoneytransfer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class WidrawActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etwithdrawamount,etwithdraw_account;
    private Button btnwithdrawsend;
    private String receiverw,amountw,idw;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widraw);


        progressDialog=new ProgressDialog(WidrawActivity.this);
        progressDialog.setMessage("Busy Sending Request to agent ...");
        progressDialog.setCanceledOnTouchOutside(false);
        Intent ii=getIntent();
        idw=ii.getStringExtra("id");
        getSupportActionBar().hide();
        etwithdraw_account=findViewById(R.id.etwithdraw_account);
        etwithdrawamount=findViewById(R.id.etwithdrawamount);

        receiverw=etwithdraw_account.getText().toString().trim();
        amountw=etwithdrawamount.getText().toString().trim();


        btnwithdrawsend=findViewById(R.id.btnwithdrawsend);
        btnwithdrawsend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnwithdrawsend:
                receiverw=etwithdraw_account.getText().toString().trim();
                amountw=etwithdrawamount.getText().toString().trim();
                if (receiverw.isEmpty() || amountw.isEmpty()){
                    AlertDialog.Builder builder=new AlertDialog.Builder(WidrawActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle("Important information");
                    builder.setMessage("Please Agent account number and amount are required to be filled first of all");
                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }else{
                    calltransferagentapi();
                }

                break;

        }
    }

    // create API
    public void calltransferagentapi() {
        // create user body object
        Money obj = new Money();
        obj.setId(idw);
        obj.setReceiver(receiverw);
        obj.setAmount(amountw);
        progressDialog.show();

        Map<String, String> param = new HashMap<>();
        param.put("transfer", idw);
        param.put("receiver",receiverw);
        param.put("amount",amountw);

        out(param);
    }
    private void out(final Map<String, String> user) {
        Call<ResponseBody> request = RESTApiClient.getInstance().getApi().withdrawmoney(user);

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Response

                if (Integer.toString(response.code()).equals("200")) {
                    progressDialog.dismiss();

                    try {
                        String jsondata = response.body().string();

                        if (jsondata != null) {

                            progressDialog.dismiss();
                            JSONObject reader = new JSONObject(jsondata);

                            String t=reader.getString("message");
                            Toast.makeText(WidrawActivity.this, "Message from DB:"+t.toLowerCase(), Toast.LENGTH_LONG).show();

                            android.app.AlertDialog.Builder b=new android.app.AlertDialog.Builder(WidrawActivity.this);
                            b.setTitle("Important message ");
                            b.setMessage("Message from the database is "+t.toLowerCase());
                            b.setCancelable(false);
                            b.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(WidrawActivity.this, "Okay,UNDERSTOOD ", Toast.LENGTH_SHORT).show();
                                }
                            });

                            b.show();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(WidrawActivity.this, "system Error", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(WidrawActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(WidrawActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(WidrawActivity.this, "server side Error occured", Toast.LENGTH_SHORT).show();
                }

                etwithdrawamount.getText().clear();
                etwithdraw_account.getText().clear();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //failure
                progressDialog.dismiss();
                Toast.makeText(WidrawActivity.this, "error"+t.getLocalizedMessage().toLowerCase(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
