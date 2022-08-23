package cinema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    private String token;
    private Seats ticket;
    public void setToken() {
        UUID uuid = UUID.randomUUID();
        this.token = uuid.toString();
    }
}
