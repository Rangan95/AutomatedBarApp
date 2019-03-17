package fr.hanquezr.automatedbarapp.bdd.dao.builder;

import java.util.HashMap;
import java.util.Map;

public class SimpleRequestFilter {

    private String query;
    private String[] arguments;
    private Map<String, String> argumentValues;

    public SimpleRequestFilter(String tableName) {
        argumentValues = new HashMap<>();
        query = "SELECT * FROM " + tableName;
    }

    public void buildArgument () {
        arguments = new String[argumentValues.size()];

        if (!argumentValues.isEmpty()) {
            query += " WHERE";
            int cpt = 0;

            for (String key : argumentValues.keySet()) {
                query += " " + key + " LIKE ?";
                arguments[cpt] = "%" + argumentValues.get(key) + "%";
            }
        }
    }

    public void addArgument (String key, String value) {
        if (!"".equals(value)) {
            argumentValues.put(key, value);
        }
    }

    public String getQuery () {
        return query;
    }

    public String[] getArguments() {
        return arguments;
    }

}
