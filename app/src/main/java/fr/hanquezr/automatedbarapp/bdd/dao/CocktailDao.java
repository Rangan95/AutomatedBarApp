package fr.hanquezr.automatedbarapp.bdd.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.hanquezr.automatedbarapp.bdd.dao.builder.SimpleRequestFilter;
import fr.hanquezr.automatedbarapp.model.Cocktail;

public class CocktailDao extends AbstractDao {

    public CocktailDao(Context context) {
        super(context);
    }

    public Cocktail read (String cocktailName) {
        SimpleRequestFilter filterCocktailRequestBuilder = new SimpleRequestFilter("COCKTAIL_TABLE");
        filterCocktailRequestBuilder.addArgument(COCKTAIL_NAME, cocktailName);
        filterCocktailRequestBuilder.buildArgument();

        Cursor c = mDb.rawQuery(filterCocktailRequestBuilder.getQuery(),
                filterCocktailRequestBuilder.getArguments());

        if (c.moveToNext()) {
            Cocktail cocktail = new Cocktail();
            cocktail.setName(c.getString(0));

            cocktail.setIngredients(getIngredients(cocktail));

            return cocktail;
        }

        return null;
    }

    public List<Cocktail> readWithFilter (String cocktailName, String bottleName) {
        SimpleRequestFilter filterCocktailRequestBuilder = new SimpleRequestFilter("COCKTAIL_TABLE");
        filterCocktailRequestBuilder.addArgument(COCKTAIL_NAME, cocktailName);
        filterCocktailRequestBuilder.buildArgument();

        Cursor c = mDb.rawQuery(filterCocktailRequestBuilder.getQuery(),
                filterCocktailRequestBuilder.getArguments());
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

    private Map<String, Double> getIngredients (Cocktail cocktail) {
        String queryAsso = "SELECT * FROM ASSO_TABLE WHERE " + ASSO_COCKTAIL_NAME + " = ?";

        Cursor cAsso = mDb.rawQuery(queryAsso, new String[] {cocktail.getName()});
        Map<String, Double> ingredients = new HashMap<>();

        while (cAsso.moveToNext()) {
            ingredients.put(cAsso.getString(0), Double.valueOf(cAsso.getString(2)));
        }

        return ingredients;
    }

}
