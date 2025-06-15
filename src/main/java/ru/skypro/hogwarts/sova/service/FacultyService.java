package ru.skypro.hogwarts.sova.service;

import jakarta.transaction.Transactional;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.hogwarts.sova.model.Faculty;
import ru.skypro.hogwarts.sova.model.Student;
import ru.skypro.hogwarts.sova.repository.FacultyRepository;
import ru.skypro.hogwarts.sova.repository.StudentRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Факультет не найден"));
    }
    @Transactional
    public Faculty editFaculty(Faculty faculty) {
        Faculty existing = facultyRepository.findById(faculty.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Факультет не найден"));

        existing.setName(faculty.getName());
        existing.setColor(faculty.getColor());

        return facultyRepository.save(existing);
    }
    @Transactional
    public void deleteFaculty(long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        faculty.getStudents().forEach(s -> s.setFaculty(null));
        studentRepository.saveAll(faculty.getStudents());

        facultyRepository.delete(faculty);
    }

    public Collection<Faculty> findByColor(String color) {
        return facultyRepository.findByColorIgnoreCase(color);
    }

    public Collection<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public List<Faculty> search(String query) {
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(query, query);
    }

    public List<Student> getStudentsByFacultyId(Long facultyId) {
        return facultyRepository.findById(facultyId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Факультет не найден"))
                .getStudents();
    }
    public Faculty findFacultyById(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Факультет с ID " + id + " не найден"
                ));
    }
}
