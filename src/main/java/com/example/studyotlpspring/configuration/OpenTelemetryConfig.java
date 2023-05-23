package com.example.studyotlpspring.configuration;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class OpenTelemetryConfig {

    private static final String JAEGER_ENDPOINT = "http://localhost:4317";

    @Bean
    public OpenTelemetrySdk getOpenTelemetrySdk() {
        return OpenTelemetrySdk.builder()
                .setTracerProvider(getTracerProvider())
                .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
                .build();
    }

    @Bean
    public SdkTracerProvider getTracerProvider() {
        return SdkTracerProvider.builder().setResource(Resource.getDefault()
                        .merge(Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME, "logical-service-name"))))
                .setSampler(Sampler.alwaysOn())
                .addSpanProcessor(SimpleSpanProcessor.create(getJaegerExporter()))
                .build();
    }

    @Bean
    public OtlpGrpcSpanExporter getJaegerExporter() {
        return OtlpGrpcSpanExporter.builder()
                .setEndpoint(JAEGER_ENDPOINT)
                .setTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    @Bean
    public Tracer getTracer() {
        return getOpenTelemetrySdk().getTracer("instrumentation-library-name", "1.0.0");
    }

}
