package fr.hanquezr.automatedbarapp.bdd.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.hanquezr.automatedbarapp.bdd.dao.builder.SimpleRequestFilter;
import fr.hanquezr.automatedbarapp.model.Bottle;

public class BottleDao extends AbstractDao {

    public BottleDao(Context context) {
        super(context);
    }

    public Bottle readBottleFromId (String id) {
        SimpleRequestFilter simpleRequestFilter = new SimpleRequestFilter("BOTTLE_TABLE");
        simpleRequestFilter.addArgument("id", id);
        simpleRequestFilter.buildArgument();

        Cursor c = mDb.rawQuery(simpleRequestFilter.getQuery(), simpleRequestFilter.getArguments());

        if (c.moveToNext()) {
            Bottle bottle = new Bottle();
            bottle.setId(c.getInt(0));
            bottle.setName(c.getString(1));
            bottle.setMaxCapacity(c.getDouble(2));
            bottle.setActuCapacity(c.getDouble(3));
            bottle.setViscous(!"0".equals(c.getString(4)));

            return bottle;
        }

        return null;
    }

    public Bottle readBottleFromNameAndMaxCapacity (String name, String maxCapacity) {
        SimpleRequestFilter simpleRequestFilter = new SimpleRequestFilter("BOTTLE_TABLE");
        simpleRequestFilter.addArgument("name", name);
        simpleRequestFilter.addArgument("maxCapacity", maxCapacity);
        simpleRequestFilter.buildArgument();

        Cursor c = mDb.rawQuery(simpleRequestFilter.getQuery(), simpleRequestFilter.getArguments());

        if (c.moveToNext()) {
            Bottle bottle = new Bottle();
            bottle.setId(c.getInt(0));
            bottle.setName(c.getString(1));
            bottle.setMaxCapacity(c.getDouble(2));
            bottle.setActuCapacity(c.getDouble(3));
            bottle.setViscous(!"0".equals(c.getString(4)));

            return bottle;
        }

        return null;
    }

    public List<Bottle> readAllBottleFromName (String name) {
        SimpleRequestFilter simpleRequestFilter = new SimpleRequestFilter("BOTTLE_TABLE");
        simpleRequestFilter.addArgument("name", name);
        simpleRequestFilter.buildArgument();

        Cursor c = mDb.rawQuery(simpleRequestFilter.getQuery(), simpleRequestFilter.getArguments());

        List<Bottle> bottles = new ArrayList<>();

        while (c.moveToNext()) {
            Bottle bottle = new Bottle();
            bottle.setId(c.getInt(0));
            bottle.setName(c.getString(1));
            bottle.setMaxCapacity(c.getDouble(2));
            bottle.setActuCapacity(c.getDouble(3));
            bottle.setViscous(!"0".equals(c.getString(4)));
            bottles.add(bottle);
        }

        return bottles;
    }

    public List<Bottle> readAllBottle () {
        SimpleRequestFilter simpleRequestFilter = new SimpleRequestFilter("BOTTLE_TABLE");
        simpleRequestFilter.buildArgument();

        Cursor c = mDb.rawQuery(simpleRequestFilter.getQuery(), simpleRequestFilter.getArguments());

        List<Bottle> bottles = new ArrayList<>();

        while (c.moveToNext()) {
            Bottle bottle = new Bottle();
            bottle.setId(c.getInt(0));
            bottle.setName(c.getString(1));
            bottle.setMaxCapacity(c.getDouble(2));
            bottle.setActuCapacity(c.getDouble(3));
            bottle.setViscous(!"0".equals(c.getString(4)));

            bottles.add(bottle);
        }

        return bottles;
    }

    public List<String> readAllBottleName () {
        String query = "SELECT DISTINCT BOTTLE_TABLE." + BOTTLE_NAME + " FROM BOTTLE_TABLE";

        Cursor c = mDb.rawQuery(query, new String[0]);
        List<String> bottleNames = new ArrayList<>();

        while (c.moveToNext()) {
            bottleNames.add(c.getString((0)));
        }

        return bottleNames;
    }

    public void createBottle (Bottle bottle) {
        ContentValues values = new ContentValues();
        values.put(BOTTLE_NAME, bottle.getName());
        values.put(BOTTLE_MAX_CAPACITY, bottle.getMaxCapacity());
        values.put(BOTTLE_ACTU_CAPACITY, bottle.getActuCapacity());
        values.put(BOTTLE_VISCOUS, bottle.isViscous());

        mDb.insert("BOTTLE_TABLE", null, values);
    }

    public void updateBottle (Bottle bottle) {
        ContentValues values = new ContentValues();
        values.put(BOTTLE_NAME, bottle.getName());
        values.put(BOTTLE_MAX_CAPACITY, bottle.getMaxCapacity());
        values.put(BOTTLE_ACTU_CAPACITY, bottle.getActuCapacity());
        values.put(BOTTLE_VISCOUS, bottle.isViscous());

        mDb.update("BOTTLE_TABLE", values, BOTTLE_ID + " = ?", new String[]{String.valueOf(bottle.getId())});
    }

    public void cleanData () {
        mDb.delete("BOTTLE_TABLE", null, null);
    }

}
