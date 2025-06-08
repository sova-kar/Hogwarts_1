package ru.skypro.hogwarts.sova.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.hogwarts.sova.model.Faculty;

import java.util.Collection;
import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Collection<Faculty> findByColorIgnoreCase(String color);
    List<Faculty> findByNameIgnoreCaseOrColorIgnoreCase(String name, String color);
}
