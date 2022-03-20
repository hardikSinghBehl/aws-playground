* Create an IAM user with programmatic access and download the security keys (access-key-id and secret-access-key)
* Create a Standard SNS topic with name `user-account-creation-topic`
* Create a new SQS Queue with name `email-notification-queue` and add the below mentioned statement in the statements array in Advanced access policy option
```
{
   "Sid":"SQSPolicyAllowingSNSPush",
   "Effect":"Allow",
   "Principal":"*",
   "Action":"sqs:SendMessage",
   "Resource":"<arn_of_current_email_notification_queue>",
   "Condition":{
      "ArnEquals":{
         "aws:SourceArn":"<arn_of_sns_user_account_creation_topic>"
      }
   }
}
```
* Create a new SQS Queue with name `event-log-queue` and add the below mentioned statement in the statements array in Advanced access policy option
```
{
   "Sid":"SQSPolicyAllowingSNSPush",
   "Effect":"Allow",
   "Principal":"*",
   "Action":"sqs:SendMessage",
   "Resource":"<arn_of_current_event_log_queue>",
   "Condition":{
      "ArnEquals":{
         "aws:SourceArn":"<arn_of_sns_user_account_creation_topic>"
      }
   }
}
```
* Create subscriptions to above created queues with configured SNS topic | [GUIDE](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-configure-subscribe-queue-sns-topic.html)
* Create an IAM policy with the below given JSON and attach the saved policy to the above created IAM user
```
{
   "Id":"EventFanoutSNSAndSQS",
   "Version":"2012-10-17",
   "Statement":[
      {
         "Sid":"SNSMessagePublishingPermission",
         "Effect":"Allow",
         "Action":"sns:Publish",
         "Resource":"<ARN_of_SNS_topic_user_account_creation_topic>"
      },
      {
         "Sid":"SQSOperationsPermissions",
         "Effect":"Allow",
         "Action":[
            "sqs:GetQueueUrl",
            "sqs:ReceiveMessage",
            "sqs:GetQueueAttributes",
            "sqs:ListQueueTags",
            "sqs:SendMessage",
            "sqs:ListQueues",
            "sqs:DeleteMessage"
         ],
         "Resource":[
            "<ARN_of_SQS_email_notification_queue>",
            "<ARN_of_SQS_event_log_queue>"
         ]
      }
   ]
}
```

