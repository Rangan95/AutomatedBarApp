package fr.hanquezr.automatedbarapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import fr.hanquezr.automatedbarapp.model.Cocktail;

public class CocktailFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 3;

    public CocktailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cocktail_list, container, false);

        // Set the adapter
        if (view.findViewById(R.id.cocktailRecycleView) instanceof RecyclerView) {
            Context context = view.findViewById(R.id.cocktailRecycleView).getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.cocktailRecycleView);
            recyclerView.setHasFixedSize(true);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyCocktailRecyclerViewAdapter(getContext(), this));
        }

        view.findViewById(R.id.fabCocktail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content, new AddCocktailFragment())
                        .addToBackStack("addCocktail")
                        .commit();
            }
        });

        return view;
    }

    public void goToCocktailView (String cocktailName) {
        Bundle args = new Bundle();
        args.putString("cocktailName", cocktailName);
        CocktailViewFragment f = new CocktailViewFragment();
        f.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content, f)
                .addToBackStack("cocktailView")
                .commit();
    }

}
