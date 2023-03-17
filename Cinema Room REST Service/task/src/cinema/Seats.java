package cinema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Seats {

    private int row;

    private int column;

    private int price;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean booked;

    public boolean isSame (Seats seats) {
        return this.row == seats.getRow() & this.column == seats.getColumn() & this.price == seats.getPrice();
    }
}
