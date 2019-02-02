package fr.hanquezr.automatedbarapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fr.hanquezr.automatedbarapp.model.Cocktail;
import fr.hanquezr.automatedbarapp.serverCall.ServerCallClean;
import fr.hanquezr.automatedbarapp.serverCall.ServerCallPurge;

public class AdminFragment extends Fragment {

    public AdminFragment() {
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
        View rootView =  inflater.inflate(R.layout.fragment_admin, container, false);

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

        return rootView;
    }
}
