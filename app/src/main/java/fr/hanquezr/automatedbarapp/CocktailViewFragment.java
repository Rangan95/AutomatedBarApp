package fr.hanquezr.automatedbarapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;

import fr.hanquezr.automatedbarapp.bdd.dao.CocktailDao;
import fr.hanquezr.automatedbarapp.model.Cocktail;
import fr.hanquezr.automatedbarapp.serverCall.ServerCallCocktail;

public class CocktailViewFragment extends Fragment {

    public CocktailViewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cocktail_view, container, false);

        ((ImageView)rootView.findViewById(R.id.cocktail_view_image)).setImageResource(R.drawable.roman_cosmo_martini_cocktail);

        CocktailDao cocktailDao = new CocktailDao(getContext());
        cocktailDao.open();
        final Cocktail cocktail = cocktailDao.read(getArguments().getString("cocktailName"));

        ((TextView)rootView.findViewById(R.id.view_cocktail_name)).setText(cocktail.getName());



        for (Map.Entry<String, Double> ingredient : cocktail.getIngredients().entrySet()) {
            ((TextView)rootView.findViewById(R.id.view_cocktail_textview)).setText(((TextView)rootView.findViewById(R.id.view_cocktail_textview)).getText().toString()
                    + ingredient.getKey() + " " + (ingredient.getValue() * 1000) + "ml\n");
        }

        rootView.findViewById(R.id.view_cocktail_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        rootView.findViewById(R.id.view_cocktail_launch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerCallCocktail serverCallCocktail = new ServerCallCocktail(getContext(), getFragmentManager());
                serverCallCocktail.execute(cocktail);
            }
        });

        rootView.findViewById(R.id.view_cocktail_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CocktailDao cocktailDao = new CocktailDao(getContext());
                cocktailDao.open();
                cocktailDao.removeCocktail(cocktail.getName());
                cocktailDao.close();
                getFragmentManager().popBackStack();
            }
        });

        return rootView;
    }

}
