## AWS Transcribe integration with Java Spring-boot (POC)
An MP3 audio file is given to the exposed API as input, and it's text translation is returned in the source language with the help of [AWS Transcribe](https://aws.amazon.com/transcribe/).

----

#### Demonstration Video (50 second screen recording with audio)

https://user-images.githubusercontent.com/69693621/155886686-9d5baafe-71dd-4a80-829f-8966642486e1.mp4

--- 

### Setup
* Install Java 17 and Maven ([SdkMan](https://sdkman.io) is recommended)
* Create an IAM user with programmtic access and enter the appropriate values in application.properties file 
```
com.behl.aws.access-key=<IAM-Access-Key-Goes-Here>
com.behl.aws.secret-access-key=<IAM-Secret-Access-Key-Goes-Here>
```
* Create 2 S3 Buckets and enter their names in application.properties file (1 as the input-store and other as output store) `A Single S3 Bucket can also be used`
```
com.behl.aws.s3.input-bucket-name=<input-bucket-name>
com.behl.aws.s3.output-bucket-name=<output-bucket-name>
```
* Attach a custom policy with the below permissions to the created IAM User
```
{
    "Version": "2012-10-17",
    "Id": "aws-transcribe-integration-poc-java-spring-boot",
    "Statement": [
        {
            "Sid": "s3-permissions",
            "Effect": "Allow",
            "Action": [
                "s3:PutObject",
                "s3:GetObject",
                "s3:DeleteObject"
            ],
            "Resource": [
                "arn:aws:s3:::<INPUT-BUCKET-NAME>/*",
                "arn:aws:s3:::<OUTPUT-BUCKET-NAME>/*"
            ]
        },
        {
            "Sid": "transcribe-permissions",
            "Effect": "Allow",
            "Action": [
                "transcribe:GetTranscriptionJob",
                "transcribe:StartTranscriptionJob"
            ],
            "Resource": "*"
        }
    ]
}
```
* Run the spring-boot application

`mvn spring-boot:run`

* The POST API is exposed at the below URI

`http://localhost:8080/audio/conversion/language/{languageCode}`

Reference: [Available Language Codes](https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/transcribe/model/LanguageCode.html)
