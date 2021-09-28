package auca.didine.quickmoneytransfer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class Transfermoney extends AppCompatActivity implements View.OnClickListener {

    private EditText etamount,etreceiver_account;
    private Button btnsend;
    private String receiver,amount,id,valueotp;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfermoney);


        progressDialog=new ProgressDialog(Transfermoney.this);
        progressDialog.setMessage("Busy Sending money...");
        progressDialog.setCanceledOnTouchOutside(false);
        Intent ii=getIntent();
        id=ii.getStringExtra("id");
        getSupportActionBar().hide();
        etreceiver_account=findViewById(R.id.etreceiver_account);
        etamount=findViewById(R.id.etamount);

        receiver=etreceiver_account.getText().toString().trim();
        amount=etamount.getText().toString().trim();


        btnsend=findViewById(R.id.btnsend);
        btnsend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnsend:
                receiver=etreceiver_account.getText().toString().trim();
                amount=etamount.getText().toString().trim();
                if (receiver.isEmpty() || amount.isEmpty()){
                    AlertDialog.Builder builder=new AlertDialog.Builder(Transfermoney.this);
                    builder.setCancelable(false);
                    builder.setTitle("Important information");
                    builder.setMessage("Please receiver account number and amount are required to be filled");
                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }else{

                    AlertDialog.Builder builder=new AlertDialog.Builder(Transfermoney.this);
                    builder.setCancelable(false);
                    builder.setTitle("Are you sure ?");
                    builder.setMessage("Do you want to send money or not, if yes press send but if not press cancel");
                    builder.setPositiveButton("SEND", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                        callsendotp();
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();

                }

                break;

        }
    }

    // create API
    public void calltransferapi() {
        // create user body object
        Money obj = new Money();
        obj.setId(id);
        obj.setReceiver(receiver);
        obj.setAmount(amount);
        progressDialog.show();

        Map<String, String> param = new HashMap<>();
        param.put("transfer", id);
        param.put("receiver",receiver);
        param.put("amount",amount);

        out(param);
    }
    private void out(final Map<String, String> user) {
        Call<ResponseBody> request = RESTApiClient.getInstance().getApi().transfermoney(user);

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
                            Toast.makeText(Transfermoney.this, "Message from DB:"+t.toLowerCase(), Toast.LENGTH_LONG).show();

                            android.app.AlertDialog.Builder b=new android.app.AlertDialog.Builder(Transfermoney.this);
                            b.setTitle("Important message ");
                            b.setMessage("Message from the database is "+t.toLowerCase());
                            b.setCancelable(false);
                            b.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(Transfermoney.this, "Okay,UNDERSTOOD ", Toast.LENGTH_SHORT).show();
                                }
                            });

                            b.show();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(Transfermoney.this, "system Error", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(Transfermoney.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Transfermoney.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(Transfermoney.this, "server side Error occured", Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //failure
                progressDialog.dismiss();
                Toast.makeText(Transfermoney.this, "error"+t.getLocalizedMessage().toLowerCase(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    // send otp
    public void callsendotp() {
        // create user body object
        Client obj = new Client();
        obj.setClient(id);

        progressDialog.show();
        Map<String, String> param = new HashMap<>();
        param.put("client", id);
        outotp(param);
    }

    private void outotp(final Map<String, String> user) {
        Call<ResponseBody> req = RESTApiClient.getInstance().getApi().sendotp(user);

        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Response

                if (Integer.toString(response.code()).equals("201")) {
                    progressDialog.dismiss();

                    AlertDialog.Builder alert = new AlertDialog.Builder(Transfermoney.this);
                    alert.setTitle("Authentication code");
                    alert.setCancelable(false);
                    alert.setMessage("Enter code that is sent to your phone number:");

// Set an EditText view to get user input
                    final EditText input = new EditText(Transfermoney.this);
                    alert.setView(input);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {


                             valueotp = input.getText().toString();
                            Log.d("", "Pin Value : " + valueotp);

                            callcheckotpcode();

                            return;
                        }
                    });

                    alert.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    return;
                                }
                            });
                    alert.show();


                    try {
                        String jsondata = response.body().string();

                        if (jsondata != null) {

                            progressDialog.dismiss();
                            JSONObject reader = new JSONObject(jsondata);

                            String t=reader.getString("message");
                            Toast.makeText(Transfermoney.this, "Message from DB:"+t.toLowerCase(), Toast.LENGTH_LONG).show();

                            android.app.AlertDialog.Builder b=new android.app.AlertDialog.Builder(Transfermoney.this);
                            b.setTitle("Important message ");
                            b.setMessage("Message from the database is "+t.toLowerCase());
                            b.setCancelable(false);
                            b.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(Transfermoney.this, "Okay,UNDERSTOOD ", Toast.LENGTH_SHORT).show();
                                }
                            });

                            b.show();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(Transfermoney.this, "system Error", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(Transfermoney.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Transfermoney.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(Transfermoney.this, "server side Error occured", Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //failure
                progressDialog.dismiss();
                Toast.makeText(Transfermoney.this, "error"+t.getLocalizedMessage().toLowerCase(), Toast.LENGTH_SHORT).show();
            }
        });
    }

private  void callcheckotpcode(){
    // create user body object
    OTPCode obj = new OTPCode();
    obj.setClient(id);

    progressDialog.show();

    Map<String, String> param = new HashMap<>();
    param.put("client",id);
    param.put("code",valueotp);
    incheckauth(param);
}

    private void incheckauth(final Map<String, String> user) {
        Call<ResponseBody> req = RESTApiClient.getInstance().getApi().checkotp(user);

        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Response

                if (Integer.toString(response.code()).equals("201")) {
                    progressDialog.dismiss();
                     calltransferapi();
                    etamount.getText().clear();
                    etreceiver_account.getText().clear();

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(Transfermoney.this, "Invalid Authentication code,please try again later ", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //failure
                progressDialog.dismiss();
                Toast.makeText(Transfermoney.this, "error"+t.getLocalizedMessage().toLowerCase(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
