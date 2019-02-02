package fr.hanquezr.automatedbarapp.serverCall;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import fr.hanquezr.automatedbarapp.bdd.dao.BottleDao;
import fr.hanquezr.automatedbarapp.bdd.dao.PlaceDao;
import fr.hanquezr.automatedbarapp.model.Bottle;
import fr.hanquezr.automatedbarapp.model.Cocktail;

public class ServerCallCocktail extends AsyncTask<Cocktail, Void, String> {

    private final static double timeForOneMl = 0.8;
    private final static double timeForOneMlForViscous = 3.1;

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Context context;
    private FragmentManager fragmentManager;

    public ServerCallCocktail (Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    private boolean checkBottles (Cocktail cocktail) {
        for (Map.Entry<String, Double> ingredient : cocktail.getIngredients().entrySet()) {
            BottleDao bottleDao = new BottleDao(context);
            bottleDao.open();
            List<Bottle> bottles = bottleDao.readAllBottleFromName(ingredient.getKey().replace(" ", "_"));
            bottleDao.close();
            int placeFound = -1;

            for (Bottle bottle : bottles) {
                PlaceDao placeDao = new PlaceDao(context);
                placeDao.open();
                int place = placeDao.readPlaceFromBottleId(String.valueOf(bottle.getId()));
                placeDao.close();

                if (placeFound == -1) {
                    placeFound = place;
                }
            }

            if (placeFound == -1) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected String doInBackground(Cocktail... params) {
        String error = null;
        if (checkBottles(params[0])) {
            error = open();

            if (error == null)
                error = runCocktail(params[0]);
            if (error == null)
                error = close();
        } else {
            error = "Il manque une ou plusieurs bouteille(s) sur les pompes";
        }

        return error;
    }

    @Override
    protected void onPostExecute (String error) {
        if (error != null ) {
            Toast.makeText(context, error, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Cocktail terminé ! ", Toast.LENGTH_SHORT).show();
            fragmentManager.popBackStack();

        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(context, "Préparation du cocktail en cours...", Toast.LENGTH_SHORT).show();
    }

    private String open () {
        String error = null;

        try {
            clientSocket = new Socket("192.168.1.89", 38608);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            error = "Erreur server : " + e.getMessage();
        }

        return error;
    }

    private String runCocktail (Cocktail cocktail) {
        String error = null;

        try {
            errorServerTreatment(in.readLine());
            out.println("200");
            out.println(runRequestConstructor(cocktail));
            errorServerTreatment(in.readLine());
            errorServerTreatment(in.readLine());
        } catch (Exception e) {
            error = "Erreur server : " + e.getMessage();
        }

        return error;
    }

    private String runRequestConstructor (Cocktail cocktail) {
        String message = "";

        message += cocktail.getIngredients().size() + " ";

        for (Map.Entry<String, Double> ingredient : cocktail.getIngredients().entrySet()) {
            BottleDao bottleDao = new BottleDao(context);
            bottleDao.open();
            List<Bottle> bottles = bottleDao.readAllBottleFromName(ingredient.getKey().replace(" ", "_"));
            bottleDao.close();
            int placeFound = -1;
            double time = timeForOneMl;

            for (Bottle bottle : bottles) {
                PlaceDao placeDao = new PlaceDao(context);
                placeDao.open();
                int place = placeDao.readPlaceFromBottleId(String.valueOf(bottle.getId()));
                placeDao.close();

                if (placeFound == -1) {
                    placeFound = place;
                    time = bottle.isViscous() ? timeForOneMlForViscous : timeForOneMl;
                }
            }

            message += placeFound + " ";
            message += ((ingredient.getValue() * 1000) * time) * 1000 + " ";
        }

        message = message.substring(0, message.length() - 1);

        return message;
    }

    public void errorServerTreatment (String serverMessage) throws Exception {
        if (!serverMessage.contains("200"))
            throw new Exception(serverMessage);
    }

    public String close () {
        String error = null;

        try {
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            error = "Erreur server : " + e.getMessage();
        }

        return error;
    }
}
