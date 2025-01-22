package pmdm.clopez.pmdmtarea3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pmdm.clopez.pmdmtarea3.databinding.FragmentPokedexBinding;
//Clase que controla el fragmento que muestra la pokedex
public class PokedexFragment extends Fragment {

    FragmentPokedexBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPokedexBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //----CONFIGURAMOS EL RV ----------------
        //Utilizamos la lista creada en el MainActivity
        binding.recyclerView.setLayoutManager(new GridLayoutManager(binding.getRoot().getContext(),3));
        binding.recyclerView.setAdapter(new PokedexRecyclerViewAdapter(MainActivity.pokemonList, binding.getRoot().getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
        // Cambia el título de la ToolBar
        if (getActivity() != null) {
            //Le damos el título a la actividad que nos indica qué fragmento está activo
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(R.string.pokedex);
            }
        }
    }
}