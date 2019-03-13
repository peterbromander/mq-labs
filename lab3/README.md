# Lab 3 SpringBoot

This lab aims to show how you implement a more production like service using MQ

## Running 
To compile and run the application use the following command

``mvn spring-boot:run``

This will should result in some Java Exceptions.

Can you find the Error code MQRC 2085? 

This MQ specific error code indicating that an MQ object is missing.  We need to creat the appropriate MQ objects.
Specifically we need to create the necessary queues.
Open the application.properties file under resources. This file contains the queue names used by this application.
Create the queues using the Web UI.

Assign the necessary authoreris.

Re-run the application - It should start without any errors.

##

## Testing the Idempotency

So now that we have the service up and running, it's time to test it! 

Put a message message with the text "Hello" on the queue `LAB3.IN` using the Web UI. 

What happens? We get an error - Why?

The Web UI don't have the functionality to create Message Properties which is needed by the service to know 
where to route the message. 

For testing purpose let's change the service to do static routing to the queue `LAB3.OUT` by doing the the follwing :

 - Create new queue LAB3.OUT 
 - Modify the service code in `Idempotent.java` by adding the following code:
    `message.setStringProperty(TARGET_KEY,"LAB3.OUT");`
    
    
Re-start the application. Put a new test message with the text "Hello" on the `LAB3.IN` queue.

What happens?

Put a new message on the queue with the same text.

What happens? Look at the LAB3.STATE queue.

Put a new message on the in queue with a new text.

What happens? Look at the LAB3.STATE queue.





## Testing reconnection 

First start the application

Stop the queue manager using the Web UI

Look at the Log files 

Restart the queue manager 

What happens? 
