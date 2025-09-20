package com.vishalrashmika.sinso.api.Utils;

import java.util.regex.Pattern;

public class Utils {
    public static boolean isValidId(String Id, Pattern pattern) {
        if (Id == null || Id.trim().isEmpty()) {
            return false;
        }

        if (!pattern.matcher(Id).matches()) {
            return false;
        }
        
        return true;
    }
}
