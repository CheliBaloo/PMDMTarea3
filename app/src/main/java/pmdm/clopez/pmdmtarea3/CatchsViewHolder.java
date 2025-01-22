package pmdm.clopez.pmdmtarea3;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import pmdm.clopez.pmdmtarea3.databinding.CatchItemCardviewBinding;
//Clase que vincula la vista con los datos de los pokemon capturados
public class CatchsViewHolder extends RecyclerView.ViewHolder{
    private final CatchItemCardviewBinding binding;
    public CatchsViewHolder(CatchItemCardviewBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    //metodo que indica como deben mostrarse los datos en el CardView
    public void bind(Pokemon pokemon){
        binding.catchPokeName.setText(pokemon.getName().toUpperCase()); //El nombre del pokemon
        Picasso.with(binding.getRoot().getContext()).load(pokemon.getSprites().getFront_default()).into(binding.catchPokeImg); //la imagen del pokemon
        binding.executePendingBindings();
        int i=1;
        for(Type type: pokemon.getTypes()){ //Para mostrar las imágenes de los tipos, debo controlar si tiene un tipo o dos
            if(i == 1)
                binding.catchType1.setImageResource(MainActivity.imgTypes.get(type.getType().getName()));
            else
                binding.catchType2.setImageResource(MainActivity.imgTypes.get(type.getType().getName()));
            i++;
        }
    }
}
