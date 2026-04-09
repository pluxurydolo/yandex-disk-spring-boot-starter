package com.pluxurydolo.yandexdisk.web;

import com.pluxurydolo.yandexdisk.dto.response.YandexDiskMediaResponse;
import com.pluxurydolo.yandexdisk.exception.YandexDiskDeleteFileException;
import com.pluxurydolo.yandexdisk.exception.YandexDiskGetDownloadLinkException;
import com.pluxurydolo.yandexdisk.exception.YandexDiskGetUploadLinkException;
import com.pluxurydolo.yandexdisk.exception.YandexDiskUploadFileException;
import com.pluxurydolo.yandexdisk.properties.YandexDiskProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Function;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

public class YandexDiskApiWebClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(YandexDiskApiWebClient.class);

    private final WebClient webClient;

    public YandexDiskApiWebClient(YandexDiskProperties yandexDiskProperties) {
        this.webClient = WebClient.builder()
            .baseUrl("https://cloud-api.yandex.net")
            .defaultHeader(AUTHORIZATION, authorizationHeader(yandexDiskProperties))
            .build();
    }

    public Mono<YandexDiskMediaResponse> getUploadLink(String path) {
        Function<UriBuilder, URI> uri = uriBuilder -> uriBuilder.path("/v1/disk/resources/upload")
            .queryParam("path", path)
            .queryParam("overwrite", true)
            .build();

        return webClient.get()
            .uri(uri)
            .retrieve()
            .bodyToMono(YandexDiskMediaResponse.class)
            .doOnSuccess(_ -> LOGGER.info("nrhf [yandex-disk-starter] Ссылка для скачивания файла на диск успешно получена"))
            .onErrorResume(throwable -> {
                LOGGER.error("qvgn [yandex-disk-starter] Произошла ошибка при получении ссылки для скачивания файла на диск");
                return Mono.error(new YandexDiskGetUploadLinkException(throwable));
            });
    }

    public Mono<String> uploadFile(YandexDiskMediaResponse uploadLink, byte[] file) {
        String href = uploadLink.href();

        return WebClient.builder()
            .baseUrl(href)
            .build()
            .put()
            .contentType(TEXT_PLAIN)
            .body(fromValue(file))
            .retrieve()
            .bodyToMono(Void.class)
            .thenReturn(href)
            .doOnSuccess(_ -> LOGGER.info("aboq [yandex-disk-starter] Файл успешно загружен на диск"))
            .onErrorResume(throwable -> {
                LOGGER.error("ffso [yandex-disk-starter] Произошла ошибка при загрузке файла на диск");
                return Mono.error(new YandexDiskUploadFileException(throwable));
            });
    }

    public Mono<YandexDiskMediaResponse> getDownloadLink(String path) {
        Function<UriBuilder, URI> uri = uriBuilder -> uriBuilder.path("/v1/disk/resources/download")
            .queryParam("path", path)
            .build();

        return webClient.get()
            .uri(uri)
            .retrieve()
            .bodyToMono(YandexDiskMediaResponse.class)
            .doOnSuccess(_ -> LOGGER.info("nonf [yandex-disk-starter] Ссылка для скачивания файла с диска успешно получена"))
            .onErrorResume(throwable -> {
                LOGGER.error("lfpq [yandex-disk-starter] Произошла ошибка при получении ссылки для скачивания файла с диска");
                return Mono.error(new YandexDiskGetDownloadLinkException(throwable));
            });
    }

    public Mono<String> deleteFile(String path) {
        Function<UriBuilder, URI> uri = uriBuilder -> uriBuilder.path("/v1/disk/resources")
            .queryParam("path", path)
            .queryParam("permanently", true)
            .build();

        return webClient.delete()
            .uri(uri)
            .retrieve()
            .toBodilessEntity()
            .thenReturn(path)
            .doOnSuccess(_ -> LOGGER.info("jace [yandex-disk-starter] Файл {} успешно удален с диска", path))
            .onErrorResume(throwable -> {
                LOGGER.error("dqza [yandex-disk-starter] Произошла ошибка при удалении файла с диска");
                return Mono.error(new YandexDiskDeleteFileException(throwable));
            });
    }

    private static String authorizationHeader(YandexDiskProperties yandexDiskProperties) {
        String token = yandexDiskProperties.token();
        return String.format("OAuth %s", token);
    }
}
