package fr.hanquezr.automatedbarapp.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String BOTTLE_ID = "id";
    private static final String BOTTLE_NAME = "name";
    private static final String BOTTLE_MAX_CAPACITY = "maxCapacity";
    private static final String BOTTLE_ACTU_CAPACITY = "actuCapacity";
    private static final String BOTTLE_VISCOUS = "viscous";

    private static final String COCKTAIL_NAME = "name";

    private static final String ASSO_BOTTLE_NAME = "bottle_name";
    private static final String ASSO_COCKTAIL_NAME = "cocktail_name";
    private static final String ASSO_QUANTITY = "quantity";

    private static final String PLACE_NUMBER = "number";
    private static final String PLACE_BOTTLE_ID = "bottle_id";

    private static final String BOTTLE_TABLE_CREATE =
            "CREATE TABLE BOTTLE_TABLE (" +
                    BOTTLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    BOTTLE_NAME + " TEXT, " +
                    BOTTLE_MAX_CAPACITY + " TEXT, " +
                    BOTTLE_ACTU_CAPACITY + " TEXT, " +
                    BOTTLE_VISCOUS + " TEXT);";

    private static final String BOTTLE_TABLE_DROP = "DROP TABLE IF EXISTS BOTTLE_TABLE;";

    private static final String COCKTAIL_TABLE_CREATE =
            "CREATE TABLE COCKTAIL_TABLE (" +
                    COCKTAIL_NAME + " TEXT PRIMARY KEY);";

    private static final String COCKTAIL_TABLE_DROP = "DROP TABLE IF EXISTS COCKTAIL_TABLE;";

    private static final String ASSO_TABLE_CREATE =
            "CREATE TABLE ASSO_TABLE (" +
                    ASSO_BOTTLE_NAME + " TEXT, " +
                    ASSO_COCKTAIL_NAME + " TEXT, " +
                    ASSO_QUANTITY + " TEXT, " +
                    "PRIMARY KEY (" + ASSO_COCKTAIL_NAME + ", " + ASSO_BOTTLE_NAME + "), " +
                    "FOREIGN KEY (" + ASSO_COCKTAIL_NAME + ") REFERENCES COCKTAIL_TABLE(" + COCKTAIL_NAME + "));";

    private static final String ASSO_TABLE_DROP = "DROP TABLE IF EXISTS ASSO_TABLE;";

    private static final String PLACE_TABLE_CREATE =
            "CREATE TABLE PLACE_TABLE (" +
                    PLACE_NUMBER + " TEXT, " +
                    PLACE_BOTTLE_ID + " TEXT, "  +
                    "PRIMARY KEY (" + PLACE_NUMBER + ", " + PLACE_BOTTLE_ID + "));";

    private static final String PLACE_TABLE_DROP = "DROP TABLE IF EXISTS PLACE_TABLE;";

    public DatabaseHandler (Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(COCKTAIL_TABLE_CREATE);
        db.execSQL(BOTTLE_TABLE_CREATE);
        db.execSQL(ASSO_TABLE_CREATE);
        db.execSQL(PLACE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ASSO_TABLE_DROP);
        db.execSQL(PLACE_TABLE_DROP);
        db.execSQL(BOTTLE_TABLE_DROP);
        db.execSQL(COCKTAIL_TABLE_DROP);
        onCreate(db);
    }

}
