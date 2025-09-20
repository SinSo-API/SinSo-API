package com.vishalrashmika.sinso.api.Config;

import java.util.regex.Pattern;

public class IDPatterns {
    public static final Pattern ARTIST_ID_PATTERN = Pattern.compile("^ART-\\d{5}$");
    public static final Pattern SONG_ID_PATTERN = Pattern.compile("^SNG-\\d{7}$");
    public static final Pattern LYRIC_ID_PATTERN = Pattern.compile("^LYR-\\d{7}$");
}
