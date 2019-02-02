package fr.hanquezr.automatedbarapp.bdd.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.hanquezr.automatedbarapp.model.Bottle;

public class PlaceDao extends AbstractDao {

    Context context;

    public PlaceDao(Context context) {
        super(context);
        this.context = context;
    }

    public Map<Integer, Bottle> readAllPlaces () {
        String query = "SELECT * FROM PLACE_TABLE";

        Cursor c = mDb.rawQuery(query, new String[0]);
        Map<Integer, Bottle> places = new HashMap<>();
        BottleDao bottleDao = new BottleDao(context);
        bottleDao.open();

        while (c.moveToNext()) {
            places.put(c.getInt(0), bottleDao.readBottleFromId(c.getString(1)));
        }

        bottleDao.close();

        return places;
    }

    public int readPlaceFromBottleId (String bottleID) {
        init();
        String query = "SELECT * FROM PLACE_TABLE WHERE " + PLACE_BOTTLE_ID + " = ?";

        Cursor c = mDb.rawQuery(query, new String[] {String.valueOf(bottleID)});

        if (c.moveToNext()) {
            return Integer.valueOf(c.getString(0));
        }

        return -1;
    }

    public Bottle readPlace (int place) {
        init();
        String query = "SELECT * FROM PLACE_TABLE WHERE " + PLACE_NUMBER + " = ?";

        Cursor c = mDb.rawQuery(query, new String[] {String.valueOf(place)});

        if (c.moveToNext()) {
            BottleDao bottleDao = new BottleDao(context);
            bottleDao.open();
            Bottle bottle = bottleDao.readBottleFromId(c.getString(1));
            bottleDao.close();

            return bottle;
        }

        return null;
    }

    public void updatePlace (int place, int bottleId) {
        init();
        ContentValues values = new ContentValues();
        values.put(PLACE_BOTTLE_ID, bottleId);

        mDb.update("PLACE_TABLE", values, PLACE_NUMBER + " = ?", new String[]{String.valueOf(place)});
    }

    private void init () {
        if (readAllPlaces().size() == 0) {
            for (int i = 0; i < 16; i++) {
                ContentValues values = new ContentValues();
                values.put(PLACE_NUMBER, i);
                values.put(PLACE_BOTTLE_ID, -1);

                mDb.insert("PLACE_TABLE", null, values);
            }
        }
    }

}
