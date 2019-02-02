package fr.hanquezr.automatedbarapp.serverCall;

import android.content.Context;
import android.os.AsyncTask;
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
import fr.hanquezr.automatedbarapp.utils.PropertyUtils;

public class ServerCallPurge extends AsyncTask<Void, Void, String> {

    private final static double timeForOneMl = 0.8;

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Context context;

    public ServerCallPurge (Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        String error = open();

        if (error == null)
            error = runPurge();
        if (error == null)
            error = close();

        return error;
    }

    @Override
    protected void onPostExecute (String error) {
        if (error != null ) {
            Toast.makeText(context, error, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Purge terminé ! ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(context, "Purge en cours...", Toast.LENGTH_SHORT).show();
    }

    private String open () {
        String error = null;
        PropertyUtils propertyUtils = new PropertyUtils(context);

        try {
            clientSocket = new Socket(propertyUtils.getProperty("ip_address"), 38608);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            error = "Erreur server : " + e.getMessage();
        }

        return error;
    }

    private String runPurge () {
        String error = null;

        try {
            errorServerTreatment(in.readLine());
            out.println("200");
            out.println(runRequestConstructor());
            errorServerTreatment(in.readLine());
            errorServerTreatment(in.readLine());
        } catch (Exception e) {
            error = "Erreur server : " + e.getMessage();
        }

        return error;
    }

    private String runRequestConstructor () {
        String message = "";

        message += 16 + " ";

        for (int i = 0; i < 16; i++) {
            message += i + " ";
            message += 10000 + " ";
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