package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.models.Person;
import org.example.services.PersonService;
import org.example.servlets.DTO.AuthorRequestDTO;
import org.example.servlets.DTO.PersonRequestDTO;
import org.example.servlets.DTO.PersonResponseDTO;
import org.example.servlets.mapper.PersonMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServletTest {

    @Mock
    PersonService personService;
    @Mock
    PersonMapper mapper;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    PrintWriter printWriter;
    @Mock
    ObjectMapper objectMapper;
    @Mock
    BufferedReader reader;
    PersonServlet personServlet;

    @BeforeEach
    void setUp() {
        personServlet = new PersonServlet(personService, mapper);
    }

    @Test
    void doGet_validId_test() throws Exception {
        Person person = new Person(UUID.randomUUID(), "Person", 2000);
        PersonResponseDTO personResponseDTO = new PersonResponseDTO(person.getId(), person.getFullName(), person.getYearOfBirth());

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(String.valueOf(person.getId()));
        when(personService.findById(person.getId())).thenReturn(person);
        when(mapper.mapToResponseDTO(person)).thenReturn(personResponseDTO);
        objectMapper.writeValueAsString(personResponseDTO);

        personServlet.doGet(request, response);

        verify(response, times(1)).getWriter();
        verify(request, times(1)).getParameter("id");
        verify(personService, times(1)).findById(person.getId());
        verify(mapper, times(1)).mapToResponseDTO(person);
        verify(objectMapper, times(1)).writeValueAsString(personResponseDTO);
        verify(response, times(1)).setContentType("application/json");
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(printWriter, times(1)).write(anyString());

        Assertions.assertEquals(String.valueOf(person.getId()), request.getParameter("id"));
        Assertions.assertNotNull(objectMapper);
        Assertions.assertNotNull(printWriter);
    }

    @Test
    void doGet_invalidId_test() throws Exception {
        String invalidId = "s4tov";

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(invalidId);

        personServlet.doGet(request, response);

        verify(printWriter, times(1)).write("Invalid person ID");
        verify(response, times(1)).setStatus(400);

        Assertions.assertEquals(invalidId, request.getParameter("id"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> personService.findById(UUID.fromString(invalidId)));
    }

    @Test
    void doGet_nullId_test() throws Exception {
        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(null);

        personServlet.doGet(request, response);

        verify(printWriter, times(1)).write("ID must not be empty");
        verify(response, times(1)).setStatus(400);
        verify(personService, never()).findById(any());

        Assertions.assertNull(request.getParameter("id"));
    }

    @Test
    void doGet_notFoundById_test() throws Exception {
        UUID notFoundId = UUID.randomUUID();

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(String.valueOf(notFoundId));
        when(personService.findById(notFoundId)).thenReturn(null);

        personServlet.doGet(request, response);

        verify(printWriter, times(1)).write("Person not found by ID " + notFoundId);
        verify(response, times(1)).setStatus(404);

        Assertions.assertNull(personService.findById(notFoundId));
    }

    @Test
    void doPost() throws Exception {
        PersonRequestDTO personRequestDTO = new PersonRequestDTO("Person", 1990);
        Person createPerson = new Person(UUID.randomUUID(), "Person", 1990);
        PersonResponseDTO personResponseDTO = new PersonResponseDTO(createPerson.getId(), createPerson.getFullName(), createPerson.getYearOfBirth());
        String requestBody = "{\"fullName\": \"Person\", \"yearOfBirth\": 1990}";

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getReader()).thenReturn(reader);
        when(reader.readLine()).thenReturn(requestBody, null);
        objectMapper.readValue(requestBody, PersonRequestDTO.class);
        when(mapper.mapToEntity(personRequestDTO)).thenReturn(createPerson);
        when(personService.create(createPerson)).thenReturn(createPerson);
        when(mapper.mapToResponseDTO(createPerson)).thenReturn(personResponseDTO);
        objectMapper.writeValueAsString(personResponseDTO);

        personServlet.doPost(request, response);

        verify(request, times(1)).getReader();
        verify(objectMapper, times(1)).readValue(requestBody, PersonRequestDTO.class);
        verify(mapper, times(1)).mapToEntity(personRequestDTO);
        verify(personService, times(1)).create(createPerson);
        verify(mapper, times(1)).mapToResponseDTO(createPerson);
        verify(objectMapper, times(1)).writeValueAsString(personResponseDTO);
        verify(response, times(1)).setStatus(200);
        verify(response, times(1)).setContentType("application/json");
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(printWriter, times(1)).write(anyString());

        Assertions.assertNotNull(objectMapper);
        Assertions.assertNotNull(printWriter);
    }

    @Test
    void doPut_validId_test() throws Exception {
        UUID id = UUID.randomUUID();
        String newName = "New person name";
        int newYearOfBirth = 1995;
        Person person = new Person(id, "Person", 1990);
        PersonRequestDTO personRequestDTO = new PersonRequestDTO(newName, newYearOfBirth);
        Person updatePerson = new Person(id, newName, newYearOfBirth);
        PersonResponseDTO personResponseDTO = new PersonResponseDTO(id, newName, newYearOfBirth);
        String requestBody = "{\"fullName\": \"New person name\", \"yearOfBirth\": 1995}";

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(String.valueOf(id));
        when(request.getReader()).thenReturn(reader);
        when(reader.readLine()).thenReturn(requestBody, null);
        objectMapper.readValue(requestBody, PersonRequestDTO.class);
        when(personService.findById(id)).thenReturn(person);
        when(mapper.mapToEntity(personRequestDTO)).thenReturn(updatePerson);
        when(personService.updateById(id, updatePerson)).thenReturn(updatePerson);
        when(mapper.mapToResponseDTO(updatePerson)).thenReturn(personResponseDTO);
        objectMapper.writeValueAsString(personResponseDTO);

        personServlet.doPut(request, response);

        verify(request, times(1)).getParameter("id");
        verify(request, times(1)).getReader();
        verify(objectMapper, times(1)).readValue(requestBody, PersonRequestDTO.class);
        verify(mapper, times(1)).mapToEntity(personRequestDTO);
        verify(personService, times(1)).updateById(id, updatePerson);
        verify(mapper, times(1)).mapToResponseDTO(updatePerson);
        verify(objectMapper, times(1)).writeValueAsString(personResponseDTO);
        verify(response, times(1)).setStatus(200);
        verify(response, times(1)).setContentType("application/json");
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(printWriter, times(1)).write(anyString());

        Assertions.assertNotNull(objectMapper);
        Assertions.assertNotNull(printWriter);
    }

    @Test
    void doPut_invalidId_test() throws Exception {
        String invalidId = "fg3qob";
        Person person = new Person("Person", 1990);

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(invalidId);
        when(request.getReader()).thenReturn(reader);
        when(reader.readLine()).thenReturn("{\"fullName\": \"Person\", \"yearOfBirth\": 1990}", null);

        personServlet.doPut(request, response);

        verify(request, times(1)).getParameter("id");
        verify(response, times(1)).setStatus(400);
        verify(printWriter, times(1)).write("Invalid person ID");

        Assertions.assertThrows(IllegalArgumentException.class, () -> personService.updateById(UUID.fromString(invalidId), person));
    }

    @Test
    void doPut_nullId_test() throws Exception {
        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(null);

        personServlet.doPut(request, response);

        verify(request, times(1)).getParameter("id");
        verify(response, times(1)).setStatus(400);
        verify(printWriter, times(1)).write("ID must not be empty");
        verify(personService, never()).updateById(any(), any());
    }

    @Test
    void doPut_notFoundById_test() throws Exception {
        UUID notFoundId = UUID.randomUUID();
        String requestBody = "{\"fullName\": \"Person\", \"yearOfBirth\": 1990}";

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(String.valueOf(notFoundId));
        when(request.getReader()).thenReturn(reader);
        when(reader.readLine()).thenReturn(requestBody, null);
        objectMapper.readValue(requestBody, AuthorRequestDTO.class);
        when(personService.findById(notFoundId)).thenReturn(null);

        personServlet.doPut(request, response);

        verify(request, times(1)).getParameter("id");
        verify(response, times(1)).setStatus(404);
        verify(printWriter, times(1)).write("Person not found by ID " + notFoundId);
        verify(personService, never()).updateById(any(), any());
    }

    @Test
    void doDelete_validId_test() throws Exception {
        Person person = new Person(UUID.randomUUID(), "Person", 2000);

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(String.valueOf(person.getId()));
        when(personService.findById(person.getId())).thenReturn(person);
        when(personService.delete(person.getId())).thenReturn(true);

        personServlet.doDelete(request, response);

        verify(response, times(1)).getWriter();
        verify(request, times(1)).getParameter("id");
        verify(personService, times(1)).delete(person.getId());
        verify(response, times(1)).setStatus(200);
        verify(printWriter, times(1)).write("Person was deleted successfully by ID " + person.getId());

        Assertions.assertTrue(personService.delete(person.getId()));
    }

    @Test
    void doDelete_invalidId_test() throws Exception {
        String invalidId = "wjtfovs34";

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(invalidId);

        personServlet.doDelete(request, response);

        verify(response, times(1)).setStatus(400);
        verify(printWriter, times(1)).write("Invalid person ID");

        Assertions.assertThrows(IllegalArgumentException.class, () -> personService.delete(UUID.fromString(invalidId)));
    }

    @Test
    void doDelete_nullId_test() throws Exception {
        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(null);

        personServlet.doDelete(request, response);

        verify(printWriter, times(1)).write("ID must not be empty");
        verify(response, times(1)).setStatus(400);
        verify(personService, never()).delete(any());

        Assertions.assertNull(request.getParameter("id"));
    }

    @Test
    void doDelete_notFoundById_test() throws Exception {
        UUID notFoundId = UUID.randomUUID();

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(String.valueOf(notFoundId));
        when(personService.findById(notFoundId)).thenReturn(null);

        personServlet.doDelete(request, response);

        verify(printWriter, times(1)).write("Person not found by ID " + notFoundId);
        verify(response, times(1)).setStatus(404);
        verify(personService, never()).delete(notFoundId);

        Assertions.assertNull(personService.findById(notFoundId));
    }
}