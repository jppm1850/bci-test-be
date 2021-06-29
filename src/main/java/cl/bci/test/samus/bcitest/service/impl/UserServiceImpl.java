package cl.bci.test.samus.bcitest.service.impl;

import cl.bci.test.samus.bcitest.data.entity.Phone;
import cl.bci.test.samus.bcitest.data.entity.User;
import cl.bci.test.samus.bcitest.data.repository.PhoneRepository;
import cl.bci.test.samus.bcitest.data.repository.UserRepository;
import cl.bci.test.samus.bcitest.dto.PhoneDTO;
import cl.bci.test.samus.bcitest.dto.UserDTO;
import cl.bci.test.samus.bcitest.dto.UserResponseDTO;
import cl.bci.test.samus.bcitest.exception.EmailExistsException;
import cl.bci.test.samus.bcitest.service.UserService;
import cl.bci.test.samus.bcitest.service.util.JWTUtil;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import io.awspring.cloud.messaging.core.QueueMessageChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Value("${app.queue.name}")
    private String queueName;
    private final AmazonSQSAsync amazonSqs;
    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;
    private final JWTUtil jwtUtil;

    @Autowired
    public UserServiceImpl(AmazonSQSAsync amazonSqs, UserRepository userRepository, PhoneRepository phoneRepository, JWTUtil jwtUtil) {
        this.amazonSqs = amazonSqs;
        this.userRepository = userRepository;
        this.phoneRepository = phoneRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<UserResponseDTO> createUser(UserDTO user) {
        return createUserIfNotExists(user).doOnNext(entity -> {
                    List<Phone> phones = new ArrayList<>();
                    for (PhoneDTO phoneDTO : user.getPhones()) {
                        var phone = new Phone();
                        phone.setUserId(entity.getId());
                        phone.setCityCode(phoneDTO.getCityCode());
                        phone.setCountryCode(phoneDTO.getCountryCode());
                        phone.setNumber(phoneDTO.getNumber());
                        phones.add(phone);
                    }
                    phoneRepository.saveAll(phones);
                }
        ).map(entity -> {
            sendMessage(entity.getEmail());
            return UserResponseDTO.builder().created(entity.getCreated())
                    .modified(entity.getModified()).active(entity.isActive())
                    .id(entity.getId()).lastLogin(entity.getLastLogin())
                    .token(entity.getToken()).build();

        });
    }

    private Mono<User> createUserIfNotExists(UserDTO user) {
        return userRepository.findByEmail(user.getEmail())
                .doOnNext(u -> {
                    throw new EmailExistsException(user.getEmail());
                }).switchIfEmpty(userRepository.save(fromDTO(user)));
    }

    private User fromDTO(UserDTO userDTO) {
        var user = new User();
        user.setName(userDTO.getName());
        user.setActive(true);
        user.setCreated(LocalDate.now());
        user.setLastLogin(LocalDate.now());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setToken(jwtUtil.getJWTToken(userDTO));
        return user;
    }

    @Async
    private void sendMessage(String email) {
        MessageChannel messageChannel = new QueueMessageChannel(amazonSqs, queueName);
        var message = String.format("{\"email\": \"%s\"}", email);
        Message<String> msg = MessageBuilder.withPayload(message)
                .build();
        messageChannel.send(msg);
    }
}
