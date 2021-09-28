package auca.didine.quickmoneytransfer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MyProfileActivity extends AppCompatActivity {

    private String name,id,country,compte,currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        getSupportActionBar().hide();
//        Picasso.with(MyProfileActivity.this)
//                .load(u.getAvatar())
//                .resize(90, 90)
//                .noFade()
//                .centerCrop()
//                .error(R.drawable.ic_person)
//                .into(imgavatar);

        TextView tvuserdata=findViewById(R.id.tvuserdata);

        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        country=intent.getStringExtra("country");
        compte=intent.getStringExtra("compte");
        currency=intent.getStringExtra("currency");
        id=intent.getStringExtra("id");


        tvuserdata.setText("Names:  "+name+"\n \n"+"Country : "+country+"\n \n"+"My Wallet Account number:" + compte+"\n \n"+"My Currency: "+currency);

    }
}