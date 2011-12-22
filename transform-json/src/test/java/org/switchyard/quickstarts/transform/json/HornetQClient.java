/*
 * JBoss, Home of Professional Open Source Copyright 2009, Red Hat Middleware
 * LLC, and individual contributors by the @authors tag. See the copyright.txt
 * in the distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package org.switchyard.quickstarts.transform.json;

import java.io.IOException;
import java.io.InputStream;

import org.milyn.io.StreamUtils;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.ServerLocator;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.switchyard.component.hornetq.ServerLocatorBuilder;
import org.switchyard.test.mixins.HornetQMixIn;

/**
 * HornetQ client that uses HornetQ API to connect to a remote server and send a
 * message to a queue.
 * 
 * @author Daniel Bevenius
 * @author <a href="tm.igarashi@gmail.com">Tomohisa Igarashi</a>
 * 
 */
public final class HornetQClient {

    private static final String INPUT_QUEUE = "jms.queue.OrderServiceQueue";
    private static final String OUTPUT_QUEUE = "jms.queue.StoreResponseQueue";
    private static final String USER = "guest";
    private static final String PASSWD = "guestp";

    /**
     * Private no-args constructor.
     */
    private HornetQClient() {
    }

    /**
     * Application logic.
     * 
     * @throws Exception exception
     */
    public void perform() throws Exception {
        HornetQMixIn hqMixIn = new HornetQMixIn(false)
                                        .setUser(USER)
                                        .setPassword(PASSWD);
        hqMixIn.initialize();

        try {
            ClientSession session = hqMixIn.getClientSession();
            ClientProducer producer = session.createProducer(INPUT_QUEUE);
            ClientMessage message = hqMixIn.createMessage(readFileContent("/order.json"));
            producer.send(message);
            session = hqMixIn.createClientSession();
            
            System.out.println("Sent message [" + message + "]");

            ClientConsumer consumer = session.createConsumer(OUTPUT_QUEUE);
            message = consumer.receive(3000);
            if (message == null) {
                throw new Exception("Couldn't receive a response message.");
            }
            System.out.println("Received message [" + message + "]");
            System.out.println("Message body [" + hqMixIn.readObjectFromMessage(message) + "]");
        } finally {
            hqMixIn.uninitialize();
        }
    }

    /**
     * Only execution point for this application.
     * 
     * @param ignored
     *            not used.
     * @throws Exception
     *             if something goes wrong.
     */
    public static void main(final String[] ignored) throws Exception {
        new HornetQClient().perform();
    }

    private String readFileContent(String path) throws Exception {
        InputStream in = null;
        try {
            in = HornetQClient.class.getResourceAsStream(path);
            return StreamUtils.readStreamAsString(in);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
