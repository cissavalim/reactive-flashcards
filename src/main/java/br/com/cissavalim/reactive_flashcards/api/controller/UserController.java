package br.com.cissavalim.reactive_flashcards.api.controller;

import br.com.cissavalim.reactive_flashcards.api.controller.request.UserRequest;
import br.com.cissavalim.reactive_flashcards.api.controller.response.UserResponse;
import br.com.cissavalim.reactive_flashcards.api.mapper.UserMapper;
import br.com.cissavalim.reactive_flashcards.core.validation.MongoId;
import br.com.cissavalim.reactive_flashcards.domain.service.UserService;
import br.com.cissavalim.reactive_flashcards.domain.service.query.UserQueryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Validated
@RequestMapping("users")
@Slf4j
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserQueryService userQueryService;
    private final UserMapper userMapper;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<UserResponse> save(@RequestBody @Valid final UserRequest request) {
        return userService.save(userMapper.toDocument(request))
                .doFirst(() -> log.info("saving-user-with-name={}", request.name()))
                .map(userMapper::toResponse);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}")
    public Mono<UserResponse> findById(@PathVariable @Valid @MongoId(message = "{userController.id}") final String id) {
        return userQueryService.findById(id)
                .doFirst(() -> log.info("find-user-by-id={}", id))
                .map(userMapper::toResponse);
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE, value = "{id}")
    public Mono<UserResponse> update(@PathVariable @Valid @MongoId(message = "{userController.id}") final String id,
                                     @RequestBody @Valid final UserRequest request) {
        return userService.update(userMapper.toDocument(request, id))
                .doFirst(() -> log.info("updating-user-with-id={}", id))
                .map(userMapper::toResponse);
    }

    @DeleteMapping(value = "{id}")
    @ResponseStatus(NO_CONTENT)
    public Mono<Void> delete(@PathVariable @Valid @MongoId(message = "{userController.id}") final String id) {
        return userService.delete(id)
                .doFirst(() -> log.info("deleting-user-with-id={}", id));
    }
}
