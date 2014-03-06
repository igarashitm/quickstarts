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
import javax.xml.namespace.QName;

import org.switchyard.remote.RemoteInvoker;
import org.switchyard.remote.RemoteMessage;
import org.switchyard.remote.http.HttpInvoker;

/**
 * Test client which uses RemoteInvoker to invoke a service with an SCA binding.
 */
public final class DozerTransformationClient {

    private static final QName SERVICE = new QName( "urn:switchyard-quickstart:transform-dozer:0.1.0", "OrderService");
    private static final String URL = "http://localhost:8080/switchyard-remote";

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
        // Create a new remote client invoker
        RemoteInvoker invoker = new HttpInvoker(URL);

        // Create request payload
        Order order = new Order();
        order.setItem("Turkey");
        order.setQuantity(1);

        // Create the request message
        RemoteMessage message = new RemoteMessage();
        message.setService(SERVICE).setOperation("submitOrder").setContent(order);

        // Invoke the service
        RemoteMessage reply = invoker.invoke(message);
        if (reply.isFault()) {
            System.err.println("Oops ... something bad happened.  " + reply.getContent());
        } else {
            OrderAck orderAck = (OrderAck) reply.getContent();
            out.println("==================================");
            out.println("Was the offer accepted? " + orderAck.isAccepted());
            out.println("Description: " + orderAck.getStatusDescription());
            out.println("Order ID: " + orderAck.getOrderId());
            out.println("==================================");
        }
    }
}
