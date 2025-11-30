package com.app.services;

import com.app.models.dtos.GenreDTO;
import com.app.models.entities.Genre;
import com.app.models.requests.CreateGenreRequest;

import java.util.List;

public interface IGenreService {
    GenreDTO findById(Long id);
    GenreDTO findByName(String name);
    List<GenreDTO> findAll();
    GenreDTO save(CreateGenreRequest genre);
}
