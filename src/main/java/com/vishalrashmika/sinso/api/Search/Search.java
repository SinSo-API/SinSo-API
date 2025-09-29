package com.vishalrashmika.sinso.api.Search;

import com.vishalrashmika.sinso.api.Songs.Songs;
import java.util.List;

public record Search(
    List<Songs> songs,
    int total,
    int page,
    int size,
    int totalPages,
    boolean hasNext,
    boolean hasPrevious
) {}