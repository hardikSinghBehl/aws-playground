### Sticky sessions with AWS ALB
POC to implement Sticky sessions (Session affinity) using custom application based cookies behind AWS Application Load Balancer (ALB)

By default, the ALB uses round-robin algorithm to redirect traffic to all the configured distinations inside the matching target-group. Sticky sessions can be used to modify this functionality by sending a user making requests within a time-period to the same backend server to meet specefic business requirements. In this proof-of-concept we use an in-memory cache to store OTPs until expiration period and expose two API endpoints to generate and validate OTP. The one-time-password is returned in the API response itself, would need to use an email-client in a real application. This functionality won't work using round-robin algorithm in case of multiple instances due to the absense of a central cache.

The custom cookie key being used by the application(s) has to be configured at the target-group level.

#### Reference
* [AWS Article](https://docs.aws.amazon.com/elasticloadbalancing/latest/application/sticky-sessions.html)

----
### APIs
1.) GET `/v1/otp/generate/{emailId}`
  * Summary: API used to generate OTP against the provided emailId

2.) GET `/v1/otp/validate?emailId={emailId}&otp={otp}`
  * Summary: API used to validate OTP using the in-memory cache 

3.) GET `/v1/otp/ping`
  * Summary: API used for health-check

----
### Deployment/System Requirements
* Install Java 17 (recommended to use [SdkMan](https://sdkman.io))

```
sdk install java 17-open
```
* Install Maven (recommended to use [SdkMan](https://sdkman.io))

```
sdk install maven
```
* Clone the repository or this [individual subdirectory](https://github.com/hardikSinghBehl/aws-java-reference-pocs/blob/main/INDIVIDUAL_FOLDER_CLONE.md) and run the below command in the root directory of the project

```
mvn clean install
```

* To start the application, run the below commands (control + (A+D) to detach screen)

```
mvn spring-boot:run &
```
