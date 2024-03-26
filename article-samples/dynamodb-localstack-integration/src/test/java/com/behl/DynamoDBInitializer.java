package com.behl;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DynamoDBInitializer implements BeforeAllCallback {

	private static final DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse("localstack/localstack:3");
	
	private static final LocalStackContainer localStackContainer = new LocalStackContainer(LOCALSTACK_IMAGE)
			.withCopyFileToContainer(MountableFile.forClasspathResource("init-dynamodb.sh", 0744), "/etc/localstack/init/ready.d/init-dynamodb.sh")
			.withServices(Service.DYNAMODB)
			.waitingFor(Wait.forLogMessage(".*Executed init-dynamodb.sh.*", 1));

	@Override
	public void beforeAll(final ExtensionContext context) {
		log.info("Creating localstack container : {}", LOCALSTACK_IMAGE);
		
		localStackContainer.start();
		addConfigurationProperties();
		
		log.info("Successfully started localstack container : {}", LOCALSTACK_IMAGE);
	}

	private void addConfigurationProperties() {
		System.setProperty("com.behl.aws.access-key", localStackContainer.getAccessKey());
		System.setProperty("com.behl.aws.secret-access-key", localStackContainer.getSecretKey());
		System.setProperty("com.behl.aws.region", localStackContainer.getRegion());
		System.setProperty("com.behl.aws.endpoint", localStackContainer.getEndpoint().toString());
	}

}