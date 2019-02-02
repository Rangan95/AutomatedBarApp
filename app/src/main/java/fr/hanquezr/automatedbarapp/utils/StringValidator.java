package fr.hanquezr.automatedbarapp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringValidator {

    public boolean ipAddressValidator (String ipAddress) {
        Pattern pattern = Pattern.compile("([0-9]{1,3}\\.){3}[0-9]{1,3}");
        Matcher mattcher = pattern.matcher(ipAddress);
        return mattcher.find();
    }

}
