package cl.bci.test.samus.bcitest.data.repository;

import cl.bci.test.samus.bcitest.data.entity.User;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface UserRepository extends ReactiveSortingRepository<User, UUID> {
    Mono<User> findByEmail(String email);
}
