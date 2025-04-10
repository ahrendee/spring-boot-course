package com.ahrendee.soap.webservices.soap_course;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.ahrendee.courses.CourseDetails;
import com.ahrendee.courses.DeleteCourseDetailsRequest;
import com.ahrendee.courses.DeleteCourseDetailsResponse;
import com.ahrendee.courses.GetAllCourseDetailsRequest;
import com.ahrendee.courses.GetAllCourseDetailsResponse;
import com.ahrendee.courses.GetCourseDetailsRequest;
import com.ahrendee.courses.GetCourseDetailsResponse;
import com.ahrendee.soap.webservices.soap_course.soap.Course;
import com.ahrendee.soap.webservices.soap_course.soap.exception.CourseNotFoundException;
import com.ahrendee.soap.webservices.soap_course.soap.service.CourseDetailsService;
import com.ahrendee.soap.webservices.soap_course.soap.service.CourseDetailsService.Status;

@Endpoint
public class CourseDetailsEndpoint {

    @Autowired
    private CourseDetailsService courseDetailsService;

    @PayloadRoot(namespace = "http://ahrendee.com/courses", localPart = "GetCourseDetailsRequest")
    @ResponsePayload
    public GetCourseDetailsResponse processCourseDetailsRequest(@RequestPayload GetCourseDetailsRequest request) {
        Optional<Course> course = Optional.ofNullable(courseDetailsService.findById(request.getId()));

        if (course.isEmpty()) {
            throw new CourseNotFoundException("Course with Id " + request.getId() + " not found");
        }

        GetCourseDetailsResponse response = new GetCourseDetailsResponse();
        response.setCourseDetails(mapCourseToCourseDetails(course.get()));
        return response;
    }

    @PayloadRoot(namespace = "http://ahrendee.com/courses", localPart = "GetAllCourseDetailsRequest")
    @ResponsePayload
    public GetAllCourseDetailsResponse processAllCourseDetailsRequest(
            @RequestPayload GetAllCourseDetailsRequest request) {
        List<Course> courses = courseDetailsService.findAll();

        GetAllCourseDetailsResponse response = new GetAllCourseDetailsResponse();
        courses.stream().forEach(course -> {
            response.getCourseDetails().add(mapCourseToCourseDetails(course));
        });

        return response;
    }

    @PayloadRoot(namespace = "http://ahrendee.com/courses", localPart = "DeleteCourseDetailsRequest")
    @ResponsePayload
    public DeleteCourseDetailsResponse deleteCourseDetailsRequest(
            @RequestPayload DeleteCourseDetailsRequest request) {
        Status status = courseDetailsService.deleteCourseById(request.getId());

        DeleteCourseDetailsResponse response = new DeleteCourseDetailsResponse();
        response.setStatus(mapStatus(status));
        // result.ifPresent(value -> {
        // if (value) {
        // response.setDeleted(true);
        // System.out.println("Course with ID " + request.getId() + " deleted.");
        // } else {
        // response.setDeleted(false);
        // System.out.println("Course with ID " + request.getId() + " not found.");
        // }
        // });

        return response;
    }

    private com.ahrendee.courses.Status mapStatus(Status status) {
        switch (status) {
            case SUCCESS:
                return com.ahrendee.courses.Status.SUCCESS;
            case FAILURE:
                return com.ahrendee.courses.Status.FAILURE;
            default:
                throw new IllegalArgumentException("Unknown status: " + status);
        }
    }

    private CourseDetails mapCourseToCourseDetails(Course course) {
        CourseDetails courseDetails = new CourseDetails();
        courseDetails.setId(course.id());
        courseDetails.setName(course.name());
        courseDetails.setDescription(course.description());
        return courseDetails;
    }
}
