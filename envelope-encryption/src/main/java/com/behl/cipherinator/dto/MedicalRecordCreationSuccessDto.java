package com.behl.cipherinator.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Schema(title = "MedicalRecordCreationSuccess", accessMode = Schema.AccessMode.WRITE_ONLY)
public class MedicalRecordCreationSuccessDto {
	
	private String id;

}
