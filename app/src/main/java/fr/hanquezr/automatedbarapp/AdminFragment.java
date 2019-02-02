package fr.hanquezr.automatedbarapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import fr.hanquezr.automatedbarapp.serverCall.ServerCallClean;
import fr.hanquezr.automatedbarapp.serverCall.ServerCallPurge;
import fr.hanquezr.automatedbarapp.serverCall.ServerCallTest;
import fr.hanquezr.automatedbarapp.utils.PropertyUtils;
import fr.hanquezr.automatedbarapp.utils.StringValidator;

public class AdminFragment extends Fragment {

    private PropertyUtils propertyUtils;

    public AdminFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        propertyUtils = new PropertyUtils(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView =  inflater.inflate(R.layout.fragment_admin, container, false);

        rootView.findViewById(R.id.purge_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerCallPurge serverCallPurge = new ServerCallPurge(getContext());
                serverCallPurge.execute();
            }
        });

        rootView.findViewById(R.id.clean_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerCallClean serverCallClean = new ServerCallClean(getContext());
                serverCallClean.execute();
            }
        });

        rootView.findViewById(R.id.test_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerCallTest serverCallTest = new ServerCallTest(getContext());
                serverCallTest.execute();
            }
        });

        ((EditText)rootView.findViewById(R.id.ip_address)).setText(propertyUtils.getProperty("ip_address"));

        rootView.findViewById(R.id.ip_save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringValidator stringValidator = new StringValidator();
                String addressIpFromEditText = ((EditText)rootView.findViewById(R.id.ip_address)).getText().toString();

                if (stringValidator.ipAddressValidator(addressIpFromEditText)) {
                    propertyUtils.saveProperty("ip_address", addressIpFromEditText);
                    Toast.makeText(getContext(), "Adresse IP enregistré", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "L'adresse IP est incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }
}
