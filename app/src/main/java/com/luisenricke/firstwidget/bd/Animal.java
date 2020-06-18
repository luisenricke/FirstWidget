package com.luisenricke.firstwidget.bd;

import com.luisenricke.firstwidget.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Animal {
    private String name;
    private int image;

    public Animal() {
        this.name = "";
        this.image = 0;
    }

    public Animal(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public ArrayList<Animal> getAnimals() {
        return new ArrayList<Animal>(Arrays.asList(
                new Animal("perro", R.drawable.perro),
                new Animal("ardilla", R.drawable.ardilla),
                new Animal("conejo", R.drawable.conejo),
                new Animal("gato", R.drawable.gato)
        ));


    }
}
