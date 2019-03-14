**Run a sample**

_E.g. running "lab2.tx.Sender"_

`mvn package exec:java -Dexec.mainClass="lab2.tx.Sender"`


***

##Fire and Forget


1. Ensure that the configuration in file `src/main/java/lab2/utils/Application.properties`  file matches your 
local MQ settings created in Lab1

2. Send a message to queue by running the following maven command

>mvn package exec:java -Dexec.mainClass="lab2.fireandforget.Sender"

_This command will compile and run the program_ 

3. Verify that the message is on the target queue using the MQ Web UI

4. Modify the application to use the JMS 2.0 style API and re-run

5. Notice the much more compact coding style

6. Now consume the messages from the previous step by running the Receiver

7. Re-run the process 2 to 6 but before running step 6 add a printout of the message properties in the 
receiving step 6 by calling the `Utils.printJMSProperties(messge)`.

8. Notice the message properties, especially the Expiry and Delivery Mode

9. Modify the Sender to produce Persistent messages.

10. Re-run 2-6 and verify 

11. This behaviour (persistence) can be accomplished without any code change. How?

12. Revert back to the original sender code

13. Modify the MQ using the Web UI 

14. Re-run step 2-6 and verify that we are producing persistent messages

15. Modify the sender code to make the messages expire after 10 minutes

16. Re-run 2-6 and verify

17. Modify the Receiver to simulate processing failure. I.e. throw Exception after receiving the message

18. What happens to the message?

19. Change (Web UI) the MQ queue properties back to so that the message won't be persistent.

20. Re-run 2-6. What happens now? Why?

21. We need to modify the code to make it transactional. Implement this change and re-run

22. What happens now?

23. Using the Web UI we can control the behavior of the backout process. Modify the receiver queue

24. Re-run. Verify the we get the expected behavior. 

 

***
##Request Reply

Ensure you have a queue called LAB2.Q2 with mqclient permissions using the Web UI
Start the lab2.reqreply.Server

Start the lab2.reqreply.ClientCorrelationId

What happens?

ReRun with lab2.reqreply.ClientTemporaryQueue

What happens?



***
##Publish and Subscribe

Start the lab2.pubsub.Subscriber

>mvn package exec:java -Dexec.mainClass="lab2.pubsub.Subscriber"

This will register a subscriber and wait for publications

Now start the publisher in another window 

>mvn package exec:java -Dexec.mainClass="lab2.pubsub.Publisher"

You should receive the publication in the subscriber window. 

The subscriber is now stoped.

Rerun the Publisher and then the Subscriber in that order.

You should not the Subscriber should not receive any publication.

No modify the Subscriber code to create a durable subscriber.

Run the modified subscriber, this version only waits one second for a publication and then exits

Now run the Publisher and the Subscriber, you should receive the subscription even thou the subscribing app wasn't running 
during the publication.

We have created a durable subscription!

Open the Web UI and add the subscription Widget. You should se you subscription in the list.
To remove a durable subscription you need the unsubscribe. You can do this by uncomment the code
subscriber and re run. 

Verify that the subscription is gone from the Web UI


####Retained Publications
Change the Publisher to send Retained Publication and run it once.


Then change the Subscriber to non durable subscriber and run. 
You should now receive the message even thou you did not have a subscriber during the Publication

Now open the Web UI look in the queue list Push the Settings icon and Show System Objects.
Sort by queue depth, find the queue SYSTEM.RETAINED.PUB.QUEUE and locate you publication




***


##IBM JMS Configurations

```
-Dcom.ibm.mq.jms.replyToStyle=RFH2 
-Dcom.ibm.mq.cfg.useIBMCipherMappings=false 
```



```
-Djavax.net.debug=none 
```