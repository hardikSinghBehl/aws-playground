### [AWS Translate](https://aws.amazon.com/translate/) integration with Java Spring-boot
#### Demonstration Screen Recording
https://user-images.githubusercontent.com/69693621/155888113-5728a133-858d-4f47-a752-4dbaefd6fa1c.mov

--- 

### Setup
* Install Java 11+ and Maven ([SdkMan](https://sdkman.io) is recommended)
* Create an IAM user with programmtic access and enter the appropriate values in application.properties file 
```
com.behl.aws.access-key=<IAM-Access-Key-Goes-Here>
com.behl.aws.secret-access-key=<IAM-Secret-Access-Key-Goes-Here>
```
* Attach the below permissions to the created IAM user
```
{
    "Version": "2012-10-17",
    "Id": "translate-integration-java-spring-boot-poc",
    "Statement": [
        {
            "Sid": "text-translation-permission",
            "Effect": "Allow",
            "Action": "translate:TranslateText",
            "Resource": "*"
        }
    ]
}
```
* Run the spring-boot application

`mvn spring-boot:run`

* API endpoint(s) information is provided in [TranslationController.class](https://github.com/hardikSinghBehl/aws-java-reference-pocs/blob/main/translate-integration/src/main/java/com/behl/translator/controller/TranslationController.java)
