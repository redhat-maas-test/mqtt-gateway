/*
 * Copyright 2016 Red Hat Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package enmasse.mqtt;

import io.vertx.core.Vertx;
import io.vertx.proton.*;
import org.apache.qpid.proton.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mock for the Subscription Service
 */
public class MockSubscriptionService {

    private static final Logger LOG = LoggerFactory.getLogger(MockSubscriptionService.class);

    private static final String SUBSCRIPTION_SERVICE_ENDPOINT = "$mqtt.subscriptionservice";
    private static final String CONTAINER_ID = "subscription-service";

    private String connectAddress;
    private int connectPort;

    private ProtonClient client;

    /**
     * Constructor
     *
     * @param vertx Vert.x instance
     */
    public MockSubscriptionService(Vertx vertx) {

        this.client = ProtonClient.create(vertx);
    }

    /**
     * Connect to the router
     */
    public void connect() {

        this.client.connect(this.connectAddress, this.connectPort, done -> {

            if (done.succeeded()) {

                LOG.info("Subscription Service started successfully ...");

                ProtonConnection connection = done.result();
                connection.setContainer(CONTAINER_ID);

                connection
                        .open();

                ProtonReceiver receiver = connection.createReceiver(SUBSCRIPTION_SERVICE_ENDPOINT);

                receiver
                        .setTarget(receiver.getRemoteTarget())
                        .handler(this::messageHandler)
                        .open();

            } else {

                LOG.info("Error starting the Will Service ...", done.cause());
            }
        });
    }

    private void messageHandler(ProtonDelivery delivery, Message message) {

        // TODO:

        LOG.info("Received {}", message);
    }

    public void close() {

        // TODO:
    }

    /**
     * Set the address for connecting to the AMQP services
     *
     * @param connectAddress    address for AMQP connections
     * @return  current Mock Subscription Service instance
     */
    public MockSubscriptionService setConnectAddress(String connectAddress) {
        this.connectAddress = connectAddress;
        return this;
    }

    /**
     * Set the port for connecting to the AMQP services
     *
     * @param connectPort   port for AMQP connections
     * @return  current Mock Subscription Service instance
     */
    public MockSubscriptionService setConnectPort(int connectPort) {
        this.connectPort = connectPort;
        return this;
    }
}