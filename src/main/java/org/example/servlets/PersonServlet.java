package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.models.Person;
import org.example.services.PersonService;
import org.example.services.impl.PersonServiceImpl;
import org.example.servlets.DTO.PersonRequestDTO;
import org.example.servlets.DTO.PersonResponseDTO;
import org.example.servlets.mapper.PersonMapper;
import org.example.servlets.mapper.PersonMapperImpl;
import org.example.utils.ServletUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

@WebServlet(urlPatterns = "/person")
public class PersonServlet extends HttpServlet {

    private PersonService personService;
    private PersonMapper personMapper;

    public PersonServlet() {
    }

    public PersonServlet(PersonService personService, PersonMapper personMapper) {
        this.personService = personService;
        this.personMapper = personMapper;
    }

    @Override
    public void init() throws ServletException {
        personService = new PersonServiceImpl();
        personMapper = new PersonMapperImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        String id = req.getParameter("id");

        if (id == null) {
            writer.write("ID must not be empty");
            resp.setStatus(400);
            return;
        }

        try {
            Person person = personService.findById(UUID.fromString(id));

            if (person == null) {
                writer.write("Person not found by ID " + id);
                resp.setStatus(404);
                return;
            }

            PersonResponseDTO personResponseDTO = personMapper.mapToResponseDTO(person);
            ObjectMapper objectMapper = new ObjectMapper();
            String personJson = objectMapper.writeValueAsString(personResponseDTO);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(200);
            writer.write(personJson);

        } catch (IllegalArgumentException e) {
            writer.write("Invalid person ID");
            resp.setStatus(400);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();

        String requestBody = ServletUtil.getRequestBody(req.getReader());
        PersonRequestDTO personRequestDTO = objectMapper.readValue(requestBody, PersonRequestDTO.class);

        Person person = personMapper.mapToEntity(personRequestDTO);
        Person createPerson = personService.create(person);
        PersonResponseDTO personResponseDTO = personMapper.mapToResponseDTO(createPerson);
        String personJson = objectMapper.writeValueAsString(personResponseDTO);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(200);
        writer.write(personJson);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String id = req.getParameter("id");

        if (id == null) {
            writer.write("ID must not be empty");
            resp.setStatus(400);
            return;
        }

        try {
            String requestBody = ServletUtil.getRequestBody(req.getReader());
            PersonRequestDTO personRequestDTO = objectMapper.readValue(requestBody, PersonRequestDTO.class);

            if (personService.findById(UUID.fromString(id)) == null) {
                writer.write("Person not found by ID " + id);
                resp.setStatus(404);
                return;
            }

            Person person = personMapper.mapToEntity(personRequestDTO);
            Person updatePerson = personService.updateById(UUID.fromString(id), person);
            PersonResponseDTO personResponseDTO = personMapper.mapToResponseDTO(updatePerson);
            String personJson = objectMapper.writeValueAsString(personResponseDTO);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(200);
            writer.write(personJson);

        } catch (IllegalArgumentException e) {
            writer.write("Invalid person ID");
            resp.setStatus(400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        String id = req.getParameter("id");

        if (id == null) {
            writer.write("ID must not be empty");
            resp.setStatus(400);
            return;
        }

        try {
            if (personService.findById(UUID.fromString(id)) == null) {
                writer.write("Person not found by ID " + id);
                resp.setStatus(404);
                return;
            }

            personService.delete(UUID.fromString(id));
            writer.write("Person was deleted successfully by ID " + id);
            resp.setStatus(200);

        } catch (IllegalArgumentException e) {
            writer.write("Invalid person ID");
            resp.setStatus(400);
        }
    }
}
