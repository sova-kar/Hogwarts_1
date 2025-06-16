package ru.skypro.hogwarts.sova;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.hogwarts.sova.controller.StudentController;
import ru.skypro.hogwarts.sova.model.Faculty;
import ru.skypro.hogwarts.sova.model.Student;
import ru.skypro.hogwarts.sova.service.FacultyService;
import ru.skypro.hogwarts.sova.service.StudentService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @MockBean
    private FacultyService facultyService;

    @Test
    void createStudent_shouldReturnCreatedStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Гарри Поттер");
        student.setAge(17);

        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гарри Поттер"))
                .andExpect(jsonPath("$.age").value(17));
    }

    @Test
    void getStudentById_shouldReturnStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Гарри Поттер");
        student.setAge(17);

        when(studentService.findStudent(anyLong())).thenReturn(student);

        mockMvc.perform(get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гарри Поттер"))
                .andExpect(jsonPath("$.age").value(17));
    }

    @Test
    void editStudent_shouldReturnUpdatedStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Обновить Поттера");
        student.setAge(18);

        when(studentService.editStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Обновить Поттера"))
                .andExpect(jsonPath("$.age").value(18));
    }

    @Test
    void deleteStudent_shouldReturnOkStatus() throws Exception {
        mockMvc.perform(delete("/student/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getStudentsByAge_shouldReturnStudentsList() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Гарри Поттер");
        student.setAge(17);

        when(studentService.findByAge(anyInt())).thenReturn(Collections.singletonList(student));

        mockMvc.perform(get("/student/age/17"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Гарри Поттер"))
                .andExpect(jsonPath("$[0].age").value(17));
    }

    @Test
    void getByAgeBetween_shouldReturnStudentsList() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Гарри Поттер");
        student.setAge(17);

        when(studentService.findByAgeBetween(anyInt(), anyInt())).thenReturn(Collections.singletonList(student));

        mockMvc.perform(get("/student/age-between?min=16&max=18"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Гарри Поттер"))
                .andExpect(jsonPath("$[0].age").value(17));
    }

    @Test
    void getAllStudents_shouldReturnStudentsList() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Гарри Поттер");
        student.setAge(17);

        when(studentService.getAllStudents()).thenReturn(Collections.singletonList(student));

        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Гарри Поттер"))
                .andExpect(jsonPath("$[0].age").value(17));
    }

    @Test
    void getStudentFaculty_shouldReturnFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Гриффиндор");
        faculty.setColor("Красный");

        when(studentService.getFacultyByStudentId(anyLong())).thenReturn(faculty);

        mockMvc.perform(get("/student/1/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гриффиндор"))
                .andExpect(jsonPath("$.color").value("Красный"));
    }
}