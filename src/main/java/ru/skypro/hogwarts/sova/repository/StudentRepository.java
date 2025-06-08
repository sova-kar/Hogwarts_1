package ru.skypro.hogwarts.sova.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.skypro.hogwarts.sova.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findByAge(int age);
    List<Student> findByAgeBetween(int min, int max);

    @Modifying
    @Transactional
    @Query("UPDATE Student s SET s.faculty = null WHERE s.faculty.id = :facultyId")
    void detachStudentsFromFaculty(@Param("facultyId") Long facultyId);
}



