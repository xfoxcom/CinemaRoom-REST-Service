package cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.stream.Collectors;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    Theatre theatre = new Theatre();

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
        if (ticket.getColumn() > 9 | ticket.getRow() > 9) {
            return new ResponseEntity<>(new error("The number of a row or a column is out of bounds!"), HttpStatus.BAD_REQUEST);
        }
        for (Seats seat : theatre.getAvailable_seats()) {
            if (ticket.getRow() == seat.getRow() & ticket.getColumn() == seat.getColumn()) {
                if (!seat.isBooked()) {
                    seat.setBooked(true);
                    return new ResponseEntity<>(seat, HttpStatus.OK);
                } else return new ResponseEntity<>(new error("The ticket has been already purchased!"), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(new error("The ticket has been already purchased!"), HttpStatus.BAD_REQUEST);
    }
}
