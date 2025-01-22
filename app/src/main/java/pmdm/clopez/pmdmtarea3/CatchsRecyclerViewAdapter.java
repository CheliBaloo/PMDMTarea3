package pmdm.clopez.pmdmtarea3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pmdm.clopez.pmdmtarea3.databinding.CatchItemCardviewBinding;
//Clase que funcionará de adaptador del Recycler View que muestra los pokemons capturados
public class CatchsRecyclerViewAdapter extends RecyclerView.Adapter<CatchsViewHolder> {
    private final ArrayList<Pokemon> pokemonList;
    private final Context context;
    public CatchsRecyclerViewAdapter(ArrayList<Pokemon> pokemons, Context context) {
        this.pokemonList = pokemons;
        this.context = context;
    }
    //Metodo para crear el View Holder
    @NonNull
    @Override
    public CatchsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CatchItemCardviewBinding binding = CatchItemCardviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CatchsViewHolder(binding);
    }
    //Metodo para vincular el ViewHolder y el Pokemon
    @Override
    public void onBindViewHolder(@NonNull CatchsViewHolder holder, int position) {
        Pokemon currentPokemon = this.pokemonList.get(position);
        holder.bind(currentPokemon);
        holder.itemView.setOnClickListener(view->((MainActivity) context).pokemonSelect(currentPokemon, view));

    }
    //Metodo para contar el número de elementos de la lista del RV
    @Override
    public int getItemCount() {
        return this.pokemonList.size();
    }
}
