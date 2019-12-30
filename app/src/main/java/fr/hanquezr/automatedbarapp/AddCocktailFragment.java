package fr.hanquezr.automatedbarapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.hanquezr.automatedbarapp.bdd.dao.BottleDao;
import fr.hanquezr.automatedbarapp.bdd.dao.CocktailDao;
import fr.hanquezr.automatedbarapp.model.Bottle;
import fr.hanquezr.automatedbarapp.model.Cocktail;

public class AddCocktailFragment extends Fragment {

    private Map<String, Double> ingredients;

    public AddCocktailFragment() {
        // Required empty public constructor
        ingredients = new HashMap<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_add_cocktail, container, false);

        final List<String> data = new ArrayList<>();
        BottleDao bottleDao = new BottleDao(getContext());
        bottleDao.open();

        for (Bottle bottle : bottleDao.readAllBottle()) {
            if (!data.contains(bottle.getName().replace("_", " ")))
                data.add(bottle.getName().replace("_", " "));
        }

        bottleDao.close();

        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, data);
        ((Spinner)rootView.findViewById(R.id.spinner_cocktail_bottle)).setAdapter(spinnerAdapter);

        rootView.findViewById(R.id.add_cocktail_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        rootView.findViewById(R.id.add_cocktail_ingredients).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredients.put(((Spinner)rootView.findViewById(R.id.spinner_cocktail_bottle)).getSelectedItem().toString(),
                        (Double.valueOf(((EditText)rootView.findViewById(R.id.edittext_cocktail_quantity)).getText().toString()) / 1000));
                ((TextView)rootView.findViewById(R.id.add_cocktail_textview)).setText(((TextView)rootView.findViewById(R.id.add_cocktail_textview)).getText().toString() +
                        ((Spinner)rootView.findViewById(R.id.spinner_cocktail_bottle)).getSelectedItem().toString() + " "
                        + ((EditText)rootView.findViewById(R.id.edittext_cocktail_quantity)).getText().toString() + "ml\n");
            }
        });

        rootView.findViewById(R.id.add_cocktail_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((EditText)rootView.findViewById(R.id.add_cocktail_name)).getText().toString() != null
                        && ((EditText)rootView.findViewById(R.id.add_cocktail_name)).getText().toString().length() > 0
                        && ingredients.size() > 0) {
                    Cocktail cocktail = new Cocktail();
                    cocktail.setName(((EditText) rootView.findViewById(R.id.add_cocktail_name)).getText().toString());
                    cocktail.setIngredients(ingredients);
                    CocktailDao cocktailDao = new CocktailDao(getContext());
                    cocktailDao.open();
                    cocktailDao.createCocktail(cocktail);
                    cocktailDao.close();
                    getFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Il manque des informations (nom ou au moins un ingrédient)", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }
}
