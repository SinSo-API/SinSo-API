package com.vishalrashmika.sinso.api.Utils;

import com.vishalrashmika.sinso.api.Config.IDPatterns;

public class Utils {
    public static boolean isValidArtistId(String artistId) {
        if (artistId == null || artistId.trim().isEmpty()) {
            return false;
        }

        if (!IDPatterns.ARTIST_ID_PATTERN.matcher(artistId).matches()) {
            return false;
        }
        
        return true;
    }
}
