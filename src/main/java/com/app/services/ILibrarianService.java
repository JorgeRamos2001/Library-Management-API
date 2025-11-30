package com.app.services;

import com.app.models.dtos.LibrarianDTO;
import com.app.models.dtos.MemberDTO;
import com.app.models.requests.CreateLibrarianRequest;
import com.app.models.requests.CreateMemberRequest;

import java.util.List;

public interface ILibrarianService {
    LibrarianDTO save(CreateLibrarianRequest librarian);
    LibrarianDTO findById(Long id);
    LibrarianDTO findByDui(String dui);
    LibrarianDTO findByEmail(String email);
    List<LibrarianDTO> findAllActiveLibrarians();
    List<LibrarianDTO> findAll();
    List<LibrarianDTO> findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    LibrarianDTO update(Long id, CreateLibrarianRequest librarian);
    void delete(Long id);
}
