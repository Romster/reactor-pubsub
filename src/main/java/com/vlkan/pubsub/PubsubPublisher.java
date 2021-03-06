/*
 * Copyright 2019-2020 Volkan Yazıcı
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permits and
 * limitations under the License.
 */

package com.vlkan.pubsub;

import com.vlkan.pubsub.model.PubsubDraftedMessage;
import com.vlkan.pubsub.model.PubsubPublishRequest;
import com.vlkan.pubsub.model.PubsubPublishResponse;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PubsubPublisher {

    private final PubsubPublisherConfig config;

    private final PubsubClient client;

    private PubsubPublisher(Builder builder) {
        this.config = builder.config;
        this.client = builder.client;
    }

    public PubsubPublisherConfig getConfig() {
        return config;
    }

    public PubsubClient getClient() {
        return client;
    }

    public Mono<PubsubPublishResponse> publishMessage(PubsubDraftedMessage message) {
        Objects.requireNonNull(message, "message");
        List<PubsubDraftedMessage> messages = Collections.singletonList(message);
        return publishMessages(messages);
    }

    public Mono<PubsubPublishResponse> publishMessages(List<PubsubDraftedMessage> messages) {
        Objects.requireNonNull(messages, "messages");
        PubsubPublishRequest publishRequest = new PubsubPublishRequest(messages);
        return publish(publishRequest);
    }

    public Mono<PubsubPublishResponse> publish(PubsubPublishRequest publishRequest) {
        Objects.requireNonNull(publishRequest, "publishRequest");
        return client.publish(config.getProjectName(), config.getTopicName(), publishRequest);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private PubsubPublisherConfig config;

        private PubsubClient client;

        private Builder() {}

        public Builder setConfig(PubsubPublisherConfig config) {
            this.config = Objects.requireNonNull(config, "config");
            return this;
        }

        public Builder setClient(PubsubClient client) {
            this.client = Objects.requireNonNull(client, "client");
            return this;
        }

        public PubsubPublisher build() {
            Objects.requireNonNull(config, "config");
            if (client == null) {
                client = PubsubClient.getDefaultInstance();
            }
            return new PubsubPublisher(this);
        }

    }

}
