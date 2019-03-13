# MQ Docker 

This lab aims to run and install the IBM MQ Docker image locally on your computer.


The IBM MQ Docker project can be found at:

https://github.com/ibm-messaging/mq-container

##Start a MQ Docker
1 Run a local queue manager with the default configuration and a volume 

https://github.com/ibm-messaging/mq-container/blob/master/docs/usage.md

Create a volume 

>docker volume create qm1data

Start :

>docker run \
  --env LICENSE=accept \
  --env MQ_QMGR_NAME=QM1 \
  --publish 1414:1414 \
  --publish 9443:9443 \
  --detach \
  --volume qm1data:/mnt/mqm \
  ibmcom/mq


##Using the Web UI to connect to MQ
2 Access the WEB ui at:

https://localhost:9443/ibmmq/console/
* User: admin
* Password: passw0rd


3 Create a new local queue called `LAB2.Q1`, we will use this queue later on

4 All API access will be made using the `DEV.APP.SVRCON`, this channel has a MCA user called
`app`. This user is a member of the `mqclient` group.

5 Grant access to user app to the new queue `LAB2.Q1` by 
select "..." -> "Manage authority records" -> "Create"  in the Queue list widget. 
Group="mqclient"  mark All MQI operations


##Using the console to connect to MQ

From the command prompt 
>docker ps

This shows the container id

Run the following command with the id from above
>docker exec -it ${CONTAINER_ID} dspmq

What do get? QMNAME(QM1)   STATUS(Running)

"Log on" to the queue manager 

>docker exec -it ${CONTAINER_ID} runmqsc QM1

Now you are inside the queue manager. Type:

>DISPLAY QLOCAL(*)

>DISPLAY QLOCAL(LAB2.Q1)

To exit 

>END

