package fr.hanquezr.automatedbarapp;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fr.hanquezr.automatedbarapp.bdd.dao.CocktailDao;
import fr.hanquezr.automatedbarapp.model.Cocktail;
import fr.hanquezr.automatedbarapp.utils.Constant;
import fr.hanquezr.automatedbarapp.utils.PropertyUtils;

public class MyCocktailRecyclerViewAdapter extends RecyclerView.Adapter<MyCocktailRecyclerViewAdapter.ViewHolder> {

    private List<Cocktail> cocktails;
    private CocktailFragment cocktailFragment;

    public MyCocktailRecyclerViewAdapter(Context context, CocktailFragment cocktailFragment) {
        PropertyUtils propertyUtils = new PropertyUtils(context);
        CocktailDao cocktailDao = new CocktailDao(context);
        cocktailDao.open();
        cocktails = cocktailDao.readWithFilter(propertyUtils.getProperty(Constant.COCKTAIL_NAME_FILTER),
                propertyUtils.getProperty(Constant.BOTTLE_NAME_FILTER));
        cocktailDao.close();
        this.cocktailFragment = cocktailFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_cocktail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.cocktailImage.setImageResource(R.drawable.roman_cosmo_martini_cocktail);
        holder.cocktailName.setText(cocktails.get(position).getName());

        holder.cocktailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cocktailFragment.goToCocktailView(holder.cocktailName.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cocktails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView cocktailImage;
        final TextView cocktailName;

        ViewHolder(View view) {
            super(view);
            mView = view;
            cocktailImage = (ImageView) view.findViewById(R.id.cocktail_image);
            cocktailName = (TextView) view.findViewById(R.id.cocktail_name);
        }

        @Override
        public String toString() {
            return cocktailName.getText().toString();
        }
    }
}
