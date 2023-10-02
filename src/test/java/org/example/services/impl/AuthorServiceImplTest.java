package org.example.services.impl;

import org.example.models.Author;
import org.example.repositories.AuthorRepository;
import org.example.services.AuthorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {


    @Mock
    private AuthorRepository authorRepository;
    AuthorService authorService;

    @BeforeEach
    void setUp() {
        authorService = new AuthorServiceImpl(authorRepository);
    }

    @Test
    void findByIdTest() {
        Author author = new Author("Author");
        UUID id = UUID.randomUUID();
        author.setId(id);

        when(authorRepository.findById(id)).thenReturn(author);

        Author authorById = authorService.findById(id);

        verify(authorRepository, times(1)).findById(id);
        Assertions.assertEquals(author, authorById);
    }

    @Test
    void findAllTest() {
        List<Author> authors = getAuthorList(List.of("Author1", "Author2", "Author3"));

        when(authorRepository.findAll()).thenReturn(authors);

        List<Author> authorList = authorService.findAll();

        verify(authorRepository, times(1)).findAll();
        Assertions.assertEquals(3, authorList.size());
    }

    @Test
    void createTest() {
        Author authorCreate = new Author("Author Create");
        UUID id = UUID.randomUUID();

        when(authorRepository.save(authorCreate)).then((Answer<Author>) invocationOnMock -> {
            Author author = invocationOnMock.getArgument(0);
            author.setId(id);
            return author;
        });

        Author author = authorService.create(authorCreate);

        verify(authorRepository, times(1)).save(authorCreate);
        Assertions.assertNotNull(authorCreate.getId());
        Assertions.assertEquals(id, authorCreate.getId());
        Assertions.assertEquals(author, authorCreate);
    }

    @Test
    void updateByIdTest() {
        Author author = new Author("Author");
        UUID id = UUID.randomUUID();
        author.setId(id);

        when(authorRepository.updateById(id, author)).then((Answer<Author>) invocationOnMock -> {
            Author authorMockArgument = invocationOnMock.getArgument(1);
            authorMockArgument.setFullName("Update Author");
            return authorMockArgument;
        });

        Author updateById = authorService.updateById(id, author);

        verify(authorRepository, times(1)).updateById(id, author);
        Assertions.assertEquals(author, updateById);
        Assertions.assertEquals("Update Author", updateById.getFullName());
    }

    @Test
    void deleteTest() {
        Author author = new Author("Author");
        UUID id = UUID.randomUUID();
        author.setId(id);

        when(authorRepository.deleteById(id)).thenReturn(true);

        boolean delete = authorService.delete(id);

        verify(authorRepository, times(1)).deleteById(id);
        Assertions.assertTrue(delete);
    }

    private List<Author> getAuthorList(List<String> names) {
        return names.stream().map(e -> new Author(UUID.randomUUID(), e)).toList();
    }
}