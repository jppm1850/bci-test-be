package cl.bci.test.samus.bcitest.data.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Table("phones")
public class Phone {
    @Column("user_id")
    private UUID userId;
    private Long id;
    private String number;
    @Column("city_code")
    private String cityCode;
    @Column("country_code")
    private String countryCode;
}
