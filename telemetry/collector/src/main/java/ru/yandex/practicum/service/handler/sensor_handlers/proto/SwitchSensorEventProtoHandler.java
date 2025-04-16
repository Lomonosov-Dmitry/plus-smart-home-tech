package ru.yandex.practicum.service.handler.sensor_handlers.proto;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.service.KafkaEventProducer;

@Component
public class SwitchSensorEventProtoHandler extends BaseSensorEventProtoHandler<SwitchSensorAvro> implements SensorEventProtoHandler {
    public SwitchSensorEventProtoHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected SwitchSensorAvro mapToAvro(SensorEventProto event) {
        SwitchSensorProto proto = event.getSwitchSensorEvent();
        return SwitchSensorAvro.newBuilder()
                .setState(proto.getState())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR_EVENT;
    }
}
