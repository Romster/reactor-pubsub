package com.vlkan.pubsub.model;

import com.vlkan.pubsub.jackson.JacksonHelpers;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PubsubNackRequestTest {

    @Test
    public void test_ctor_with_null_ackIds() {
        Assertions
                .assertThatThrownBy(() -> new PubsubNackRequest(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("ackIds");
    }

    @Test
    public void test_ctor_with_empty_ackIds() {
        Assertions
                .assertThatThrownBy(() -> new PubsubNackRequest(Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("empty ackIds");
    }

    @Test
    public void test_serialization() {
        List<String> ackIds = Collections.singletonList("some-ack-id");
        PubsubNackRequest request = new PubsubNackRequest(ackIds);
        Map<String, Object> actualRequestMap = JacksonHelpers.writeValueAsMap(request);
        Map<String, Object> expectedRequestMap = new HashMap<String, Object>() {{
            put(PubsubNackRequest.JsonFieldName.ACK_IDS, ackIds);
            put(PubsubNackRequest.JsonFieldName.ACK_DEADLINE_SECONDS, 0);
        }};
        Assertions.assertThat(actualRequestMap).isEqualTo(expectedRequestMap);
    }
}