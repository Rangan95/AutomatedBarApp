package fr.hanquezr.automatedbarapp.model;

import java.util.Map;

public class Cocktail {

    private String name;
    private Map<String, Double> ingredients;

    public String getName() {
        return name;
    }

    public Map<String, Double> getIngredients() {
        return ingredients;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(Map<String, Double> ingredients) {
        this.ingredients = ingredients;
    }

}
