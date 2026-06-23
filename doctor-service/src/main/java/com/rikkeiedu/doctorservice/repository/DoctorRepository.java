package com.rikkeiedu.doctorservice.repository;

import com.rikkeiedu.doctorservice.entity.Doctor;
import com.rikkeiedu.doctorservice.dto.DoctorDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query("SELECT new com.rikkeiedu.doctorservice.dto.DoctorDTO(d.id, d.name, d.specialization) FROM Doctor d")
    List<DoctorDTO> findAllProjected();
}
