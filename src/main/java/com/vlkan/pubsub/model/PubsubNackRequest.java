package com.vlkan.pubsub.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

/**
 * Jackson-serializable Pub/Sub model for forcing-message-to-become-not-acknowledged request.
 * It sets ackDeadlineSeconds for given messages to 0, imitating the nack() method of org.springframework.cloud.gcp.pubsub.core.PubSubTemplate
 */
public class PubsubNackRequest {

    enum JsonFieldName {;
        static final String ACK_IDS = "ackIds";
        static final String ACK_DEADLINE_SECONDS = "ackDeadlineSeconds";
    }

    @JsonProperty(PubsubNackRequest.JsonFieldName.ACK_IDS)
    private final List<String> ackIds;

    @JsonProperty(PubsubNackRequest.JsonFieldName.ACK_DEADLINE_SECONDS)
    @SuppressWarnings("FieldCanBeLocal")
    private final int ackDeadlineSeconds = 0;

    public PubsubNackRequest(List<String> ackIds) {
        Objects.requireNonNull(ackIds, "ackIds");
        if (ackIds.isEmpty()) {
            throw new IllegalArgumentException("empty ackIds");
        }
        this.ackIds = ackIds;
    }

    public List<String> getAckIds() {
        return ackIds;
    }

    public int getAckDeadlineSeconds() {
        return ackDeadlineSeconds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PubsubNackRequest that = (PubsubNackRequest) o;
        return Objects.equals(ackIds, that.ackIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ackIds, ackDeadlineSeconds);
    }

    @Override
    public String toString() {
        return "PubsubNackRequest{" +
                "ackIds=" + ackIds +
                ", ackDeadlineSeconds=" + ackDeadlineSeconds +
                '}';
    }
}
