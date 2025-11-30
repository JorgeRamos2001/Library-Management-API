package com.app.services.implementations;

import com.app.exceptions.DuplicateResourceException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.models.dtos.GenreDTO;
import com.app.models.entities.Genre;
import com.app.models.requests.CreateGenreRequest;
import com.app.repositories.IGenreRepository;
import com.app.services.IGenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GenreServiceImpl implements IGenreService {
    private final IGenreRepository genreRepository;

    @Override
    public GenreDTO findById(Long id) {
        Genre genre = genreRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Genre with id:" + id + " not found."));
        return mapToDTO(genre);
    }

    @Override
    public GenreDTO findByName(String name) {
        Genre genre = genreRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Genre with name:" + name + " not found."));
        return mapToDTO(genre);
    }

    @Override
    public List<GenreDTO> findAll() {
        return genreRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public GenreDTO save(CreateGenreRequest genre) {
        if (genreRepository.existsByNameIgnoreCase(genre.getName())) throw new DuplicateResourceException("Genre already exists.");
        return mapToDTO(genreRepository.save(Genre
                .builder()
                .name(genre.getName())
                .build()));
    }

    private GenreDTO mapToDTO(Genre genre) {
        return GenreDTO
                .builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }
}
