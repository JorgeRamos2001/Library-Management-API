package com.app.services.implementations;

import com.app.exceptions.OperationNotPermittedException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.models.dtos.AuthorDTO;
import com.app.models.entities.Author;
import com.app.models.requests.CreateAuthorRequest;
import com.app.repositories.IAuthorRepository;
import com.app.services.IAuthorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements IAuthorService {

    private final IAuthorRepository authorRepository;

    @Override
    public AuthorDTO save(CreateAuthorRequest author) {
        return mapToDTO(authorRepository.save(Author
                .builder()
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .biography(author.getBiography())
                .nationality(author.getNationality())
                .build()));
    }

    @Override
    public AuthorDTO findById(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author with: " + id + " not found."));
        return mapToDTO(author);
    }

    @Override
    public List<AuthorDTO> findAll() {
        return authorRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<AuthorDTO> findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName) {
        return authorRepository.findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(firstName, lastName).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public AuthorDTO update(Long id, CreateAuthorRequest author) {
        Author existingAuthor = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author with: " + id + " not found."));
        existingAuthor.setFirstName(author.getFirstName());
        existingAuthor.setLastName(author.getLastName());
        existingAuthor.setBiography(author.getBiography());
        existingAuthor.setNationality(author.getNationality());
        return mapToDTO(authorRepository.save(existingAuthor));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Author existingAuthor = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author with: " + id + " not found."));

        if (!existingAuthor.getBooks().isEmpty()) {
            throw new OperationNotPermittedException("Cannot delete author with associated books. Remove the books or association first.");
        }

        authorRepository.delete(existingAuthor);
    }

    private AuthorDTO mapToDTO(Author author) {
        return AuthorDTO.builder()
                .id(author.getId())
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .biography(author.getBiography())
                .nationality(author.getNationality())
                .build();
    }
}
