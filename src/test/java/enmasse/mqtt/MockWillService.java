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
import org.apache.qpid.proton.amqp.transport.ErrorCondition;
import org.apache.qpid.proton.amqp.transport.LinkError;
import org.apache.qpid.proton.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mock for the Will Service
 */
public class MockWillService {

    private static final Logger LOG = LoggerFactory.getLogger(MockWillService.class);

    private static final String WILL_SERVICE_ENDPOINT = "$mqtt.willservice";
    private static final String CONTAINER_ID = "will-service";

    private String connectAddress;
    private int connectPort;

    private ProtonClient client;

    /**
     * Constructor
     *
     * @param vertx Vert.x instance
     */
    public MockWillService(Vertx vertx) {

        this.client = ProtonClient.create(vertx);
    }

    /**
     * Connect to the router
     */
    public void connect() {

        this.client.connect(this.connectAddress, this.connectPort, done -> {

            if (done.succeeded()) {

                LOG.info("Will Service started successfully ...");

                ProtonConnection connection = done.result();
                connection.setContainer(CONTAINER_ID);

                connection
                        .sessionOpenHandler(session -> session.open())
                        .receiverOpenHandler(this::receiverHandler)
                        .open();

            } else {

                LOG.info("Error starting the Will Service ...", done.cause());
            }
        });
    }

    private void receiverHandler(ProtonReceiver receiver) {

        // Will Service supports only the control address
        if (!receiver.getRemoteTarget().getAddress().equals(WILL_SERVICE_ENDPOINT)) {

            ErrorCondition error = new ErrorCondition(LinkError.DETACH_FORCED, "The endpoint provided is not supported");
            receiver
                    .setCondition(error)
                    .close();
        } else {

            // TODO: tracking the AMQP sender

            receiver
                    .setTarget(receiver.getRemoteTarget())
                    .handler((delivery, message) -> {

                        this.messageHandler(receiver, delivery, message);
                    })
                    .open();
        }
    }

    private void messageHandler(ProtonReceiver receiver, ProtonDelivery delivery, Message message) {

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
     * @return  current Mock Will Service instance
     */
    public MockWillService setConnectAddress(String connectAddress) {
        this.connectAddress = connectAddress;
        return this;
    }

    /**
     * Set the port for connecting to the AMQP services
     *
     * @param connectPort   port for AMQP connections
     * @return  current Mock Will Service instance
     */
    public MockWillService setConnectPort(int connectPort) {
        this.connectPort = connectPort;
        return this;
    }
}