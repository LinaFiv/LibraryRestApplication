package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.models.Person;
import org.example.services.PersonService;
import org.example.servlets.DTO.PersonResponseDTO;
import org.example.servlets.mapper.PersonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeopleServletTest {

    @Mock
    PersonService personService;
    @Mock
    PersonMapper mapper;
    @Mock
    ObjectMapper objectMapper;
    @Mock
    PrintWriter printWriter;
    PeopleServlet peopleServlet;

    @BeforeEach
    void setUp() {
        peopleServlet = new PeopleServlet(personService, mapper);
    }

    @Test
    void doGet() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        List<Person> people = getPersonList(List.of("Person1", "Person2", "Person3"));
        List<PersonResponseDTO> personResponseDTOS = getPersonResponseDTOList(people);

        when(response.getWriter()).thenReturn(printWriter);
        when(personService.findAll()).thenReturn(people);
        when(mapper.mapToList(people)).thenReturn(personResponseDTOS);
        objectMapper.writeValueAsString(personResponseDTOS);

        peopleServlet.doGet(request, response);

        verify(personService, times(1)).findAll();
        verify(mapper, times(1)).mapToList(people);
        verify(objectMapper, times(1)).writeValueAsString(personResponseDTOS);
        verify(response, times(1)).setStatus(200);
        verify(response, times(1)).setContentType("application/json");
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(printWriter, times(1)).println(anyString());
    }

    private List<Person> getPersonList(List<String> names) {
        return names.stream().map(e -> new Person(UUID.randomUUID(), e, 2000)).toList();
    }

    private List<PersonResponseDTO> getPersonResponseDTOList(List<Person> personList) {
        return personList.stream().map(e -> new PersonResponseDTO(e.getId(), e.getFullName(), e.getYearOfBirth())).toList();
    }
}