package Models.AvroModels;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HubEventAvro {
    private String hubId;
    private Instant timestamp;
    private Object payload;
} 