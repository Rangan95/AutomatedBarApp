package fr.hanquezr.automatedbarapp.bdd.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.hanquezr.automatedbarapp.model.Cocktail;

public class CocktailDao extends AbstractDao {

    public CocktailDao(Context context) {
        super(context);
    }

    private Map<String, Double> getIngredients (Cocktail cocktail) {
        String queryAsso = "SELECT * FROM ASSO_TABLE WHERE " + ASSO_COCKTAIL_NAME + " = ?";

        Cursor cAsso = mDb.rawQuery(queryAsso, new String[] {cocktail.getName()});
        Map<String, Double> ingredients = new HashMap<>();

        while (cAsso.moveToNext()) {
            ingredients.put(cAsso.getString(0), Double.valueOf(cAsso.getString(2)));
        }

        return ingredients;
    }

    public Cocktail readCocktailFromName (String cocktailName) {
        String query = "SELECT * FROM COCKTAIL_TABLE WHERE " + COCKTAIL_NAME + " = ?";

        Cursor c = mDb.rawQuery(query, new String[] {cocktailName});

        if (c.moveToNext()) {
            Cocktail cocktail = new Cocktail();
            cocktail.setName(c.getString(0));

            cocktail.setIngredients(getIngredients(cocktail));

            return cocktail;
        }

        return null;
    }

    public List<Cocktail> readAllCocktail () {
        String query = "SELECT * FROM COCKTAIL_TABLE";

        Cursor c = mDb.rawQuery(query, new String[0]);
        List<Cocktail> cocktails = new ArrayList<>();

        while (c.moveToNext()) {
            Cocktail cocktail = new Cocktail();
            cocktail.setName(c.getString(0));
            cocktail.setIngredients(getIngredients(cocktail));
            cocktails.add(cocktail);
        }

        return cocktails;
    }

    public void createCocktail (Cocktail cocktail) {
        ContentValues values = new ContentValues();
        values.put(COCKTAIL_NAME, cocktail.getName());
        mDb.insert("COCKTAIL_TABLE", null, values);

        for (Map.Entry<String, Double> ingredient : cocktail.getIngredients().entrySet()) {
            ContentValues valuesAsso = new ContentValues();
            valuesAsso.put(ASSO_BOTTLE_NAME, ingredient.getKey());
            valuesAsso.put(ASSO_COCKTAIL_NAME, cocktail.getName());
            valuesAsso.put(ASSO_QUANTITY, String.valueOf(ingredient.getValue()));
            mDb.insert("ASSO_TABLE", null, valuesAsso);
        }
    }

    public void removeCocktail (String cocktailName) {
        mDb.delete("COCKTAIL_TABLE", COCKTAIL_NAME + " = ?", new String[] {cocktailName});
    }

}
