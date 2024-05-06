package com.behl.configuration;

import java.nio.charset.Charset;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(PromptTemplateProperties.class)
public class PromptTemplateConfiguration {

	private final PromptTemplateProperties promptTemplateProperties;

	@Bean
	@SneakyThrows
	public PromptTemplate promptTemplate() {
		final var promptTemplateResource = promptTemplateProperties.getTemplate();
		final var ddlSchemaResource = promptTemplateProperties.getDdlSchema();

		final var promptTemplate = new PromptTemplate(promptTemplateResource);
		promptTemplate.add("ddl", ddlSchemaResource.getContentAsString(Charset.defaultCharset()));
		return promptTemplate;
	}

}
