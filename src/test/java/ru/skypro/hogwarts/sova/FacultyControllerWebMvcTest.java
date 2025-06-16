package ru.skypro.hogwarts.sova;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.hogwarts.sova.controller.FacultyController;
import ru.skypro.hogwarts.sova.model.Faculty;
import ru.skypro.hogwarts.sova.model.Student;
import ru.skypro.hogwarts.sova.service.FacultyService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacultyService facultyService;

    @Test
    void createFaculty_shouldReturnCreatedFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Гриффиндор");
        faculty.setColor("Красный");

        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гриффиндор"))
                .andExpect(jsonPath("$.color").value("Красный"));
    }

    @Test
    void getFacultyById_shouldReturnFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Гриффиндор");
        faculty.setColor("Красный");

        when(facultyService.findFaculty(anyLong())).thenReturn(faculty);

        mockMvc.perform(get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гриффиндор"))
                .andExpect(jsonPath("$.color").value("Красный"));
    }

    @Test
    void editFaculty_shouldReturnUpdatedFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Обновленный Гриффиндор");
        faculty.setColor("Золотой");

        when(facultyService.editFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Обновленный Гриффиндор"))
                .andExpect(jsonPath("$.color").value("Золотой"));
    }

    @Test
    void deleteFaculty_shouldReturnOkStatus() throws Exception {
        mockMvc.perform(delete("/faculty/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getFacultiesByColor_shouldReturnFacultiesList() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Гриффиндор");
        faculty.setColor("Красный");

        when(facultyService.findByColor(anyString())).thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(get("/faculty/color/Красный"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Гриффиндор"))
                .andExpect(jsonPath("$[0].color").value("Красный"));
    }

    @Test
    void searchFaculties_shouldReturnFacultiesList() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Гриффиндор");
        faculty.setColor("Красный");

        when(facultyService.search(anyString())).thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(get("/faculty/search?query=Гриф"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Гриффиндор"))
                .andExpect(jsonPath("$[0].color").value("Красный"));
    }

    @Test
    void getAllFaculties_shouldReturnFacultiesList() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Гриффиндор");
        faculty.setColor("Красный");

        when(facultyService.getAllFaculties()).thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Гриффиндор"))
                .andExpect(jsonPath("$[0].color").value("Красный"));
    }

    @Test
    void getFacultyStudents_shouldReturnStudentsList() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Гарри Поттер");
        student.setAge(17);

        when(facultyService.getStudentsByFacultyId(anyLong())).thenReturn(Collections.singletonList(student));

        mockMvc.perform(get("/faculty/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Гарри Поттер"))
                .andExpect(jsonPath("$[0].age").value(17));
    }
}