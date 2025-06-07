package ru.skypro.hogwarts.sova.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.hogwarts.sova.model.Student;

import java.util.Collection;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findByAge(int age);
}
