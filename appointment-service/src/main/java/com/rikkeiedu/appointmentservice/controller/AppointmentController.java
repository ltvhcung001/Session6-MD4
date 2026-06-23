package com.rikkeiedu.appointmentservice.controller;

import com.rikkeiedu.appointmentservice.entity.Appointment;
import com.rikkeiedu.appointmentservice.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping({"/api/v1/appointments", "/api/v1/appointment"})
    public ResponseEntity<?> createAppointment(@RequestBody Appointment appointment) {
        // 1. Check if Patient exists via patient-service
        try {
            ResponseEntity<Object> response = restTemplate.getForEntity(
                "http://patient-service/api/v1/patients/" + appointment.getPatientId(), 
                Object.class
            );
            if (response.getStatusCode() != HttpStatus.OK) {
                return new ResponseEntity<>("Patient with ID " + appointment.getPatientId() + " not found.", HttpStatus.BAD_REQUEST);
            }
        } catch (HttpClientErrorException.NotFound e) {
            return new ResponseEntity<>("Patient with ID " + appointment.getPatientId() + " not found.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to verify patient: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 2. Check if Doctor exists via doctor-service
        try {
            ResponseEntity<Object> response = restTemplate.getForEntity(
                "http://doctor-service/api/v1/doctors/" + appointment.getDoctorId(), 
                Object.class
            );
            if (response.getStatusCode() != HttpStatus.OK) {
                return new ResponseEntity<>("Doctor with ID " + appointment.getDoctorId() + " not found.", HttpStatus.BAD_REQUEST);
            }
        } catch (HttpClientErrorException.NotFound e) {
            return new ResponseEntity<>("Doctor with ID " + appointment.getDoctorId() + " not found.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to verify doctor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 3. Save appointment
        if (appointment.getStatus() == null) {
            appointment.setStatus("PENDING");
        }
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return new ResponseEntity<>(savedAppointment, HttpStatus.CREATED);
    }
}
