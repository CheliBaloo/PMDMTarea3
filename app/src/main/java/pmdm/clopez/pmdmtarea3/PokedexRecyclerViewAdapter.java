package pmdm.clopez.pmdmtarea3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pmdm.clopez.pmdmtarea3.databinding.ItemCardviewBinding;
//Clase que funcionará de adaptador del Recycler View que muestra los pokemons de la pokedex
public class PokedexRecyclerViewAdapter extends RecyclerView.Adapter<PokedexViewHolder> {

    private final ArrayList<Pokemon> pokemonList;
    private final Context context;

    public PokedexRecyclerViewAdapter(ArrayList<Pokemon> pokemons, Context context) {
        this.pokemonList = pokemons;
        this.context = context;
    }
    //Metodo para crear el View Holder
    @NonNull
    @Override
    public PokedexViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCardviewBinding binding = ItemCardviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PokedexViewHolder(binding);
    }
    //Metodo para vincular el ViewHolder y el Pokemon
    @Override
    public void onBindViewHolder(@NonNull PokedexViewHolder holder, int position) {
        Pokemon currentPokemon = this.pokemonList.get(position);
        holder.bind(currentPokemon);
        holder.itemView.setOnClickListener(view->((MainActivity) context).pokemonCatch(currentPokemon, view));

    }
    //Metodo para contar el número de elementos de la lista del RV
    @Override
    public int getItemCount() {
        return this.pokemonList.size();
    }
}
