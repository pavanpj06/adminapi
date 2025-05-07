package com.example.admin.reposities;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.admin.entities.PlanEntity;
import java.util.List;
import java.util.Optional;


public interface PlanRepo extends JpaRepository<PlanEntity, Long>{

	
	
	Optional<PlanEntity> findByPlanName(String planName);
}
