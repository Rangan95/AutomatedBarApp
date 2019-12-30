package fr.hanquezr.automatedbarapp.bdd.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import fr.hanquezr.automatedbarapp.bdd.DatabaseHandler;

public class Dao {

    protected final static int VERSION = 10;
    protected final static String NAME = "database.db";

    protected SQLiteDatabase mDb = null;
    protected DatabaseHandler mHandler = null;

    protected static final String BOTTLE_ID = "id";
    protected static final String BOTTLE_NAME = "name";
    protected static final String BOTTLE_MAX_CAPACITY = "maxCapacity";
    protected static final String BOTTLE_ACTU_CAPACITY = "actuCapacity";
    protected static final String BOTTLE_VISCOUS = "viscous";

    protected static final String COCKTAIL_NAME = "name";

    protected static final String ASSO_BOTTLE_NAME = "bottle_name";
    protected static final String ASSO_COCKTAIL_NAME = "cocktail_name";
    protected static final String ASSO_QUANTITY = "quantity";

    protected static final String PLACE_NUMBER = "number";
    protected static final String PLACE_BOTTLE_ID = "bottle_id";

    protected Context context;

    public Dao(Context context) {
        this.context = context;
        mHandler = new DatabaseHandler(context, NAME, null, VERSION);
    }

    public void open () {
        mDb = mHandler.getWritableDatabase();
    }

    public void close () {
        mDb.close();
    }

    public void cleanData() {
        mDb.delete("PLACE_TABLE", null, null);
        mDb.delete("ASSO_TABLE", null, null);
        mDb.delete("BOTTLE_TABLE", null, null);
        mDb.delete("COCKTAIL_TABLE", null, null);
    }

}
