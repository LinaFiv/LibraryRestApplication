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
import org.example.servlets.DTO.PersonResponseDTO;
import org.example.servlets.mapper.PersonMapper;
import org.example.servlets.mapper.PersonMapperImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(urlPatterns = "/people")
public class PeopleServlet extends HttpServlet {
    private PersonService personService;
    private PersonMapper personMapper;

    public PeopleServlet() {
    }

    public PeopleServlet(PersonService personService, PersonMapper personMapper) {
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
        List<Person> people = personService.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        List<PersonResponseDTO> dtoList = personMapper.mapToList(people);
        String peopleJson = objectMapper.writeValueAsString(dtoList);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(200);
        writer.println(peopleJson);
    }
}
