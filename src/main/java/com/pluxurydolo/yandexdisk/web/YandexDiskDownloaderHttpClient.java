package com.pluxurydolo.yandexdisk.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@HttpExchange
public interface YandexDiskDownloaderHttpClient {

    @GetExchange
    Mono<ResponseEntity<Void>> getFileLocation(URI uri);

    @GetExchange
    Mono<byte[]> downloadFile(URI fileLocation);
}
