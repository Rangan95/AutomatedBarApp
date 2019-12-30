package fr.hanquezr.automatedbarapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.hanquezr.automatedbarapp.bdd.dao.BottleDao;
import fr.hanquezr.automatedbarapp.bdd.dao.PlaceDao;
import fr.hanquezr.automatedbarapp.model.Bottle;

public class MyBottleRecyclerViewAdapter extends RecyclerView.Adapter<MyBottleRecyclerViewAdapter.ViewHolder> {

    public MyBottleRecyclerViewAdapter() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_bottle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.label.setText("Pompe " + (position+1));
        final List<String> data = new ArrayList<>();
        data.add("None");
        final BottleDao bottleDao = new BottleDao(holder.mView.getContext());
        bottleDao.open();

        for (Bottle bottle : bottleDao.readAllBottle()) {
            data.add(bottle.getName().replace("_", " ") + "-" + bottle.getMaxCapacity() + "L");
        }

        bottleDao.close();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(holder.mView.getContext(), R.layout.spinner_item, data);
        holder.spinner.setAdapter(spinnerAdapter);

        PlaceDao placeDao = new PlaceDao(holder.mView.getContext());
        placeDao.open();
        Bottle bottle = placeDao.readPlace(position);

        placeDao.close();

        if (bottle != null)
            holder.spinner.setSelection(spinnerAdapter.getPosition(bottle.getName().replace("_", " ") + "-" + bottle.getMaxCapacity() + "L"), false);
        else
            holder.spinner.setSelection(spinnerAdapter.getPosition("None"), false);
        final int place = position;
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (position == 0) {
                    PlaceDao placeDao = new PlaceDao(holder.mView.getContext());
                    placeDao.open();
                    placeDao.updatePlace(place, -1);
                    placeDao.close();
                } else {
                    final BottleDao bottleDaoSpinner = new BottleDao(holder.mView.getContext());
                    bottleDaoSpinner.open();
                    final Bottle bottle = bottleDaoSpinner.readBottleFromNameAndMaxCapacity(data.get(position).split("-")[0].replace(" ", "_"), data.get(position).split("-")[1].replace("L", ""));

                    PlaceDao placeDao = new PlaceDao(holder.mView.getContext());
                    placeDao.open();

                    new AlertDialog.Builder(holder.mView.getContext(), R.style.AlertDialogTheme)
                    .setCancelable(true)
                    .setTitle("Nouvelle bouteille")
                    .setMessage("Est-ce une nouvelle bouteille ?")
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BottleDao bottleDaoDialog = new BottleDao(holder.mView.getContext());
                            bottleDaoDialog.open();
                            bottle.setActuCapacity(bottle.getMaxCapacity());
                            bottleDaoDialog.updateBottle(bottle);
                            bottleDaoDialog.close();
                            Toast.makeText(holder.mView.getContext(), "Remise à zéro : " + bottle.getActuCapacity(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .create().show();

                    placeDao.updatePlace(place, bottle.getId());

                    placeDao.close();
                    bottleDaoSpinner.close();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    @Override
    public int getItemCount() {
        return 16;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final Spinner spinner;
        final TextView label;

        ViewHolder(View view) {
            super(view);
            mView = view;
            spinner = view.findViewById(R.id.spinner_bottle_place);
            label = view.findViewById(R.id.label_bottle_place);
        }

        @Override
        public String toString() {
            return label.getText().toString();
        }
    }
}
