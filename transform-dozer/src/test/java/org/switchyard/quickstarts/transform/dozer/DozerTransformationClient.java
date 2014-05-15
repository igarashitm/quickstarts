/*
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.switchyard.quickstarts.transform.dozer;

import static java.lang.System.out;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Assert;

/**
 * Test client which uses RemoteInvoker to invoke a service with an SCA binding.
 */
public final class DozerTransformationClient {

    private static final String AMQ_USER = "karaf";
    private static final String AMQ_PASSWD = "karaf";
    private static final String AMQ_BROKER_URL = "tcp://localhost:61616";
    private static final String REQUEST_QUEUE_NAME = "RequestQueue";
    private static final String REPLY_QUEUE_NAME = "ReplyQueue";

    /**
     * Private no-args constructor.
     */
    private DozerTransformationClient() {
    }

    /**
     * Only execution point for this application.
     * @param ignored not used.
     * @throws Exception if something goes wrong.
     */
    public static void main(final String[] ignored) throws Exception {

        Order order = new Order();
        order.setItem("Turkey");
        order.setQuantity(1);
        OrderAck orderAck;
        
        ConnectionFactory cf = new ActiveMQConnectionFactory(AMQ_USER, AMQ_PASSWD, AMQ_BROKER_URL);
        Connection conn = cf.createConnection();
        conn.start();

         try {
             Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
             MessageProducer producer = session.createProducer(session.createQueue(REQUEST_QUEUE_NAME));
             producer.send(session.createObjectMessage(order));
             session.close();

             session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
             MessageConsumer consumer = session.createConsumer(session.createQueue(REPLY_QUEUE_NAME));
             Message message = consumer.receive(10000);
             Assert.assertTrue("Invalid message: " + message, message instanceof ObjectMessage);
             orderAck = (OrderAck) ((ObjectMessage)message).getObject();
         } finally {
             conn.close();
         }

         out.println("==================================");
         out.println("Was the offer accepted? " + orderAck.isAccepted());
         out.println("Description: " + orderAck.getStatusDescription());
         out.println("Order ID: " + orderAck.getOrderId());
         out.println("==================================");
    }
}
