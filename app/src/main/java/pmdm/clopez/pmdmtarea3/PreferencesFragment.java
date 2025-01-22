package pmdm.clopez.pmdmtarea3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;
//Clase que controla el fragmento de preferencias
public class PreferencesFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey); //vinculamos con el XML que configura los ajustes

        //MANEJO DE LAS PREFERENCIAS:
        Preference about = findPreference("about");
        if (about != null) {
            about.setOnPreferenceClickListener(this::showAbout);
        }

        Preference logout = findPreference("logout");
        if (logout != null) {
            logout.setOnPreferenceClickListener(this::logoutSession);
        }

        ListPreference language = findPreference("language");
        if (language != null) {
            language.setOnPreferenceChangeListener(this::changeLanguage);
        }
    }
    //metodo para cambiar lenguaje al cambiar la preferencia en la pantalla de ajustes
    private boolean changeLanguage(Preference preference, Object o) {
        String value = o.toString();
        String lang;
        if(value.equals("english"))
            lang="en";
        else
            lang="es";

        //Establecemos la configuración y reiniciamos la actividad
        Configuration config = new Configuration();
        config.setLocale(new Locale(lang));
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        Activity activity = getActivity();
        if (activity != null) {
            activity.recreate();
        }

        return true;
    }

    //metodo que cierra sesión desde la preferencia "Cerrar sesión"
    private boolean logoutSession(Preference preference) {
        //Preguntamos si está seguro de querer cerrar sesión
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.logout).setMessage(R.string.logout_ask).setPositiveButton(R.string.si, (dialog, id) -> {
            //Si acepta cerrar sesión
            AuthUI.getInstance()
                    .signOut(getActivity())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActivity(), R.string.closed_session, Toast.LENGTH_SHORT).show();
                            goToLogin(); //vamos a la pantalla de login
                        }
                    });
            dialog.cancel();
        }).setNegativeButton(R.string.no, ((dialog, id) ->{dialog.cancel();} )); //si no acepta cerrar sesion
        builder.show();
        return true;
    }
    //metodo para mostrar pantalla de "Acerca de"
    private boolean showAbout(Preference preference) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.about).setMessage(R.string.info_about).setPositiveButton(R.string.accept, (dialog, id) -> {
            //Acciones a realizar cuando pulsamos el botón.
            dialog.cancel();
        });
        builder.show();
        return true;
    }
    //metodo para regresar a la pantalla de login
    public void goToLogin(){
        Activity actividad = getActivity();
        if(actividad!=null) {
            Intent i = new Intent(actividad, LoginActivity.class);
            startActivity(i);
            actividad.finish();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Cambia el título de la ToolBar
        if (getActivity() != null) {
            //Le damos el título a la actividad que nos indica qué fragmento está activo
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(R.string.settings);
            }
        }
    }
}