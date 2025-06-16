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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/student";
    }

    @Test
    void createStudent_shouldReturnCreatedStudent() {
        Student student = new Student();
        student.setName("Гарри Поттер");
        student.setAge(17);

        ResponseEntity<Student> response = restTemplate.postForEntity(
                getBaseUrl(),
                student,
                Student.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Student createdStudent = response.getBody();
        assertNotNull(createdStudent);
        assertEquals("Гарри Поттер", createdStudent.getName());
        assertEquals(17, createdStudent.getAge());
        assertNotNull(createdStudent.getId());
    }

    @Test
    void getStudentById_shouldReturnStudent() {
        Student student = new Student();
        student.setName("Тестовый");
        student.setAge(20);
        Student savedStudent = studentRepository.save(student);

        ResponseEntity<Student> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + savedStudent.getId(),
                Student.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Student foundStudent = response.getBody();
        assertNotNull(foundStudent);
        assertEquals(savedStudent.getId(), foundStudent.getId());
        assertEquals("Тестовый", foundStudent.getName());
        assertEquals(20, foundStudent.getAge());
    }

    @Test
    void updateStudent_shouldReturnUpdatedStudent() {
        Student student = new Student();
        student.setName("Существующее имя");
        student.setAge(20);
        Student savedStudent = studentRepository.save(student);

        savedStudent.setName("Новое имя");
        savedStudent.setAge(21);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Student> requestEntity = new HttpEntity<>(savedStudent, headers);

        ResponseEntity<Student> response = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.PUT,
                requestEntity,
                Student.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Student updatedStudent = response.getBody();
        assertNotNull(updatedStudent);
        assertEquals("Новое имя", updatedStudent.getName());
        assertEquals(21, updatedStudent.getAge());
        assertEquals(savedStudent.getId(), updatedStudent.getId());
    }

    @Test
    void deleteStudent_shouldDeleteStudent() {
        Student student = new Student();
        student.setName("Удалить");
        student.setAge(22);
        Student savedStudent = studentRepository.save(student);

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                getBaseUrl() + "/" + savedStudent.getId(),
                HttpMethod.DELETE,
                null,
                Void.class);

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
        assertFalse(studentRepository.existsById(savedStudent.getId()));
    }

    @Test
    void getStudentsByAge_shouldReturnStudentsList() {
        Student student = new Student();
        student.setName("Тестовый");
        student.setAge(20);
        studentRepository.save(student);

        ResponseEntity<List> response = restTemplate.getForEntity(
                getBaseUrl() + "/age/20",
                List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getByAgeBetween_shouldReturnStudentsList() {
        Student student = new Student();
        student.setName("Тестовый");
        student.setAge(20);
        studentRepository.save(student);

        ResponseEntity<List> response = restTemplate.getForEntity(
                getBaseUrl() + "/age-between?min=19&max=21",
                List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getAllStudents_shouldReturnStudentsList() {
        Student student = new Student();
        student.setName("Тестовый");
        student.setAge(20);
        studentRepository.save(student);

        ResponseEntity<List> response = restTemplate.getForEntity(
                getBaseUrl(),
                List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getStudentFaculty_shouldReturnFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Гриффиндор");
        faculty.setColor("Красный");
        Faculty savedFaculty = facultyRepository.save(faculty);

        Student student = new Student();
        student.setName("Гарри Поттер");
        student.setAge(17);
        student.setFaculty(savedFaculty);
        Student savedStudent = studentRepository.save(student);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + savedStudent.getId() + "/faculty",
                Faculty.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Faculty responseFaculty = response.getBody();
        assertNotNull(responseFaculty);
        assertEquals("Гриффиндор", responseFaculty.getName());
        assertEquals("Красный", responseFaculty.getColor());
        assertEquals(savedFaculty.getId(), responseFaculty.getId());
    }
}
