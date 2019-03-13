**Run a sample**

_E.g. running "lab2.tx.Sender"_

`mvn package exec:java -Dexec.mainClass="lab2.tx.Sender"`


***

**Fire and Forget**


1. Ensure that the configuration in file `src/main/java/lab2/utils/Application.properties`  file matches your 
local MQ settings created in Lab1

2. Send a message to queue by running the following maven command
``mvn package exec:java -Dexec.mainClass="lab2.fireandforget.Sender"``
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
**Publish and Subscribe**
1. 

***
**Request Reply**

***

**IBM JMS Configurations**

```
-Dcom.ibm.mq.jms.replyToStyle=RFH2 
-Dcom.ibm.mq.cfg.useIBMCipherMappings=false 
```



```
-Djavax.net.debug=none 
```