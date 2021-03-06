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

package com.vlkan.pubsub.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vlkan.pubsub.util.CollectionHelpers;

import java.util.List;
import java.util.Objects;

/**
 * Jackson-serializable Pub/Sub acknowledge request model.
 */
public class PubsubAckRequest {

    enum JsonFieldName {;

        static final String ACK_IDS = "ackIds";

    }

    @JsonProperty(JsonFieldName.ACK_IDS)
    private final List<String> ackIds;

    public PubsubAckRequest(List<String> ackIds) {
        Objects.requireNonNull(ackIds, "ackIds");
        if (ackIds.isEmpty()) {
            throw new IllegalArgumentException("empty ackIds");
        }
        this.ackIds = ackIds;
    }

    public List<String> getAckIds() {
        return ackIds;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PubsubAckRequest that = (PubsubAckRequest) object;
        return Objects.equals(ackIds, that.ackIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ackIds);
    }

    @Override
    public String toString() {
        String formattedAckIds = CollectionHelpers.limitedFormat(ackIds, 2);
        return "PubsubAckRequest{" +
                "ackIds=" + formattedAckIds +
                '}';
    }

}
