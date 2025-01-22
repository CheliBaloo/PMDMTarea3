package pmdm.clopez.pmdmtarea3;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;

import pmdm.clopez.pmdmtarea3.databinding.FragmentCatchsBinding;

//Clase que controla el fragmento de pokemon capturados
public class CatchsFragment extends Fragment {
    FragmentCatchsBinding binding;
    //Iniciamos usuario de firebase y la base de datos de Firestore para posibles usos
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.catchRv.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));

        //------------RECYCLER VIEW----------------

        //Lista de pokemon capturados que mostrará en el recycler view
        ArrayList<Pokemon> pokemonList = new ArrayList<>();

        //Recogemos los pokemon guardados en la db
        db.collection(user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Pokemon poke = document.toObject(Pokemon.class);
                        pokemonList.add(poke); //lo añadimos a la lista
                        pokemonList.sort(new Comparator<Pokemon>() { //los ordenamos según su ID
                            @Override public int compare(Pokemon p1, Pokemon p2) {
                                return Integer.compare(p1.getId(), p2.getId());
                            }
                        });
                        binding.catchRv.getAdapter().notifyDataSetChanged(); //Avisamos al recycler view para que se actualice, ya que la llamada a la db es asíncrona
                    }
                } else {
                    System.out.println("Error al obtener documentos: " + task.getException());
                }
            }
        });
        //Le indicamos al recycler view su adaptador y la lista a usar
        CatchsRecyclerViewAdapter adapter = new CatchsRecyclerViewAdapter(pokemonList, binding.getRoot().getContext());
        binding.catchRv.setAdapter(adapter);


        //-----------------OPCIÓN DE ELIMINAR POKEMON-----------------

        //consultamos en sharedPreferences si está activa la opción de eliminar pokemon capturados
        SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(binding.getRoot().getContext());
        boolean deleting = sharedPreference.getBoolean("delete_option", false);

        if(deleting){ //si lo está, activamos la opción de deslizar el CardView para eliminar
            ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                @Override public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    // No necesitamos mover ítems en este caso
                    return false;
                }
                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    // Definimos la funcionalidad al deslizar
                    int position = viewHolder.getAbsoluteAdapterPosition();
                    //Preguntamos por un AlertDialog si está seguro de eliminarlo
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.delete).setMessage(R.string.ifSure).setPositiveButton(R.string.accept, (dialog, id) -> {
                        removePokemon(pokemonList, position);
                    }).setNegativeButton(R.string.cancel, (dialog, id)->{
                        adapter.notifyItemChanged(position);
                        dialog.cancel();
                    });
                    builder.show();
                }
                @Override public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                    super.clearView(recyclerView, viewHolder);
                    // Vuelve a notificar el ítem para que regrese a su lugar
                    adapter.notifyItemChanged(viewHolder.getAbsoluteAdapterPosition()); }
            };
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback); itemTouchHelper.attachToRecyclerView(binding.catchRv);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCatchsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    //Metodo para eliminar pokemon de la lista de capturados
    public void removePokemon(ArrayList<Pokemon> pokemonList, int position){
        String pokeName = pokemonList.get(position).getName();
        db.collection(user.getEmail()).document(pokeName)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FireStore", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FireStore", "Error deleting document", e);
                    }
                });
        //Borramos el pokemon de la lista y actualizamos la actividad con los cambios realizados
        pokemonList.remove(position);
        getActivity().recreate();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Cambia el título de la ActionBar
        if (getActivity() != null) {
            //Le damos el título a la actividad que nos indica qué fragmento está activo
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(R.string.mypokemon);
            }
        }
    }

}