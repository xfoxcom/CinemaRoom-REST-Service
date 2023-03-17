package cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class Controller {

    @Value("PASSWORD")
    private String PASSWORD;

    @Autowired
    private Stats stats;

    @Autowired
    private Theatre theatre;

    List<Token> tokens = new ArrayList<>();

    @GetMapping("/seats")
    public Theatre getSeats() {
        return theatre;
    }

    @PostMapping("/purchase")
    public ResponseEntity<Object> buyTicket(@RequestBody Seats ticket) {

        if (ticket.getColumn() > 9 | ticket.getRow() > 9 | ticket.getColumn() < 0 | ticket.getRow() < 0) {
            return new ResponseEntity<>(new Response("The number of a row or a column is out of bounds!"), HttpStatus.BAD_REQUEST);
        }

        for (Seats seat : theatre.getAvailable_seats()) {
            if (ticket.getRow() == seat.getRow() & ticket.getColumn() == seat.getColumn()) {
                if (!seat.isBooked()) {
                    seat.setBooked(true);
                    Token token = new Token();
                    token.setToken();
                    token.setTicket(seat);
                    tokens.add(token);
                    stats.setCurrent_income(stats.getCurrent_income() + seat.getPrice());
                    stats.setNumber_of_available_seats(theatre.getAvailable_seats().size());
                    stats.setNumber_of_purchased_tickets(stats.getNumber_of_purchased_tickets() + 1);
                    return new ResponseEntity<>(token, HttpStatus.OK);
                } else
                    return new ResponseEntity<>(new Response("The ticket has been already purchased!"), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(new Response("The ticket has been already purchased!"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/return")
    public ResponseEntity<Object> returnTicket(@RequestBody Map<String, String> token) {

        for (Token token1 : tokens) {
            if (token1.getToken().equals(token.get("token"))) {

                int price = theatre.getBookedSeats().stream()
                        .filter(s -> s.isSame(token1.getTicket()))
                        .collect(Collectors.toList())
                        .get(0)
                        .getPrice();

                theatre.getBookedSeats().stream()
                        .filter(s -> s.isSame(token1.getTicket()))
                        .collect(Collectors.toList())
                        .get(0)
                        .setBooked(false);

                stats.setCurrent_income(stats.getCurrent_income() - price);
                stats.setNumber_of_available_seats(theatre.getAvailable_seats().size());
                stats.setNumber_of_purchased_tickets(stats.getNumber_of_purchased_tickets() - 1);

                return new ResponseEntity<>(new ReturnTicket(token1.getTicket()), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new Response("Wrong token!"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam(name = "password", required = false) String password) {

        if (password == null || !password.equals(PASSWORD))
            return new ResponseEntity<>(new Response("The password is wrong!"), HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}
