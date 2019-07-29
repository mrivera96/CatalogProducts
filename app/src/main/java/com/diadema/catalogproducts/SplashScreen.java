package com.diadema.catalogproducts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by César Andrade on 27/03/2019.
 */

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        setTheme(R.style.AppTheme);
        //3000=3 segundos
        int loading = 3000;
        new Handler().postDelayed(() -> {
            //setelah loading maka akan langsung berpindah ke home activity
            Intent home=new Intent(getApplicationContext(), MainActivity.class);
            startActivity(home);
            finish();
        }, loading);
    }
}
