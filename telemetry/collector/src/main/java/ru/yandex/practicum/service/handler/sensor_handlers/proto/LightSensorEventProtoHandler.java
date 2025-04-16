package ru.yandex.practicum.service.handler.sensor_handlers.proto;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.service.KafkaEventProducer;

@Component
public class LightSensorEventProtoHandler extends BaseSensorEventProtoHandler<LightSensorAvro> implements SensorEventProtoHandler {
    public LightSensorEventProtoHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected LightSensorAvro mapToAvro(SensorEventProto event) {
        LightSensorProto proto = event.getLightSensorEvent();
        return LightSensorAvro.newBuilder()
                .setLuminosity(proto.getLuminosity())
                .setLinkQuality(proto.getLinkQuality())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR_EVENT;
    }
}
