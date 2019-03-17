package fr.hanquezr.automatedbarapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import fr.hanquezr.automatedbarapp.bdd.dao.BottleDao;
import fr.hanquezr.automatedbarapp.bdd.dao.PlaceDao;
import fr.hanquezr.automatedbarapp.model.Bottle;
import fr.hanquezr.automatedbarapp.utils.PropertyUtils;

public class SearchFragment extends Fragment {

    private final String BOTTLE_NAME_FILTER = "BOTTLE_NAME_FILTER";
    private final String COCKTAIL_NAME_FILTER = "COCKTAIL_NAME_FILTER";

    public SearchFragment () {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_search, container, false);

        final PropertyUtils propertyUtils = new PropertyUtils(getContext());

        EditText editText = view.findViewById(R.id.searchview_cocktail_name);

        if (!"".equals(propertyUtils.getProperty(COCKTAIL_NAME_FILTER)))
            editText.setText(propertyUtils.getProperty(COCKTAIL_NAME_FILTER));

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                propertyUtils.saveProperty(COCKTAIL_NAME_FILTER, s.toString());
            }
        });

        final List<String> spinnerData = new ArrayList<>();
        spinnerData.add("None");
        BottleDao bottleDao = new BottleDao(getContext());
        bottleDao.open();

        for (String bottleName : bottleDao.readAllBottleName()) {
            spinnerData.add(bottleName.replace("_", " "));
        }

        bottleDao.close();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, spinnerData);
        Spinner spinner = view.findViewById(R.id.searchview_spinner_alcool);
        spinner.setAdapter(spinnerAdapter);

        if (!"".equals(propertyUtils.getProperty(BOTTLE_NAME_FILTER)))
            spinner.setSelection(spinnerData.indexOf(propertyUtils.getProperty(BOTTLE_NAME_FILTER)));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                propertyUtils.saveProperty(BOTTLE_NAME_FILTER, spinnerData.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        return view;
    }

}
