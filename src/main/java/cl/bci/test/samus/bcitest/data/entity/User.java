package cl.bci.test.samus.bcitest.data.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Table("users")
public class User {
    @Id
    @Column("id")
    private UUID id;
    private String name;
    private String email;
    private String password;
    private LocalDate created;
    private LocalDate modified;
    @Column("last_login")
    private LocalDate lastLogin;
    private String token;
    private boolean active;
}
