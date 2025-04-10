package ru.yandex.practicum.model.sensor_events;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class LightSensorEvent extends SensorEvent {
    int linkQuality;
    int luminosity;

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
