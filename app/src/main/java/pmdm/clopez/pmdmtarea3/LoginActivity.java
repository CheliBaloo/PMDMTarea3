package pmdm.clopez.pmdmtarea3;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
//Clase que controla la actividad que muestra la pantalla de Login
public class LoginActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }
    //Metodo que registra una devolución de llamada para el contrato de resultado de la actividad de FirebaseUI
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );
    //Metodo que crea una intención de inicio de sesión con los métodos de inicio de sesión elegidos:
    private void startSignIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                 new AuthUI.IdpConfig.GoogleBuilder().build());

        //Usamos interfaz predeterminada, solo modificamos el logo
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.poke_tittle)      // Set logo drawable
                .setTheme(R.style.Base_Theme_PMDMTarea3)   // Set theme
                .build();
        signInLauncher.launch(signInIntent);
    }
    //Metodo que controla el resultado de intento de inicio de sesion
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {

        if (result.getResultCode() == RESULT_OK) {
            // Si se ha iniciado correctamente, lo lleva a la MainActivity
            goToMainActivity();
        } else { //Si no se ha podido, indica error de inicio
            Toast.makeText(this, "Error Login", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    //Si se inicia la app de nuevo, controla si ya se ha iniciado la sesion
    protected void onStart() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        super.onStart();
        if(user!=null){
            goToMainActivity();
        }else{
            startSignIn();
        }
    }
    //metodo para cambiar a la actividad principal
    private void goToMainActivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Toast.makeText(this, "Bienvenido "+user.getDisplayName(), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
