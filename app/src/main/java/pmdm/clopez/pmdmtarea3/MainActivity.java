package pmdm.clopez.pmdmtarea3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pmdm.clopez.pmdmtarea3.databinding.ActivityMainBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
    //Clase que controla la actividad principal de la app
public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    NavController navController;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //Mapa que relaciona los tipos de pokemon con sus iconos representativos. Static para poder usar desde otras clases
    public static Map<String, Integer> imgTypes = new HashMap<>();
    //Lista para el RV de Pokedex
    public static ArrayList<Pokemon> pokemonList = new ArrayList<>();
    //URL de la API donde obtenemos la info de los pokemons
    final String URL_BASE = "https://pokeapi.co/api/v2/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //Comprobamos si la lista está vacía, se rellena
        if(pokemonList.isEmpty()){
            fillPokemonList();
            fillImg();
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        applyLanguage(); //Ajustamos al lenguaje elegido de la app

        //-------CONFIGURACION DE NAVEGACION DE FRAGMENTOS----------
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = NavHostFragment.findNavController(navHostFragment); //buscamos el controlador
            NavigationUI.setupWithNavController(binding.bottomNavigationView, navController); //asociamos la navegación con el menu de navegacion hecho
            NavigationUI.setupActionBarWithNavController(this, navController); //lo asociamos con el ActionBar
        }
        initiazeAppBar();

        binding.bottomNavigationView.setOnItemSelectedListener(this::onMenuSelected);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.red)));

        // ---CONTROL DE FUNCIONAMIENTO DEL BOTÓN ATRÁS DEL DISPOSITIVO
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //si estamos en el fragmento de info del pokemon y pulsamos atras, volveremos a hacer visible el BottomNavigation
                if(navController.getCurrentDestination().getId() == R.id.infoFragment) {
                    binding.bottomNavigationView.setVisibility(View.VISIBLE);
                    navController.navigateUp();
                }
                else setEnabled(false); //En otros casos, funcionará por defecto
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

    }
    //metodo para ocultar el boton de atras en los fragmentos que se accede por el menu de navegacion
    private void initiazeAppBar() {
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.catchsFragment,
                R.id.pokedexFragment,
                R.id.preferencesFragment
        ).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }


    //Metodo para controlar el funcionamiento del boton atras del actionBar
    @Override
    public boolean onSupportNavigateUp(){
        binding.bottomNavigationView.setVisibility(View.VISIBLE);
        return super.onSupportNavigateUp() || navController.navigateUp();
    }
    //Control de seleccion del BottomNavigation
    private boolean onMenuSelected(MenuItem menuItem) {
        navController.popBackStack();
        if(menuItem.getItemId() == R.id.mypokemon_menu)
            navController.navigate(R.id.catchsFragment);

        else if(menuItem.getItemId() == R.id.pokedex_menu)
            navController.navigate(R.id.pokedexFragment);
        else
            navController.navigate(R.id.preferencesFragment);

        return true;
    }
    //Metodo para cuando capturamos un pokemon desde la pokedex
    public void pokemonCatch(Pokemon pokemon, View view){
        //Comprobamos si ya ha sido capaturado
        if(!pokemon.getCatched()){ //Si no, se guarda en la db, se comunica al usuario y se marca como capturado
            db.collection(user.getEmail()).document(pokemon.getName()).set(pokemon).addOnSuccessListener(runnable->{
                pokemon.setCatched(true);
                        Toast.makeText(this, R.string.catched, Toast.LENGTH_SHORT).show();
                        recreate();
            })
                    .addOnFailureListener(runnable->{
                        Toast.makeText(this, R.string.error_notsaved, Toast.LENGTH_SHORT).show();});
        }else //si ya ha sido capturado, se indica al usuario
            Toast.makeText(this, R.string.no_catched, Toast.LENGTH_SHORT).show();
    }
    //Metodo que se ejecuta cuando elegimos un pokemon de nuestra lista de capturados para enviar la información y cambiar al fragmento de info
    public void pokemonSelect(Pokemon pokemon, View view){
        Bundle bundle = new Bundle();
        bundle.putString("name", pokemon.getName());
        bundle.putInt("id", pokemon.getId());
        bundle.putString("img", pokemon.getSprites().getFront_default());
        bundle.putInt("weight", pokemon.getWeight());
        bundle.putInt("height", pokemon.getHeight());

        List<Type> typesList = pokemon.getTypes();
        int i = 1;
        for(Type type:typesList){
            bundle.putString("type"+i,type.getType().getName());
            i++;
        }

        Navigation.findNavController(view).navigate(R.id.infoFragment, bundle);
        binding.bottomNavigationView.setVisibility(View.GONE); //Dejamos de mostrar el bottom navigation en el fragmento de info
    }
    /*Metodo para rellenar la lista de pokemon que se mostrará en la pokedex.
        Lo hago desde Main Activity ya que desde PokedexFragment, retrasaba la creación de la lista ya que tenía que
        conectar con la API cada vez que cambiaba de fragmento. De esta manera, solo se crea al iniciar la app y reutliza la lista cuando
        cambiamos de fragmento*/
    private void fillPokemonList(){
        //Conectamos con la API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PokemonAPI pokemonAPI = retrofit.create(PokemonAPI.class);

        for(int i = 1; i<=151; i++){ //Sacamos los 151 primeros pokemon, que corresponden con la primera generacion
            Call<Pokemon> call = pokemonAPI.getPokemonById(Integer.toString(i));
            call.enqueue(new Callback<Pokemon>() {
                @Override
                public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                    if (response.isSuccessful()) {
                        Pokemon pokemon = response.body();
                        pokemonList.add(pokemon); //Los añadimos a la lista y los ordenamos por ID
                        pokemonList.sort(new Comparator<Pokemon>() {
                            @Override public int compare(Pokemon p1, Pokemon p2) {
                                return Integer.compare(p1.getId(), p2.getId());
                            }
                        });
                    } else {
                        Log.e("error", "Hubo un error inesperado!");
                    }
                }
                @Override
                public void onFailure(Call<Pokemon> call, Throwable t) {
                    Log.e("error", "Error al obtener los datos: ", t);
                    System.out.println("error");
                }
            });
        }
    }
    //Metodo para rellenar el Mapa que guarda la relación de cada tipo con su icono
    private void fillImg(){
        imgTypes.put("fire", R.drawable.fire_type);
        imgTypes.put("water", R.drawable.water_type);
        imgTypes.put("grass", R.drawable.grass_type);
        imgTypes.put("electric", R.drawable.electric_type);
        imgTypes.put("ghost", R.drawable.ghost_type);
        imgTypes.put("dark", R.drawable.dark_type);
        imgTypes.put("fairy", R.drawable.fairy_type);
        imgTypes.put("dragon", R.drawable.dragon_type);
        imgTypes.put("psychic", R.drawable.psychic_type);
        imgTypes.put("fighting", R.drawable.fighting_type);
        imgTypes.put("steel", R.drawable.steel_type);
        imgTypes.put("ice", R.drawable.ice_type);
        imgTypes.put("normal", R.drawable.normal_type);
        imgTypes.put("bug", R.drawable.bug_type);
        imgTypes.put("rock", R.drawable.rock_type);
        imgTypes.put("ground", R.drawable.ground_type);
        imgTypes.put("flying", R.drawable.flying_type);
        imgTypes.put("poison", R.drawable.poison_type);
    }
    //Metodo para aplicar el lenguaje seleccionado desde que inicia la app
    public void applyLanguage(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String lang = "es";
        if(sharedPreferences.getString("language", "spanish").equals("english")) {
            lang = "en";
        }
        Configuration config = new Configuration();
        config.setLocale(new Locale(lang));
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}
