package cl.bci.test.samus.bcitest.rest.api;

import cl.bci.test.samus.bcitest.dto.UserDTO;
import cl.bci.test.samus.bcitest.dto.UserResponseDTO;
import cl.bci.test.samus.bcitest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserAPI {
    private final UserService userService;

    @Autowired
    public UserAPI(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public Mono<ResponseEntity<Mono<UserResponseDTO>>> createUser(@Valid @RequestBody UserDTO user) {
        return Mono.just(ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(user)));
    }
}
