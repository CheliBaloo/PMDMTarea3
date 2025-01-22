package pmdm.clopez.pmdmtarea3;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
//Interfaz que se usa para obtener los datos de la API
public interface PokemonAPI {
    @GET("pokemon/{id}")
    Call<Pokemon> getPokemonById(@Path("id") String id);

}
