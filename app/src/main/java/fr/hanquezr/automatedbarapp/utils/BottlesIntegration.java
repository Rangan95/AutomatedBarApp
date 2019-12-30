package fr.hanquezr.automatedbarapp.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import fr.hanquezr.automatedbarapp.bdd.dao.BottleDao;
import fr.hanquezr.automatedbarapp.model.Bottle;

public class BottlesIntegration {

    private Context context;

    public BottlesIntegration(Context context) {
        this.context = context;
    }

    public void integrateBottlesFromFile() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.context.getAssets().open("Bottles.txt")));
        BottleDao bottleDao = new BottleDao(this.context);
        bottleDao.open();
        String line = null;

        while((line = reader.readLine()) != null) {
            String[] tabs = line.split(";");
            Bottle bottle = new Bottle();
            bottle.setName(tabs[0]);
            bottle.setMaxCapacity(Double.valueOf(tabs[1]));
            bottle.setActuCapacity(Double.valueOf(tabs[1]));
            bottle.setViscous(Boolean.valueOf(tabs[2]));

            bottleDao.createBottle(bottle);
        }

        bottleDao.close();
        reader.close();
    }

}
