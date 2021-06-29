package cl.bci.test.samus.bcitest.service;

import cl.bci.test.samus.bcitest.dto.UserDTO;
import cl.bci.test.samus.bcitest.dto.UserResponseDTO;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserResponseDTO> createUser(UserDTO user);
}
