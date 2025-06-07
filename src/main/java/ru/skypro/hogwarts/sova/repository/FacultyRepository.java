package ru.skypro.hogwarts.sova.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.hogwarts.sova.model.Faculty;

import java.util.Collection;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Collection<Faculty> findByColorIgnoreCase(String color);
}
