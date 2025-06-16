package ru.skypro.hogwarts.sova;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import ru.skypro.hogwarts.sova.model.Faculty;
import ru.skypro.hogwarts.sova.model.Student;
import ru.skypro.hogwarts.sova.repository.FacultyRepository;
import ru.skypro.hogwarts.sova.repository.StudentRepository;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/faculty";
    }

    @Test
    void createFaculty_shouldReturnCreatedFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Гриффиндор");
        faculty.setColor("Красный");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                getBaseUrl(),
                faculty,
                Faculty.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Faculty createdFaculty = response.getBody();
        assertNotNull(createdFaculty);
        assertEquals("Гриффиндор", createdFaculty.getName());
        assertEquals("Красный", createdFaculty.getColor());
        assertNotNull(createdFaculty.getId());
    }

    @Test
    void getFacultyById_shouldReturnFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Тестовый факультет");
        faculty.setColor("Голубой");
        Faculty savedFaculty = facultyRepository.save(faculty);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + savedFaculty.getId(),
                Faculty.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Faculty foundFaculty = response.getBody();
        assertNotNull(foundFaculty);
        assertEquals(savedFaculty.getId(), foundFaculty.getId());
        assertEquals("Тестовый факультет", foundFaculty.getName());
        assertEquals("Голубой", foundFaculty.getColor());
    }

    @Test
    void updateFaculty_shouldReturnUpdatedFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Существующее название");
        faculty.setColor("Зеленый");
        Faculty savedFaculty = facultyRepository.save(faculty);

        savedFaculty.setName("Новое название");
        savedFaculty.setColor("Желтый");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Faculty> requestEntity = new HttpEntity<>(savedFaculty, headers);

        ResponseEntity<Faculty> response = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.PUT,
                requestEntity,
                Faculty.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Faculty updatedFaculty = response.getBody();
        assertNotNull(updatedFaculty);
        assertEquals("Новое название", updatedFaculty.getName());
        assertEquals("Желтый", updatedFaculty.getColor());
        assertEquals(savedFaculty.getId(), updatedFaculty.getId());
    }

    @Test
    void deleteFaculty_shouldDeleteFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Удалить");
        faculty.setColor("Черный");
        Faculty savedFaculty = facultyRepository.save(faculty);
        Long facultyId = savedFaculty.getId();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                getBaseUrl() + "/" + facultyId,
                HttpMethod.DELETE,
                null,
                Void.class);

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
        assertFalse(facultyRepository.existsById(facultyId));
    }

    @Test
    void getFacultiesByColor_shouldReturnFacultiesList() {
        Faculty faculty = new Faculty();
        faculty.setName("Гриффиндор");
        faculty.setColor("Красный");
        facultyRepository.save(faculty);

        ResponseEntity<List> response = restTemplate.getForEntity(
                getBaseUrl() + "/color/Красный",
                List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void searchFaculties_shouldReturnFacultiesList() {
        Faculty faculty = new Faculty();
        faculty.setName("Гриффиндор");
        faculty.setColor("Красный");
        facultyRepository.save(faculty);

        ResponseEntity<List> response = restTemplate.getForEntity(
                getBaseUrl() + "/search?query=Гриффиндор",
                List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());

        ResponseEntity<List> responseByColor = restTemplate.getForEntity(
                getBaseUrl() + "/search?query=Красный",
                List.class);

        assertEquals(HttpStatus.OK, responseByColor.getStatusCode());
        assertFalse(responseByColor.getBody().isEmpty());
    }

    @Test
    void getFacultyStudents_shouldReturnStudentsList() {
        Faculty faculty = new Faculty();
        faculty.setName("Гриффиндор");
        faculty.setColor("Красный");
        Faculty savedFaculty = facultyRepository.save(faculty);

        Student student = new Student();
        student.setName("Гарри Поттер");
        student.setAge(17);
        student.setFaculty(savedFaculty);
        studentRepository.save(student);

        ResponseEntity<List> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + savedFaculty.getId() + "/students",
                List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());

        List<Map<String, Object>> students = response.getBody();
        assertEquals("Гарри Поттер", students.get(0).get("name"));
        assertEquals(17, students.get(0).get("age"));
    }
}