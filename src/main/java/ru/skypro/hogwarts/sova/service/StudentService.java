package ru.skypro.hogwarts.sova.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.hogwarts.sova.model.Faculty;
import ru.skypro.hogwarts.sova.model.Student;
import ru.skypro.hogwarts.sova.repository.StudentRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final FacultyService facultyService;

    public StudentService(StudentRepository studentRepository,
                          FacultyService facultyService) {
        this.studentRepository = studentRepository;
        this.facultyService = facultyService;
    }



    @Transactional
    public Student createStudent(Student student) {
        if (student.getName() == null || student.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Нужно имя");
        }
        if (student.getAge() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Возраст должен быть больше 0");
        }

        if (student.getFaculty() != null && student.getFaculty().getId() != null) {
            Faculty faculty = facultyService.findFacultyById(student.getFaculty().getId());
            student.setFaculty(faculty);
        }

        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Студент не найден"));
    }

    @Transactional
    public Student editStudent(Student student) {
        Student existing = studentRepository.findById(student.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Студент не найден"));

        existing.setName(student.getName());
        existing.setAge(student.getAge());
        if (student.getFaculty() != null) {
            existing.setFaculty(student.getFaculty());
        }

        return studentRepository.save(existing);
    }
    @Transactional
    public void deleteStudent(long id) {
        studentRepository.deleteById(id);
        studentRepository.flush();
    }

    public Collection<Student> findByAge(int age) {
        return studentRepository.findByAge(age);
    }

    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<Student> findByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getFacultyByStudentId(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Студент не найден"))
                .getFaculty();
    }

}

