package hungry.monkey.intrusion.detector.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "UserData")
public class UserData implements Serializable {
    @Getter
    @Setter
    private String userName;

    @Id
    @Setter
    @Getter
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    private Integer count;

    @Override
    @SneakyThrows
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}
