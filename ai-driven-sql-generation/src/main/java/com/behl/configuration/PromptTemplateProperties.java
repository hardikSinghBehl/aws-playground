package com.behl.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "com.behl.prompt")
public class PromptTemplateProperties {

	@NotNull(message = "Prompt template must be configured")
	private Resource template;

	@NotNull(message = "DDL schema must be configured")
	private Resource ddlSchema;

}
