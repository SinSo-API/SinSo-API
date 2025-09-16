package com.vishalrashmika.sinso.api.Lyrics;

public record Lyrics(
    String lyricId,
    String songId,
    String songName,
    String songNameSinhala,
    String artistId,
    String artistName,
    String artistNameSinhala,
    String lyricContent,
    String lyricContentSinhala
) {}