package cl.bci.test.samus.bcitest.data.repository;

import cl.bci.test.samus.bcitest.data.entity.Phone;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PhoneRepository extends ReactiveSortingRepository<Phone, UUID> {
}
