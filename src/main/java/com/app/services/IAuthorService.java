package com.app.services;

import com.app.models.dtos.AuthorDTO;
import com.app.models.requests.CreateAuthorRequest;

import java.util.List;

public interface IAuthorService {
    AuthorDTO save(CreateAuthorRequest author);
    AuthorDTO findById(Long id);
    List<AuthorDTO> findAll();
    List<AuthorDTO> findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    AuthorDTO update(Long id, CreateAuthorRequest author);
    void delete(Long id);
}
