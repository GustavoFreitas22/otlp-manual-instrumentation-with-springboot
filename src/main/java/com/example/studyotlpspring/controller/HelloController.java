package com.example.studyotlpspring.controller;

import com.example.studyotlpspring.configuration.OpenTelemetryConfig;
import com.example.studyotlpspring.service.ServiceHello;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/")
public class HelloController {

    @Autowired
    private OpenTelemetryConfig opentelemetry;

   @GetMapping
   public String Hello() throws InterruptedException {
       Random rdn = new Random();
       Span first = opentelemetry.getTracer().spanBuilder("Hello_span").startSpan();

       var interval = rdn.nextInt(5);

       interval *= 100;

       Thread.sleep(interval);
       try (Scope ss = first.makeCurrent()) {
           first.addEvent("New Event!");
           first.setAttribute("study.test", true);
           childOne(first);
       } finally {
           first.end();
       }

       return "Olá";
   }

    void childOne(Span parentSpan) {
        Span childSpan = opentelemetry.getTracer().spanBuilder("Child Span 1")
                .setParent(Context.current().with(parentSpan))
                .startSpan();
        try {
            ServiceHello service = new ServiceHello();
            service.randonTime();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            childSpan.end();
        }
    }
}
