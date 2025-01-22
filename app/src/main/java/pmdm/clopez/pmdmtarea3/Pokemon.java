package pmdm.clopez.pmdmtarea3;

import java.util.List;

//Clase para obtener y guardar la información de pokemon desde la API
public class Pokemon {
    //Varibales para guardar los datos desde la API
    private int id;
    private String name;
    private Sprites sprites;
    private int weight;
    private int height;
    private List<Type> types;
    boolean catched; //variable para guardar si el pokemon ya ha sido capturado

    //METODOS GETTERS
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public List<Type> getTypes() {
        return types;
    }
    public boolean getCatched(){return catched;}

    //Metodo para marcar como capturado
    public void setCatched(boolean catched){this.catched = catched;}
}
//Clase para obtener la URL de la imagen del pokemon
class Sprites{
    private String front_default;
    public String getFront_default() {
        return front_default;
    }


}
//CLASES PARA OBTENER LOS TIPO, información anidada en la API
class Type {
    private TypeInfo type;

    public TypeInfo getType() {
        return type;
    }

}
class TypeInfo {
    private String name;

    public String getName() {
        return name;
    }

}

