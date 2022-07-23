package cinema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Seats {
    private int row;
    private int column;
    private int price;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean booked;
}
