package cl.bci.test.samus.bcitest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    @NotBlank(message = "Name code cannot be empty")
    private String name;
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email code cannot be empty")
    private String email;
    @NotBlank(message = "Password code cannot be empty")
    @Pattern(regexp = "^(?=.*?\\d.*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{8,16}$",
            message = "Password must have at least one upper case letter, one lower case letter and two numbers. " +
                    "Also the size must be between 8 and 16")
    private String password;
    @NotEmpty(message = "Phones must have at least one value")
    @Valid
    private List<PhoneDTO> phones;
}
