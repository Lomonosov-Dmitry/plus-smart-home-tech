package ru.yandex.practicum.service.handler.sensor_handlers.proto;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.service.KafkaEventProducer;

@Component
public class TemperatureSensorEventProtoHandler extends BaseSensorEventProtoHandler<TemperatureSensorAvro> implements SensorEventProtoHandler {
    public TemperatureSensorEventProtoHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected TemperatureSensorAvro mapToAvro(SensorEventProto event) {
        TemperatureSensorProto proto = event.getTemperatureSensorEvent();
        return TemperatureSensorAvro.newBuilder()
                .setTemperatureF(proto.getTemperatureF())
                .setTemperatureC(proto.getTemperatureC())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR_EVENT;
    }
}
