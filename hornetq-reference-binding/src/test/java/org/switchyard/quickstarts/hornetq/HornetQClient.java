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
package org.switchyard.quickstarts.hornetq;

import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientSession;
import org.switchyard.test.mixins.HornetQMixIn;


/**
 * HornetQ client that uses HornetQ API to connect to a remote server and
 * receive a message from a queue.
 *
 * @author <a href="tm.igarashi@gmail.com">Tomohisa Igarashi</a>
 *
 */
public final class HornetQClient {
    
    private static final String QUEUE = "jms.queue.GreetingServiceQueue";
    private static final String USER = "guest";
    private static final String PASSWD = "guestp";

    /**
     * Private no-args constructor.
     */
    private HornetQClient() {
    }
    
    /**
     * Receive a response message from HornetQ queue and display its contents.
     * @param ignored not used.
     * @throws Exception if something goes wrong.
     */
    public static void main(final String[] ignored) throws Exception {
        HornetQMixIn hqMixIn = new HornetQMixIn(false)
                                        .setUser(USER)
                                        .setPassword(PASSWD);
        hqMixIn.initialize();
        
        try {
            final ClientSession session = hqMixIn.getClientSession();
            final ClientConsumer consumer = session.createConsumer(QUEUE);
            ClientMessage message = null;
            while ((message = consumer.receive(3000)) != null) {
                System.out.println("Received message [" + message + "]");
                System.out.println("Contents in Received message [" + hqMixIn.readObjectFromMessage(message) + "]");
                message.acknowledge();
            }
        } finally {
            hqMixIn.uninitialize();
        }
    }
}
