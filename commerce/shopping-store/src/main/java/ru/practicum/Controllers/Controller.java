package ru.practicum.Controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.Services.StoreService;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class Controller {
    StoreService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public void getStore() {

    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void putStore() {

    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void postStore() {

    }

    @PostMapping("/removeProductFromStore")
    @ResponseStatus(HttpStatus.OK)
    public void postStore2() {

    }

    @PostMapping("/quantityState")
    @ResponseStatus(HttpStatus.OK)
    public void postStore3() {

    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public void getStore2(@RequestParam Long productId) {

    }
}
