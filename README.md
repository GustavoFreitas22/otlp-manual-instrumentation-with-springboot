# otlp-manual-instrumentation-with-springboot
 Este é um projeto de estudo.
 A ideia central era entender como funciona o sistema de trace do OpenTelemetry e realizar a instrumentalização manual da ferramenta utilizando o OpenTelemetry + Jaeger, evitando a perda de performance do uso do agent.

### Setup de ambiente
 - É necessário ter o java 17 instalado.

### Setup do Jaeger
 - Para configurar o Jaegar basta executar o comando:
```shell
    docker run --rm -it --name jaeger2  -e COLLECTOR_OTLP_ENABLED=true   -p 4317:4317   -p 16686:16686   jaegertracing/all-in-one:1.39
```