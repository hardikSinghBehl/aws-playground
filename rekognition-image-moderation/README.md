## Integrating AWS Rekognition with Java Spring-boot
### Image [content moderation](https://docs.aws.amazon.com/rekognition/latest/dg/moderation.html) use-case

A Java Spring-boot application exposing a single REST API to update user's profile picture. AWS Rekognition has been integrated in the service layer to detect any inappropriate, suggestive, unwanted or offensive content in the image being uploaded, If any `ModerationLabels` are detected the API returns HTTP Status of `406 Not Acceptable`.

----
#### 50 second Demonstration Recording

https://user-images.githubusercontent.com/69693621/155315203-871a48a8-93e1-4913-9063-1d7f494ca580.mov

---
### Local Setup Requirement and Steps
* Install Java 11 or higher (recommended to use [SdkMan](https://sdkman.io))

`sdk install java 17-open`

* Install Maven (recommended to use [SdkMan](https://sdkman.io))

`sdk install maven`

* Clone the repo (Clone entire root or [clone this subdirectory](https://stackoverflow.com/questions/600079/how-do-i-clone-a-subdirectory-only-of-a-git-repository))

* Create an IAM user with programmatic access with the below policy/permissions

```
{
    "Version": "2012-10-17",
    "Id": "Rekognition-detect-moderation-labels-permission",
    "Statement": [
        {
            "Sid": "detect-moderation-labels",
            "Effect": "Allow",
            "Action": "rekognition:DetectModerationLabels",
            "Resource": "*"
        }
    ]
}
```
* Go to application.properties file and fill the below values with security credentials received above
```
com.behl.aws.access-key=<Enter-IAM-Access-Key-Id-Here>
com.behl.aws.secret-access-key=<Enter-IAM-Secret-Access-Key-Here>
```
* Run the application

`mvn spring-boot:run &`

* Test the below API path using HTTP PUT Method and required image file

`http://localhost:8080/users/profile/image`
