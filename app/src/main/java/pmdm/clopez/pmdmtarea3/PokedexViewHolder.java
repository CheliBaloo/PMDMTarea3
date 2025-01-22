package pmdm.clopez.pmdmtarea3;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import pmdm.clopez.pmdmtarea3.databinding.ItemCardviewBinding;
//Clase que vincula la vista con los datos de los pokemon de la pokedex
public class PokedexViewHolder extends RecyclerView.ViewHolder {
    private final ItemCardviewBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public PokedexViewHolder(ItemCardviewBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    //metodo que indica como deben mostrarse los datos en el CardView
    public void bind(Pokemon pokemon){
        binding.pokeId.setText(Integer.toString(pokemon.getId()));
        binding.pokeName.setText(pokemon.getName().toUpperCase());
        Picasso.with(binding.getRoot().getContext()).load(pokemon.getSprites().getFront_default()).into(binding.pokeImg);
        binding.executePendingBindings();
        binding.cardView.setCardBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.screen));
        //Comprobamos si el pokemon ya ha sido capturado si se encuentra en la db
        db.collection(user.getEmail()).document(pokemon.getName()).get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                         if(document.exists()) {
                            binding.cardView.setCardBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.screen_dark)); //le cambiamos el color a su tarjeta
                            pokemon.setCatched(true); //lo marcamos como capturado
                            Log.d("FireStrore", "Obtenido correcto");
                        }else
                            pokemon.setCatched(false);
                    } else {
                        Log.e("FireStore", "Error getting documents.", task.getException());
                    }
                }
            });
    }
}
