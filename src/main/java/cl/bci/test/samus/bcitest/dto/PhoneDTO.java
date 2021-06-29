package cl.bci.test.samus.bcitest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class PhoneDTO {
    @NotBlank(message = "Number cannot be empty")
    private String number;
    @NotBlank(message = "City code cannot be empty")
    private String cityCode;
    @NotBlank(message = "Country code cannot be empty")
    private String countryCode;
}
