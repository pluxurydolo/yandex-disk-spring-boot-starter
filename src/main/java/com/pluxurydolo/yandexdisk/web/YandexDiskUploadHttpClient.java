package com.pluxurydolo.yandexdisk.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PutExchange;
import reactor.core.publisher.Mono;

import static org.springframework.util.MimeTypeUtils.TEXT_PLAIN_VALUE;

@HttpExchange
public interface YandexDiskUploadHttpClient {

    @PutExchange(contentType = TEXT_PLAIN_VALUE)
    Mono<ResponseEntity<Void>> upload(@RequestBody byte[] file);
}
