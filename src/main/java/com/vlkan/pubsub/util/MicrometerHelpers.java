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

package com.vlkan.pubsub.util;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

public enum MicrometerHelpers {;

    public static <T> Mono<T> measureLatency(
            MeterRegistry meterRegistry,
            String meterName,
            Map<String, Timer> timerByKey,
            String key,
            Function<Boolean, String[]> tagSupplier,
            Mono<T> mono) {
        return Mono
                .fromCallable(System::nanoTime)
                .flatMap(startInstantNanos -> mono
                        .doOnSuccess(ignored -> {
                            long durationNanos = System.nanoTime() - startInstantNanos;
                            Timer timer = createSuccessTimer(meterRegistry, meterName, timerByKey, key, tagSupplier);
                            timer.record(durationNanos, TimeUnit.NANOSECONDS);
                        })
                        .doOnError(ignored -> {
                            long durationNanos = System.nanoTime() - startInstantNanos;
                            Timer timer = createFailureTimer(meterRegistry, meterName, timerByKey, key, tagSupplier);
                            timer.record(durationNanos, TimeUnit.NANOSECONDS);
                        }));
    }

    private static Timer createSuccessTimer(
            MeterRegistry meterRegistry,
            String meterName,
            Map<String, Timer> timerByKey,
            String key,
            Function<Boolean, String[]> tagSupplier) {
        String mapKey = key + "/success";
        return timerByKey.computeIfAbsent(mapKey, ignoredKey -> {
            String[] tags = tagSupplier.apply(true);
            return meterRegistry.timer(meterName, tags);
        });
    }

    private static Timer createFailureTimer(
            MeterRegistry meterRegistry,
            String meterName,
            Map<String, Timer> timerByKey,
            String key,
            Function<Boolean, String[]> tagSupplier) {
        String mapKey = key + "/failure";
        return timerByKey.computeIfAbsent(mapKey, ignoredKey -> {
            String[] tags = tagSupplier.apply(false);
            return meterRegistry.timer(meterName, tags);
        });
    }

    public static <T, N extends Number> Mono<T> measureCount(
            MeterRegistry meterRegistry,
            String meterName,
            Map<String, Counter> counterByKey,
            String key,
            Supplier<String[]> tagSupplier,
            Function<T, N> countExtractor,
            Mono<T> mono) {
        return mono.doOnNext(value -> {
            N count = countExtractor.apply(value);
            counterByKey
                    .computeIfAbsent(key, ignored -> {
                        String[] tags = tagSupplier.get();
                        String[] extendedTags = new String[tags.length + 2];
                        System.arraycopy(tags, 0, extendedTags, 2, tags.length);
                        extendedTags[0] = "type";
                        extendedTags[1] = "counter";
                        return meterRegistry.counter(meterName, extendedTags);
                    })
                    .increment(count.doubleValue());
        });
    }

}
