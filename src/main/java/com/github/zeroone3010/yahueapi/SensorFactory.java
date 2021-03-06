package com.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zeroone3010.yahueapi.domain.SensorDto;

import java.net.MalformedURLException;
import java.net.URL;

final class SensorFactory {
  private SensorFactory() { /* prevent */ }

  static Sensor buildSensor(final String id, final SensorDto sensor, final String bridgeUri,
                            final ObjectMapper objectMapper) {
    if (sensor == null) {
      throw new HueApiException("Sensor " + id + " cannot be found.");
    }

    final URL url = buildSensorUrl(bridgeUri, id);

    final SensorType type = SensorType.parseTypeString(sensor.getType());
    switch (type) {
      case MOTION:
        return new MotionSensorImpl(id, sensor, url, objectMapper);
      case TEMPERATURE:
        return new TemperatureSensorImpl(id, sensor, url, objectMapper);
      case DAYLIGHT:
        return new DaylightSensorImpl(id, sensor, url, objectMapper);
      case DIMMER_SWITCH:
        return new DimmerSwitchImpl(id, sensor, url, objectMapper);
      default:
        return new BasicSensor(id, sensor, url, objectMapper);
    }

  }

  private static URL buildSensorUrl(final String bridgeUri, final String sensorId) {
    try {
      return new URL(bridgeUri + "sensors/" + sensorId);
    } catch (final MalformedURLException e) {
      throw new HueApiException(e);
    }
  }

}
