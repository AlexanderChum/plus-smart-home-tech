package Models.HubEvent.ScenarioEventImpl;

import Models.Enums.ConditionType;
import Models.Enums.Operation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ScenatioCondition {
    private String sensorId;
    private ConditionType type;
    private Operation operation;
    private Integer value;
}
