### [Polly](https://aws.amazon.com/polly/) Integration with Java Spring-boot to convert Text-to-Audio
#### Demonstration Screen Recording
https://user-images.githubusercontent.com/69693621/156869860-979608e2-0621-4a46-aeda-cbbeb4bcacde.mov

---

### Local Setup
* Install Java 17+ and Maven ([SdkMan](https://sdkman.io) is recommended)
* Create an IAM user with programmatic access with the below permissions
```
{
    "Version": "2012-10-17",
    "Id": "polly-poc-integration-java-spring-boot",
    "Statement": [
        {
            "Sid": "poc-required-permissions",
            "Effect": "Allow",
            "Action": [
                "polly:SynthesizeSpeech",
                "polly:StartSpeechSynthesisTask",
                "polly:DescribeVoices"
            ],
            "Resource": "*"
        }
    ]
}
```
* Configure the security credentials of the above user in .properties file
```
com.behl.aws.access-key=<IAM-Access-Key-Goes-Here>
com.behl.aws.secret-access-key=<IAM-Secret-Access-Key-Goes-Here>
```
* Run the spring-boot application

`mvn spring-boot:run`

* The `POST` API is exposed at the below URI

`http://localhost:8080/v1/generate/audio`

The above API takes JSON request body with a single key, `text`
