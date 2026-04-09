package com.pluxurydolo.yandexdisk.web;

import com.pluxurydolo.yandexdisk.dto.response.YandexDiskMediaResponse;
import com.pluxurydolo.yandexdisk.exception.YandexDiskDownloadFileException;
import com.pluxurydolo.yandexdisk.exception.YandexDiskGetFileLocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.util.unit.DataSize;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Consumer;

import static java.net.URI.create;

public class YandexDiskDownloaderWebClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(YandexDiskDownloaderWebClient.class);

    private final WebClient webClient;

    public YandexDiskDownloaderWebClient() {
        int maxInMemorySize = (int) DataSize.ofMegabytes(64)
            .toBytes();

        Consumer<ClientCodecConfigurer> clientCodecConfigurer = codecs -> codecs.defaultCodecs()
            .maxInMemorySize(maxInMemorySize);

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
            .codecs(clientCodecConfigurer)
            .build();

        this.webClient = WebClient.builder()
            .exchangeStrategies(exchangeStrategies)
            .baseUrl("https://downloader.disk.yandex.ru")
            .build();
    }

    public Mono<URI> getFileLocation(YandexDiskMediaResponse downloadLink) {
        String href = downloadLink.href();
        URI uri = create(href);

        return webClient.get()
            .uri(uri)
            .retrieve()
            .toBodilessEntity()
            .map(HttpEntity::getHeaders)
            .mapNotNull(HttpHeaders::getLocation)
            .doOnSuccess(_ -> LOGGER.info("yhcj [yandex-disk-starter] Местонахождение файла успешно получено"))
            .onErrorResume(throwable -> {
                LOGGER.error("rgiw [yandex-disk-starter] Произошла ошибка при получении местонахождения файла");
                return Mono.error(new YandexDiskGetFileLocationException(throwable));
            });
    }

    public Mono<byte[]> downloadFile(URI fileLocation) {
        return webClient.get()
            .uri(fileLocation)
            .retrieve()
            .bodyToMono(byte[].class)
            .doOnSuccess(_ -> LOGGER.info("rrew [yandex-disk-starter] Файл успешно скачан с диска"))
            .onErrorResume(throwable -> {
                LOGGER.error("pzjm [yandex-disk-starter] Произошла ошибка при скачивании файла с диска");
                return Mono.error(new YandexDiskDownloadFileException(throwable));
            });
    }
}
