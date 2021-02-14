# Kafview
Simple Kafka Message UI, support Avro Schema.

## Add Config
http://localhost:9000/config

## Check Message
http://localhost:9000/topic/messages

## Check Specific Topic Message
http://localhost:9000/topic/messages?name=<topicName>&timeInSec=<fetch message from current-timeInSec>

## Sample Config file
/src/main/resources/static/config/sample-kafviewConfig.json