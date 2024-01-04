package com.behl.cipherinator.service;

import org.springframework.stereotype.Service;

import com.behl.cipherinator.dto.UserCreationRequestDto;
import com.behl.cipherinator.dto.UserLoginRequestDto;
import com.behl.cipherinator.entity.User;
import com.behl.cipherinator.exception.InvalidLoginCredentialException;
import com.behl.cipherinator.exception.UserNameAlreadyExistsException;
import com.behl.cipherinator.repository.UserRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final EnvelopeEncryptionService envelopeEncryptionService;

	/**
	 * Creates a new user record in the DataSource corresponding to provided user
	 * creation request.
	 *
	 * @param userCreationRequestDto The data for creating a new user record.
	 * @throws IllegalArgumentException if provided argument is {@code null}
	 * @throws UserNameAlreadyExistsException if a record with provided UserName
	 *                                        already exists in the DataSource.
	 */
	public void create(@NonNull final UserCreationRequestDto userCreationRequest) {
		final var userName = userCreationRequest.getUserName();
		final var userNameExists = userRepository.existsByUserName(userName);
		if (Boolean.TRUE.equals(userNameExists)) {
			throw new UserNameAlreadyExistsException("Account with given username already exists.");
		}
		
		final var encryptionResult = envelopeEncryptionService.encrypt(userCreationRequest.getPassword());

		final var user = new User();
		user.setUserName(userName);
		user.setPassword(encryptionResult.getEncryptedData());
		user.setEncryptedDataKey(encryptionResult.getEncryptedDataKey());

		userRepository.save(user);
	}

	/**
	 * Validates user login credentials. Expects UserName and plain-text password.
	 *
	 * @param userLoginRequestDto The user login request containing login credentials.
	 * @throws IllegalArgumentException if provided argument is {@code null}
	 * @throws InvalidLoginCredentialException if the login credentials are invalid.
	 */
	public void login(@NonNull final UserLoginRequestDto userLoginRequest) {
		final var user = userRepository.findByUserName(userLoginRequest.getUserName()).orElseThrow(InvalidLoginCredentialException::new);
		final var decryptedPassword = envelopeEncryptionService.decrypt(user.getPassword(), user.getEncryptedDataKey());
		final var isPasswordValid = userLoginRequest.getPassword().equals(decryptedPassword);
		
		if (Boolean.FALSE.equals(isPasswordValid)) {
			throw new InvalidLoginCredentialException();
		}
	}

}
