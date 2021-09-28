package auca.didine.quickmoneytransfer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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

public class MainActivity extends AppCompatActivity {
private TextView tvname,tvcountry,tvcompte,tvbalances;
private String name,id,country,compte,currency,email,role_id,created_at;
private ProgressDialog progressDialog;
private LinearLayout lintransfermoney,lintransaction;


    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    tvbalances=findViewById(R.id.tvbalances);
    tvname=findViewById(R.id.tvname);
    tvcountry=findViewById(R.id.tvcountry);
    tvcompte=findViewById(R.id.tvcompte);
    Intent intent=getIntent();

    lintransfermoney=findViewById(R.id.lintransfermoney);

    name=intent.getStringExtra("name");
    country=intent.getStringExtra("country");
    compte=intent.getStringExtra("compte");
    currency=intent.getStringExtra("currency");
    id=intent.getStringExtra("id");

    tvname.setText("Names: "+name);
    tvcountry.setText("Country: "+country);
    tvcompte.setText("Account Number:"+compte);


        lintransfermoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i=new Intent(MainActivity.this,Transfermoney.class);
               i.putExtra("id",id);
               startActivity(i);
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);

            }
        });

    progressDialog=new ProgressDialog(MainActivity.this);
    progressDialog.setMessage("Loading...");
    progressDialog.setCanceledOnTouchOutside(false);
    callgetuserbalapi();


        BottomNavigationView navigationView =findViewById(R.id.bottom_view_main);
        navigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {

                case R.id.nav_home:
                    Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.nav_transaction:
                    Intent i=new Intent(MainActivity.this,TransactionActivity.class);
                    i.putExtra("id",id);
                    startActivity(i);
                    overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                    break;
//                case R.id.nav_me:
//                    startActivity(new Intent(MainActivity.this,MeActivity.class));
//                    overridePendingTransition(R.anim.fadein,R.anim.fadeout);
//                    break;

                case R.id.nav_withdraw:
                    Intent iii=new Intent(MainActivity.this,WidrawActivity.class);
                    iii.putExtra("id",id);
                    startActivity(iii);
                    overridePendingTransition(R.anim.fadeout,R.anim.fadein);
                    break;

            }

            return true;
        });




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.accountmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.nav_profile:
                Intent inte=new Intent(MainActivity.this,MyProfileActivity.class);
                inte.putExtra("name",name);
                inte.putExtra("country",country);
                inte.putExtra("compte",compte);
                inte.putExtra("currency",currency);
                startActivity(inte);

                return  true;
            case R.id.nav_moreinfo:
                Toast.makeText(this, "My Name is Didine Mukunde, [ AUCA  ] for more info visit my IG ", Toast.LENGTH_LONG).show();
                return true;

            case R.id.nav_refresh:
                callgetuserbalapi();
                Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.nav_logout:
                AlertDialog.Builder b=new AlertDialog.Builder(MainActivity.this);
                b.setTitle("Important Comfirmation");
                b.setMessage("Are you sure you want to logout?"+"\n"+"If yes press logout but if not press cancel");
                b.setCancelable(false);
                b.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Cancelled please", Toast.LENGTH_SHORT).show();
                    }
                });
                b.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MainActivity.this.startActivity(intent);
                    }
                });

                b.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // create API
    public void callgetuserbalapi() {
        // create user body object
        Client obj = new Client();
        obj.setClient(compte);
        progressDialog.show();

        Map<String, String> param = new HashMap<>();
        param.put("client", compte);

        in(param);
    }

    private void in(final Map<String, String> user) {
        Call<ResponseBody> request = RESTApiClient.getInstance().getApi().getclientbalance(user);

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

                            JSONObject data = reader.getJSONObject("balance");
                            String previous_balances = data.getString("previous_balances");
                            String amounts = data.getString("amounts");
                            String balances = data.getString("balances");
                            String fees = data.getString("fees");
                            String category = data.getString("category");
                            String created_at = data.getString("created_at");

                            tvbalances.setText("My Balance:"+balances+" "+currency+"\n"
                                    +"Prev-Bal:"+previous_balances+" "+currency);
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "system Error", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //failure
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "error"+t.getLocalizedMessage().toLowerCase(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;

        } else {
            Toast.makeText(getBaseContext(), "press back again to exit", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }

}