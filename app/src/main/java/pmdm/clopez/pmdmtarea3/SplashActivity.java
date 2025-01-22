package pmdm.clopez.pmdmtarea3;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import pmdm.clopez.pmdmtarea3.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    private static final int SPLASH_TIME = 3000; //Tiempo que se mantendra la pantalla Splash

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());

        //Verificamos si la versión de la API <31
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.S){
            setContentView(binding.getRoot()); //mostramos la pantalla

            //Controlamos el inicio de la MainActivity
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }, SPLASH_TIME);
        }else{ //si API>= 31, se muestra directamente la MainActivity
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}