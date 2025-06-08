package ru.skypro.hogwarts.sova.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.hogwarts.sova.model.Faculty;
import ru.skypro.hogwarts.sova.model.Student;
import ru.skypro.hogwarts.sova.service.FacultyService;
import ru.skypro.hogwarts.sova.service.StudentService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("student")
@Tag(name = "Student API", description = "API для работы со студентами")
public class StudentController {
    private final StudentService studentService;
    private final FacultyService facultyService;

    public StudentController(StudentService studentService , FacultyService facultyService) {
        this.studentService = studentService;
        this.facultyService = facultyService;
    }

    @Operation(summary = "Создать студента")
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        try {
            if (student.getFaculty() != null && student.getFaculty().getId() != null) {
                Faculty faculty = facultyService.findFaculty(student.getFaculty().getId());
                student.setFaculty(faculty);
            }

            student.setVersion(0);

            Student createdStudent = studentService.createStudent(student);
            return ResponseEntity.ok(createdStudent);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    @Operation(summary = "Получить студента по ID")
    @GetMapping("{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @Operation(summary = "Обновить данные студента")
    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @Operation(summary = "Удалить студента")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить студентов по возрасту")
    @GetMapping("age/{age}")
    public ResponseEntity<Collection<Student>> getStudentsByAge(@PathVariable int age) {
        Collection<Student> students = studentService.findByAge(age);
        if (students.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(students);
    }

    @Operation(summary = "Получить всех студентов")
    @GetMapping
    public Collection<Student> getAllStudents() {
        return studentService.getAllStudents();
    }
    @Operation(summary = "Получить студентов в диапазоне")
    @GetMapping("/age-between")
    public ResponseEntity<List<Student>> getByAgeBetween(
            @RequestParam int min,
            @RequestParam int max) {
        return ResponseEntity.ok(studentService.findByAgeBetween(min, max));
    }
    @GetMapping("/{id}/faculty")
    public Faculty getStudentFaculty(@PathVariable Long id) {
        return studentService.getFacultyByStudentId(id);
    }
}
