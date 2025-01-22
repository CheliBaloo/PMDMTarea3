package pmdm.clopez.pmdmtarea3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import pmdm.clopez.pmdmtarea3.databinding.FragmentInfoBinding;
//Clase que controla la creación y funcionamiento del Fragmento que muestra la info del pokemon seleccionado
public class InfoFragment extends Fragment {
    FragmentInfoBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtenemos los datos del argumento que inicia este fragmento y se guardan en variables
        if (getArguments() != null) {
            String name = getArguments().getString("name");
            String url_img = getArguments().getString("img");
            float weight = getArguments().getInt("weight");
            float height =getArguments().getInt("height");
            int id = getArguments().getInt("id");
            String type1 = getArguments().getString("type1");
            String type2 = getArguments().getString("type2");

            //Vinculamos los datos obtenidos del fragmento anterior con el View del fragmento
            binding.infoName.setText(String.format("%d - %s", id, name.toUpperCase()));
            binding.infoHeight.setText(String.format("%s %.0f cm",getString(R.string.info_height), height*10));
            binding.infoWeight.setText(String.format("%s %.2f kg",getString(R.string.info_weight), weight*0.1));
            Picasso.with(binding.getRoot().getContext()).load(url_img).into(binding.infoImg);
            binding.infoType1.setImageResource(MainActivity.imgTypes.get(type1));
            System.out.println(type2);
            if (type2!=null){ //Comprobamos si hay un segundo tipo
                binding.infoType2.setImageResource(MainActivity.imgTypes.get(type2));
            }
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
                actionBar.setTitle(R.string.info_title);
            }
        }
    }
}