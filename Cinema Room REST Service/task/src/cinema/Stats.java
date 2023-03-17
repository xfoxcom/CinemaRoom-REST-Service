package cinema;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Stats {

    private int current_income;

    private int number_of_available_seats;

    private int number_of_purchased_tickets;

    public Stats () {
        this.current_income = 0;
        this.number_of_available_seats = 81;
        this.number_of_purchased_tickets = 0;
    }
}
