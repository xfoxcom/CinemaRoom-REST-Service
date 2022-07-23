package cinema;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Component
public class Theatre {
    private final int total_rows = 9;
    private final int total_columns = 9;
    private List<Seats> available_seats = new ArrayList<>();
    public Theatre () {
        for (int i = 1; i <=9; i++) {
            for (int j = 1; j<=9; j++) {
                Seats seat = new Seats();
                seat.setBooked(false);
                seat.setRow(i);
                seat.setColumn(j);
                if (seat.getRow() <= 4) {
                    seat.setPrice(10);
                } else seat.setPrice(8);
                available_seats.add(seat);
            }
        }
    }

    public List<Seats> getAvailable_seats() {
        return available_seats.stream().filter(s -> !s.isBooked()).collect(Collectors.toList());
    }
}
