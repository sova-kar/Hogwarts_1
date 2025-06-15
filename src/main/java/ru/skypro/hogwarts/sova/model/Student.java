package ru.skypro.hogwarts.sova.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Schema(description = "Студент Хогвартса")

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")

public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор студента", example = "1")
    private Long id;

    @Version
    private Integer version = 0;
    public void setVersion(Integer version) {
        this.version = version;
    }


    @Schema(description = "Имя студента", example = "Гарри Поттер")
    private String name;

    @Schema(description = "Возраст студента", example = "17")
    private int age;

    public Student() {
    }
    @ManyToOne
    @JoinColumn(name = "faculty_id")
    @JsonBackReference
    private Faculty faculty;

    public Student(Long id, String name, int age, Faculty faculty) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.faculty = faculty;
        this.version = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return age == student.age && Objects.equals(id, student.id) && Objects.equals(name, student.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
    public Faculty getFaculty() { return faculty; }
    public void setFaculty(Faculty faculty) { this.faculty = faculty; }

}
