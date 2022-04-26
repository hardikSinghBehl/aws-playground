### Fanout Event-driven architecture using AWS SNS and SQS
Achieving fanout publishing and parallel processing against an occured event. Message is published to a SNS topic, two queues subscribed to the topic receive identical JSON objects representing the event in real-time and consumers of the queues consume/listen/process the message according to their individual needs. 

#### Services
* [user-creation-service](https://github.com/hardikSinghBehl/aws-java-reference-pocs/tree/main/simple-notification-service-fanout/user-creation-service) (Publisher to SNS topic `user-account-creation-topic`)
* [email-notification-system](https://github.com/hardikSinghBehl/aws-java-reference-pocs/tree/main/simple-notification-service-fanout/email-notification-system) (Subscriber to queue `email-notification-queue`)
* [event-log-system](https://github.com/hardikSinghBehl/aws-java-reference-pocs/tree/main/simple-notification-service-fanout/event-log-system) (Subscriber to queue `event-log-queue`)

#### Setup
* [AWS services setup](https://github.com/hardikSinghBehl/aws-java-reference-pocs/blob/main/simple-notification-service-fanout/AWS_SERVICES_SETUP.md)
* [Local setup](https://github.com/hardikSinghBehl/aws-java-reference-pocs/blob/main/simple-notification-service-fanout/LOCAL_SETUP.md)

#### References
* [Event-Driven Architecture](https://aws.amazon.com/event-driven-architecture/)
* [Fanout notifications using SNS and SQS](https://aws.amazon.com/getting-started/hands-on/send-fanout-event-notifications/)

### Demonstration screen-recording

https://user-images.githubusercontent.com/69693621/158197217-8c445977-dcca-4283-b074-bbf0edb4af99.mov

