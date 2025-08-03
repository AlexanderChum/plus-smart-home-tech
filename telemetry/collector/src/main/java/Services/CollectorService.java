package Services;

import Models.HubEvent.HubEvent;
import Models.SensorEvent.SensorEvent;

public interface CollectorService {
    void sendSensorData(SensorEvent event);

    void sendHubData(HubEvent event);
}
