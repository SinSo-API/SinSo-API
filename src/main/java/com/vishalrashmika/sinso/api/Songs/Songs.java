package com.vishalrashmika.sinso.api.Songs;

public record Songs(
    String songId,
    String songName,
    String songNameSinhala,
    String artistId,
    String artistName,
    String artistNameSinhala,
    String composer,
    String lyricist,
    String lyricsId,
    String lyricContent,
    String lyricContentSinhala
) {}

