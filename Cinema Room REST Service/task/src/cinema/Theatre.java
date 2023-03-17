package cinema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
public class Theatre {

    @Value("${total_rows}")
    private int total_rows;

    @Value("${total_columns}")
    private int total_columns;

    private List<Seats> available_seats = new ArrayList<>();

    private Theatre() {
        for (int i = 1; i <= total_rows; i++) {
            for (int j = 1; j <= total_columns; j++) {

                Seats seat = Seats.builder()
                        .booked(false)
                        .row(i)
                        .column(j)
                        .build();

                if (i <= 4) {
                    seat.setPrice(10);
                } else seat.setPrice(8);

                available_seats.add(seat);
            }
        }
    }

    public List<Seats> getAvailable_seats() {
        return available_seats.stream()
                .filter(s -> !s.isBooked())
                .collect(Collectors.toList());
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public List<Seats> getBookedSeats() {
        return available_seats.stream()
                .filter(Seats::isBooked)
                .collect(Collectors.toList());
    }
}
