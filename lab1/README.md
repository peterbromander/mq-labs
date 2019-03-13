# MQ Docker 

This lab aims to run and install the IBM MQ Docker image locally on your computer.


The IBM MQ Docker project can be found at:

https://github.com/ibm-messaging/mq-container


1 Run a local queue manager with the default configuration and a volume 

https://github.com/ibm-messaging/mq-container/blob/master/docs/usage.md

```
docker volume create qm1data
```


```
docker run \
  --env LICENSE=accept \
  --env MQ_QMGR_NAME=QM1 \
  --publish 1414:1414 \
  --publish 9443:9443 \
  --detach \
  --volume qm1data:/mnt/mqm \
  ibmcom/mq
```
  
2 Access the WEB ui at:

https://localhost:9443/ibmmq/console/
* User: admin
* Password: passw0rd


3 Create a new local queue called "Q1", we will use this queue later on
