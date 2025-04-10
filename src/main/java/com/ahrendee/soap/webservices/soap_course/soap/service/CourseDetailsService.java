package com.ahrendee.soap.webservices.soap_course.soap.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import com.ahrendee.soap.webservices.soap_course.soap.Course;

@Service
public class CourseDetailsService {

    public enum Status {
        SUCCESS, FAILURE
    }

    private static List<Course> courseList = new ArrayList<Course>();

    static {
        var course1 = new Course(1, "Course 1", "Description 1");
        courseList.add(course1);
        var course2 = new Course(2, "Course 2", "Description 2");
        courseList.add(course2);
        var course3 = new Course(3, "Course 3", "Description 3");
        courseList.add(course3);
        var course4 = new Course(4, "Course 4", "Description 4");
        courseList.add(course4);
    }

    public static void main(String[] args) {
        // new CourseDetailsService().deleteCourseById(2);

        var courseToUpdate = new Course(2, "Updated Course 2", "Updated Description 2");
        new CourseDetailsService().updateCourse(courseToUpdate);
    }

    public Course findById(int id) {
        // Simulate fetching course details from a database or another source
        return courseList.stream().filter(course -> course.id() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Course> findAll() {
        // Simulate fetching all course details from a database or another source
        return courseList;
    }

    public Status deleteCourseById(int id) {
        Predicate<Course> courseMatched = course -> course.id() == id;

        if (courseList.removeIf(courseMatched)) {
            System.out.println("Course with ID " + id + " deleted.");
            return Status.SUCCESS;
        } else {
            System.out.println("Course with ID " + id + " not found.");
            return Status.FAILURE;
        }
    }

    public Course updateCourse(Course course) {
        // Simulate updating a course
        Predicate<Course> courseMatched = _course -> _course.id() == course.id();
        courseList.stream()
                .filter(courseMatched)
                .findFirst()
                .ifPresent(existingCourse -> {
                    courseList.remove(existingCourse);
                    courseList.add(course);
                });
        System.out.println(courseList);
        System.out.println("Course with ID " + course.id() + " updated.");
        return course;
    }

    public Course createCourse(Course course) {
        // Simulate creating a new course
        courseList.add(course);
        System.out.println("Course with ID " + course.id() + " created.");
        return course;
    }
}
