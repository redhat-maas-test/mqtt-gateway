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

import enmasse.mqtt.messages.AmqpSubscribeMessage;
import io.vertx.core.shareddata.Shareable;

/**
 * Class for bringing AMQP_SUBSCRIBE through verticles in a shared data map
 */
public class AmqpSubscribeData implements Shareable {

    private final Object messageId;
    private final AmqpSubscribeMessage subscribe;

    /**
     * Constructor
     *
     * @param messageId AMQP_SUBSCRIBE message identifier
     * @param subscribe AMQP_SUBSCRIBE message
     */
    public AmqpSubscribeData(Object messageId, AmqpSubscribeMessage subscribe) {
        this.messageId = messageId;
        this.subscribe = subscribe;
    }

    /**
     * AMQP_SUBSCRIBE message identifier
     * @return
     */
    public Object messageId() {
        return this.messageId;
    }

    /**
     * AMQP_SUBSCRIBE message
     * @return
     */
    public AmqpSubscribeMessage subscribe() {
        return this.subscribe;
    }
}
