package fr.hanquezr.automatedbarapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import fr.hanquezr.automatedbarapp.bdd.dao.BottleDao;
import fr.hanquezr.automatedbarapp.model.Bottle;


public class AddBottleFragment extends Fragment {

    public AddBottleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_add_bottle, container, false);

        rootView.findViewById(R.id.add_bottle_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        rootView.findViewById(R.id.add_bottle_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((EditText)rootView.findViewById(R.id.add_bottle_name)).getText().toString() != null
                        && ((EditText)rootView.findViewById(R.id.add_bottle_name)).getText().toString().length() > 0
                        && ((EditText)rootView.findViewById(R.id.add_bottle_max_capacity)).getText().toString() != null
                        && ((EditText)rootView.findViewById(R.id.add_bottle_max_capacity)).getText().toString().length() > 0) {
                    Bottle bottle = new Bottle();
                    bottle.setName(((EditText) rootView.findViewById(R.id.add_bottle_name)).getText().toString().replace(" ", "_"));
                    bottle.setMaxCapacity(Double.valueOf(((EditText) rootView.findViewById(R.id.add_bottle_max_capacity)).getText().toString()));
                    bottle.setActuCapacity(Double.valueOf(((EditText) rootView.findViewById(R.id.add_bottle_max_capacity)).getText().toString()));
                    bottle.setViscous(((Switch)rootView.findViewById(R.id.add_bottle_switch)).isChecked());
                    BottleDao bottleDao = new BottleDao(getContext());
                    bottleDao.open();
                    bottleDao.createBottle(bottle);
                    bottleDao.close();
                    getFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Veuillez renseigner tous les champs", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }
}
