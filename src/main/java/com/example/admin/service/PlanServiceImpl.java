package com.example.admin.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.admin.bindings.PlanForm;
import com.example.admin.constants.AppConstants;
import com.example.admin.entities.PlanEntity;
import com.example.admin.entities.UserEntity;
import com.example.admin.reposities.PlanRepo;
import com.example.admin.reposities.UserRepo;

@Service
public class PlanServiceImpl implements PlanService {

	@Autowired
	private PlanRepo planRepo;

	@Autowired
	private UserRepo userRepo;
	PlanEntity ent = null;

	@Override
	public String createPlan(PlanForm planForm) {
		String msg = null;

//		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		    String email = authentication.getName();

//		    UserEntity user = userRepo.findByEmail(email).orElseThrow();

		Optional<UserEntity> user = userRepo.findByEmail(planForm.getCreateByUser());

		UserEntity userEntity = user.get();
//		            .orElseThrow(() -> new RuntimeException("User not found with email: " + planForm.getCreateByUser()));
		Optional<PlanEntity> byPlanName = planRepo.findByPlanName(planForm.getPlanName());
		if (!byPlanName.isPresent()) {
			ent = new PlanEntity();
			ent.setPlanName(planForm.getPlanName());
			ent.setPlanStartDate(planForm.getStartDate());
			ent.setPlanEndDate(planForm.getEndDate());
			ent.setUser(userEntity);

			planRepo.save(ent);
			msg = AppConstants.PLAN_CREATION_MSG;
		} else
			msg = AppConstants.PLAN_EXISTS;

		return msg;
	}

	@Override
	public PlanForm editThePlan(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createMultiplePlans(List<PlanForm> planForm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<PlanForm> getAllThePlan(int page, int size,String sortField, String sortDir) {
		 Sort sort = sortDir.equalsIgnoreCase("desc") ?
	                Sort.by(sortField).descending() :
	                Sort.by(sortField).ascending();
		PageRequest of = PageRequest.of(page, size,sort);
		Page<PlanEntity> all = planRepo.findAll(of);
		List<PlanForm> collect = all.getContent().stream().map(user -> {
			PlanForm planForm = new PlanForm();
			planForm.setPlanName(user.getPlanName());
			planForm.setStartDate(user.getPlanStartDate());
			planForm.setEndDate(user.getPlanEndDate());
			planForm.setCreateByUser(user.getUser().getEmail());

			return planForm;

		}).collect(Collectors.toList());

		 return new PageImpl<>(collect, of, all.getTotalElements());
	}

}
