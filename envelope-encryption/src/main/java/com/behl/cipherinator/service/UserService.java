package com.behl.cipherinator.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.behl.cipherinator.dto.UserCreationRequestDto;
import com.behl.cipherinator.dto.UserLoginRequestDto;
import com.behl.cipherinator.entity.User;
import com.behl.cipherinator.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final EncryptionService encryptionService;

	public HttpStatus create(final UserCreationRequestDto userCreationRequestDto) {
		final var enryptionResult = encryptionService.encrypt(userCreationRequestDto.getPassword());

		if (userRepository.findByUserName(userCreationRequestDto.getUserName()).isPresent())
			return HttpStatus.CONFLICT;

		final var user = new User();
		user.setUserName(userCreationRequestDto.getUserName());
		user.setPassword(enryptionResult.getEncryptedResult());
		user.setEncryptedDataKey(enryptionResult.getEncryptedDataKey());

		userRepository.save(user);
		return HttpStatus.CREATED;
	}

	public HttpStatus login(final UserLoginRequestDto userLoginRequestDto) {
		final var user = userRepository.findByUserName(userLoginRequestDto.getUserName())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if (!userLoginRequestDto.getPassword()
				.equals(encryptionService.decrypt(user.getPassword(), user.getEncryptedDataKey())))
			return HttpStatus.UNAUTHORIZED;
		return HttpStatus.OK;
	}

}
