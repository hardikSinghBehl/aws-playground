package com.behl.cipherinator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.behl.cipherinator.dto.ExceptionResponseDto;
import com.behl.cipherinator.dto.MedicalRecordCreationDto;
import com.behl.cipherinator.dto.MedicalRecordCreationSuccessDto;
import com.behl.cipherinator.dto.MedicalRecordDto;
import com.behl.cipherinator.dto.MedicalRecordUpdationDto;
import com.behl.cipherinator.service.MedicalRecordService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Medical Record Management", description = "Endpoints for managing patient medical records.")
public class MedicalRecordController {

	private final MedicalRecordService medicalRecordService;

	@PostMapping(value = "/api/v1/medical-record", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Registers medical record", description = "Registers a new medical record in the system corresponding to provided information.")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "201", description = "Medical record registered successfully"),
			@ApiResponse(responseCode = "400", description = "Malformed request.",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class)))})
	public ResponseEntity<MedicalRecordCreationSuccessDto> create(@Valid @RequestBody final MedicalRecordCreationDto medicalRecordCreationRequest) {
		final var medicalRecordId = medicalRecordService.create(medicalRecordCreationRequest);
		final var response = MedicalRecordCreationSuccessDto.builder().id(medicalRecordId).build();
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PutMapping(value = "/api/v1/medical-record/{medicalRecordId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Updates medical record", description = "Updates medical record in the system corresponding to provided information.")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Medical record updated successfully"),
			@ApiResponse(responseCode = "404", description = "No Medical record exists with given Id.",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "Malformed request.",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))) })
	public ResponseEntity<HttpStatus> update(@PathVariable final String medicalRecordId,
			@Valid @RequestBody final MedicalRecordUpdationDto medicalRecordUpdationRequest) {
		medicalRecordService.update(medicalRecordId, medicalRecordUpdationRequest);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping(value = "/api/v1/medical-record/{medicalRecordId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Retrieves medical record", description = "Retrieves patient medical record corresponding to provided Id.")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Medical record retieved successfully"),
			@ApiResponse(responseCode = "404", description = "No Medical record exists with given Id.",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class)))})
	public ResponseEntity<MedicalRecordDto> retrieve(@PathVariable final String medicalRecordId) {
		final var response = medicalRecordService.retrieve(medicalRecordId);
		return ResponseEntity.ok(response);
	}

}