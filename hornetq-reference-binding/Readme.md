Introduction
============
This quickstart demonstrates the usage of the HornetQ Component and it's reference binding feature, 
by binding to a HornetQ Queue. When the service is called a message will be sent into this queue.

This example is invoked through a SOAP gateway binding.

JBoss AS 7
----------
1. Build the quickstart:
    mvn clean install
2. Start JBoss AS 7 in standalone mode:
     ./standalone.sh -server-config standalone-full.xml
3. Add JMS user using add-user.sh with username=guest, password=guestp, Realm=ApplicationRealm
    ./add-user.sh
4. Add a guest role to the user "guest"
   echo "guest=guest" >> ${AS7}/standalone/configuration/application-roles.properties
5. Start JBoss CLI and connect: 
    ./jboss-admin.sh --connect
6. Create the JMS Queue using CLI:
    add-jms-queue --name=GreetingServiceQueue
7. Deploy the quickstart
     deploy  /path/to/quickstarts/hornetq-binding/target/switchyard-quickstart-hornetq-reference-binding.jar
8. Submit a request with your preferred SOAP client - src/test/resources/xml contains sample 
   requests and the responses that you should see
9. Execute HornetQClient to receive a response message from HornetQ queue
    mvn exec:java 

Expected Results
================
```
Received message [ClientMessage[messageID=7, durable=true, address=jms.queue.GreetingServiceQueue,properties=TypedProperties[null]]]
Contents in Received message [Hello there Tomo :-) ]
``` 

## Further Reading

1. [HornetQ Bindings Documentation](https://docs.jboss.org/author/display/SWITCHYARD/HornetQ+Bindings)

