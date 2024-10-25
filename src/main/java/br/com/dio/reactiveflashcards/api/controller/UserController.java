package br.com.dio.reactiveflashcards.api.controller;

import br.com.dio.reactiveflashcards.api.controller.request.UserRequest;
import br.com.dio.reactiveflashcards.api.controller.response.UserResponse;
import br.com.dio.reactiveflashcards.api.mapper.UserMapper;
import br.com.dio.reactiveflashcards.config.validation.MongoId;
import br.com.dio.reactiveflashcards.domain.service.UserService;
import br.com.dio.reactiveflashcards.domain.service.query.UserQueryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("users")
@Slf4j
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserQueryService userQueryService;
    private final UserMapper userMapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserResponse> save(@Valid @RequestBody final UserRequest userRequest) {
        return userService.save(userMapper.toDocument(userRequest))
                .doFirst(() -> log.info("saving-user={}", userRequest.name()))
                .map(userMapper::toResponse);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "{id}")
    public Mono<UserResponse> findById(@PathVariable @Valid @MongoId(message = "{userController.id}") final String id) {
        return userQueryService.findById(id)
                .doFirst(() -> log.info("finding-user={}", id))
                .map(userMapper::toResponse);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, value = "{id}")
    public Mono<UserResponse> update(@PathVariable @Valid @MongoId(message = "{userController.id}") final String id,
                                     @Valid @RequestBody final UserRequest userRequest) {
        return userService.update(userMapper.toDocument(userRequest, id))
                .doFirst(() -> log.info("updating-user-id={}", id))
                .map(userMapper::toResponse);
    }

    @DeleteMapping(value = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable @Valid @MongoId(message = "{userController.id}") final String id) {
        return userService.delete(id)
                .doFirst(() -> log.info("deleting-user={}", id));
    }
}
