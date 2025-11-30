package com.app.services.implementations;

import com.app.exceptions.BadRequestException;
import com.app.exceptions.DuplicateResourceException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.models.dtos.LibrarianDTO;
import com.app.models.entities.Librarian;
import com.app.models.enums.LibrarianRoles;
import com.app.models.requests.CreateLibrarianRequest;
import com.app.repositories.ILibrarianRepository;
import com.app.services.ILibrarianService;
import com.app.utils.DuiValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LibrarianServiceImpl implements ILibrarianService {

    private final ILibrarianRepository librarianRepository;

    @Override
    public LibrarianDTO save(CreateLibrarianRequest librarian) {
        if(!DuiValidator.isValidDUI(librarian.getDui())) throw new BadRequestException("The DUI entered is invalid");
        if(librarianRepository.existsByDui(librarian.getDui())) throw new DuplicateResourceException("DUI: " + librarian.getDui() +" already exists.");
        if(librarianRepository.existsByEmail(librarian.getEmail())) throw new DuplicateResourceException("Email: " + librarian.getEmail() +" already exists.");


        return mapToDTO(librarianRepository.save(Librarian
                .builder()
                .firstName(librarian.getFirstName())
                .lastName(librarian.getLastName())
                .dui(librarian.getDui())
                .phoneNumber(librarian.getPhoneNumber())
                .address(librarian.getAddress())
                .email(librarian.getEmail())
                .password(librarian.getPassword())
                .hiredOn(LocalDate.now())
                .librarianRole(LibrarianRoles.LIBRARIAN)
                .isActive(true)
                .build()));
    }

    @Override
    public LibrarianDTO findById(Long id) {
        return mapToDTO(librarianRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Librarian with id: " + id + " not found.")));
    }

    @Override
    public LibrarianDTO findByDui(String dui) {
        if(!DuiValidator.isValidDUI(dui)) throw new BadRequestException("The DUI entered is invalid");

        return mapToDTO(librarianRepository.findByDui(dui).orElseThrow(() -> new ResourceNotFoundException("Librarian with DUI: " + dui + " not found.")));
    }

    @Override
    public LibrarianDTO findByEmail(String email) {
        return mapToDTO(librarianRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Librarian with email: " + email + " not found.")));
    }

    @Override
    public List<LibrarianDTO> findAllActiveLibrarians() {
        return librarianRepository.findAllByIsActive(true).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<LibrarianDTO> findAll() {
        return librarianRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<LibrarianDTO> findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName) {
        return librarianRepository.findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(firstName, lastName).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public LibrarianDTO update(Long id, CreateLibrarianRequest librarian) {
        if(!DuiValidator.isValidDUI(librarian.getDui())) throw new BadRequestException("The DUI entered is invalid");

        Librarian librarianToUpdate = librarianRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Librarian with id: " + id + " not found."));

        if (!librarianToUpdate.getDui().equals(librarian.getDui())) {
            if (librarianRepository.existsByDui(librarian.getDui())) {
                throw new DuplicateResourceException("DUI: " + librarian.getDui() + " already exists.");
            }
        }
        if(!librarianToUpdate.getEmail().equals(librarian.getEmail())) {
            if(librarianRepository.existsByEmail(librarian.getEmail())) throw new DuplicateResourceException("Email: " + librarian.getEmail() +" already exists.");
        }
        librarianToUpdate.setFirstName(librarian.getFirstName());
        librarianToUpdate.setLastName(librarian.getLastName());
        librarianToUpdate.setDui(librarian.getDui());
        librarianToUpdate.setPhoneNumber(librarian.getPhoneNumber());
        librarianToUpdate.setAddress(librarian.getAddress());
        librarianToUpdate.setEmail(librarian.getEmail());
        librarianToUpdate.setPassword(librarian.getPassword());
        return mapToDTO(librarianRepository.save(librarianToUpdate));
    }

    @Override
    public void delete(Long id) {
        Librarian librarianToUpdate = librarianRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Librarian with id: " + id + " not found."));
        librarianToUpdate.setActive(false);
        librarianRepository.save(librarianToUpdate);
    }

    private LibrarianDTO mapToDTO(Librarian librarian) {
        return LibrarianDTO
                .builder()
                .id(librarian.getId())
                .firstName(librarian.getFirstName())
                .lastName(librarian.getLastName())
                .dui(librarian.getDui())
                .email(librarian.getEmail())
                .phoneNumber(librarian.getPhoneNumber())
                .address(librarian.getAddress())
                .hiredOn(librarian.getHiredOn())
                .librarianRole(librarian.getLibrarianRole().name())
                .isActive(librarian.isActive())
                .build();
    }
}
