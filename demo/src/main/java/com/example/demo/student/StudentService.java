package com.example.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents(){
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) throws IllegalAccessException {
        Optional<Student> studentOptional = studentRepository
                .findStudentByEmail(student.getEmail());

        if(studentOptional.isPresent()){
                throw new IllegalAccessException("email taken");
            }

        studentRepository.save(student);
//        System.out.println(student);
    }

    public void deleteStudent(Long studentId) throws IllegalAccessException {
        boolean exists = studentRepository.existsById(studentId);
        if(!exists){
            throw new IllegalAccessException(
                    "student with id " + studentId + " doesn't exists");
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException(
                        "student with id " + studentId + " doesn't exists"));

        if(name != null && name.length() > 0 && !student.getName().equals(name)){
            student.setName(name);
        }

        if(email != null && email.length() > 0 && !student.getEmail().equals(email)){
            Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
            if(studentOptional.isPresent()){
                throw new IllegalStateException("email taken");
            }
            student.setEmail(email);
        }
    }

//    @Transactional
//    public void updateStudent(Long studentId) throws IllegalAccessException{
//        boolean exists = studentRepository.existsById(studentId);
//        if(!exists){
//            throw new IllegalAccessException(
//                    "student with id " + studentId + " doesn't exists");
//        }
//        Optional<Student> student = studentRepository.findById(studentId);
//        Student updatedStudent = student.get();
//        updatedStudent.setName("Mary");
//        updatedStudent.setEmail("mary@gmail.com");
//        studentRepository.save(updatedStudent);
//    }

}
