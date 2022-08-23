package cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    private static final String PASSWORD = "super_secret";
    @Autowired
    private Stats stats;

    @Autowired
    Theatre theatre = new Theatre();

    List<Token> tokens = new ArrayList<>();

    @GetMapping("/seats")
    public Theatre getSeats () {
        return theatre;
    }
    @PostMapping("/purchase")
    public ResponseEntity<Object> buyTicket (@RequestBody Seats ticket) {
        class error {
            public String error;
            public error (String error) {
                this.error = error;
            }
        }
        if (ticket.getColumn() > 9 | ticket.getRow() > 9 | ticket.getColumn() < 0 | ticket.getRow() < 0) {
            return new ResponseEntity<>(new error("The number of a row or a column is out of bounds!"), HttpStatus.BAD_REQUEST);
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
                } else return new ResponseEntity<>(new error("The ticket has been already purchased!"), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(new error("The ticket has been already purchased!"), HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/return")
    public ResponseEntity<Object> returnTicket (@RequestBody Map<String, String> token) {
        class error {
            public String error;
            public error (String error) {
                this.error = error;
            }
        }
        class returned {
            public Seats returned_ticket;
            public returned (Seats seats) {
                this.returned_ticket = seats;
            }
        }
        for (Token token1 : tokens) {
            if (token1.getToken().equals(token.get("token"))) {
                int price = theatre.getBookedSeats().stream().filter(s -> s.isSame(token1.getTicket())).collect(Collectors.toList()).get(0).getPrice();
                theatre.getBookedSeats().stream().filter(s -> s.isSame(token1.getTicket())).collect(Collectors.toList()).get(0).setBooked(false);
                stats.setCurrent_income(stats.getCurrent_income() - price);
                stats.setNumber_of_available_seats(theatre.getAvailable_seats().size());
                stats.setNumber_of_purchased_tickets(stats.getNumber_of_purchased_tickets() - 1);
                return new ResponseEntity<>(new returned(token1.getTicket()), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new error("Wrong token!"), HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/stats")
    public ResponseEntity<Object> getStats (@RequestParam(name = "password", required = false) String password) {
        class error {
            public String error;
            public error (String error) {
                this.error = error;
            }
        }
        if (password == null || !password.equals(PASSWORD)) return new ResponseEntity<>(new error("The password is wrong!"), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}
