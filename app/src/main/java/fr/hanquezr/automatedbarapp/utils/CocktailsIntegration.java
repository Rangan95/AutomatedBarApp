package fr.hanquezr.automatedbarapp.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import fr.hanquezr.automatedbarapp.bdd.dao.BottleDao;
import fr.hanquezr.automatedbarapp.bdd.dao.CocktailDao;
import fr.hanquezr.automatedbarapp.model.Cocktail;

public class CocktailsIntegration {

    private Context context;

    public CocktailsIntegration(Context context) {
        this.context = context;
    }

    public void integrateCocktailsFromFile() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.context.getAssets().open("Cocktails.txt")));
        CocktailDao cocktailDao = new CocktailDao(this.context);
        BottleDao bottleDao = new BottleDao(context);
        cocktailDao.open();
        bottleDao.open();
        String line;

        while((line = reader.readLine()) != null) {
            String[] tabs = line.split(";");
            Cocktail cocktail = new Cocktail();
            cocktail.setName(tabs[0]);

            Map<String, Double> ingredients = new HashMap<>();

            for (int i = 1; i < (tabs.length - 1); i=i+2) {
                Log.i("cocktail", tabs[0] + " " + tabs[i] + " " + tabs[i+1]);
                if (bottleDao.readAllBottleFromName(tabs[i]).isEmpty()) {
                    cocktailDao.close();
                    bottleDao.close();
                    reader.close();
                    throw new Exception("Impossible de continuer, la bouteille " + tabs[i] + " du cocktail " + tabs[0] + " n'existe pas.");
                } else if (Double.valueOf(tabs[i+1]) > 150) {
                    cocktailDao.close();
                    bottleDao.close();
                    reader.close();
                    throw new Exception("Impossible de continuer, quantité trop élévé pour " + tabs[i] + " du cocktail " + tabs[0] + ".");
                }
                ingredients.put(tabs[i], (Double.valueOf(tabs[i+1]) / 1000));
            }

            cocktail.setIngredients(ingredients);

            cocktailDao.createCocktail(cocktail);
        }

        cocktailDao.close();
        bottleDao.close();
        reader.close();
    }

}
