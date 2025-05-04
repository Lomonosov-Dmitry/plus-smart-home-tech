package ru.yandex.practicum.service.handler.sensor_handlers.proto;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.service.KafkaEventProducer;

@Component
public class MotionSensorEventProtoHandler extends BaseSensorEventProtoHandler<MotionSensorAvro> implements SensorEventProtoHandler {
    public MotionSensorEventProtoHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected MotionSensorAvro mapToAvro(SensorEventProto event) {
        MotionSensorProto proto = event.getMotionSensorEvent();
        return MotionSensorAvro.newBuilder()
                .setVoltage(proto.getVoltage())
                .setLinkQuality(proto.getLinkQuality())
                .setMotion(proto.getMotion())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_EVENT;
    }
}
