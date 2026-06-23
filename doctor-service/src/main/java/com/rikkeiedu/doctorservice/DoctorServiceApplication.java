package com.rikkeiedu.doctorservice;

import com.rikkeiedu.doctorservice.entity.Doctor;
import com.rikkeiedu.doctorservice.repository.DoctorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class DoctorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoctorServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner demoData(DoctorRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new Doctor(null, "Dr. John Smith", "Nội khoa", 10, "john.smith@hospital.com", true));
                repo.save(new Doctor(null, "Dr. Jane Doe", "Nhi khoa", 5, "jane.doe@hospital.com", false));
            }
        };
    }
}
