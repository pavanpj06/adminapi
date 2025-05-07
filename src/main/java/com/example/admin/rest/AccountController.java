package com.example.admin.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.admin.bindings.UnLockAccountForm;
import com.example.admin.bindings.UserAccountForm;
import com.example.admin.constants.AppConstants;
import com.example.admin.service.AccountService;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@RestController
//@CrossOrigin(origins = "*")
public class AccountController {

	Map<String, String> messageResponse = new HashMap();

	@Autowired
	private AccountService accountService;

	// spring AOP

	private Logger logger = LoggerFactory.getLogger(AccountController.class);

	@PostMapping("/account-creation")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<Map<String, String>> accountCreation(@Valid @RequestBody UserAccountForm acForm)
			throws MessagingException, IOException {
		logger.debug("Account creation process started");
		ResponseEntity<Map<String, String>> response = null;
		boolean userAccount = accountService.createUserAccount(acForm);
		if (userAccount) {
			messageResponse.put("message", AppConstants.ACCOUNT_CREATION_MSG);
			response = new ResponseEntity<>(messageResponse, HttpStatus.CREATED);// 201
		} else {
			messageResponse.put("message", "With this mail id account is already exists");
			response = new ResponseEntity<>(messageResponse, HttpStatus.CONFLICT); // 409
		}

		return response;
	}

	@GetMapping("fetch-all-user-accounts")
	public ResponseEntity<Page<UserAccountForm>> fetchAccountDetail(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<UserAccountForm> allUserAccountDetails = accountService.getAllUserAccountDetails(page, size);

		return new ResponseEntity<>(allUserAccountDetails, HttpStatus.OK);

	}

	@GetMapping("/edit-user-account/{userId}")
	public ResponseEntity<UserAccountForm> editUserAccount(@PathVariable("userId") Long userId) {
		UserAccountForm userAccountById = accountService.getUserAccountById(userId);
		return new ResponseEntity<>(userAccountById, HttpStatus.OK);
	}

	@PutMapping("/update-account-status/{userId}/{status}")
	public ResponseEntity<List<UserAccountForm>> updateTheRecord(@PathVariable("userId") Integer userId,
			@PathVariable("status") String status) {
		accountService.accountStatusChange(userId, status);

		List<UserAccountForm> allUserAccountDetails = (List<UserAccountForm>) accountService
				.getAllUserAccountDetails(userId, userId);

		return new ResponseEntity<>(allUserAccountDetails, HttpStatus.OK);
	}

	@PostMapping("/unlock-account")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<Map<String, String>> unlockTheAccount(@RequestBody UnLockAccountForm unlckForm) {

		ResponseEntity<Map<String, String>> response = null;

		String unlockTheAccount = accountService.unlockTheAccount(unlckForm);

		if (unlockTheAccount.contains("unlocking is done")) {

			messageResponse.put("message", unlockTheAccount);
			response = new ResponseEntity<>(messageResponse, HttpStatus.OK);
		} else
			messageResponse.put("message", unlockTheAccount);
		response = new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);

		return response;

	}

	@DeleteMapping("/delete-by-id/{userId}")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<Map<String, String>> deletyById(@PathVariable("userId") Long id) {
		boolean deleteById = accountService.deleteById(id);
		ResponseEntity<Map<String, String>> response = null;
		if (deleteById) {
			messageResponse.put("message", "Account deleted successfully");
			response = ResponseEntity.ok(messageResponse);
		} else {
			messageResponse.put("message", "Account not deleted. An issue occurred.");
			response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
		}

		return response;
	}

}
