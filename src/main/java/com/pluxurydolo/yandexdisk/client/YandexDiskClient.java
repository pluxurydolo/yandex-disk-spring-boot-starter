package com.pluxurydolo.yandexdisk.client;

import com.pluxurydolo.yandexdisk.dto.YandexDiskDefaultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Function;

import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

public class YandexDiskClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(YandexDiskClient.class);

    private final WebClient yandexDiskRestClient;
    private final WebClient yandexDiskDownloadingClient;

    public YandexDiskClient(WebClient yandexDiskRestClient, WebClient yandexDiskDownloadingClient) {
        this.yandexDiskRestClient = yandexDiskRestClient;
        this.yandexDiskDownloadingClient = yandexDiskDownloadingClient;
    }

    public Mono<YandexDiskDefaultResponse> getUploadLink(String path) {
        Function<UriBuilder, URI> uri = uriBuilder -> uriBuilder.path("/v1/disk/resources/upload")
            .queryParam("path", path)
            .queryParam("overwrite", true)
            .build();

        return yandexDiskRestClient.get()
            .uri(uri)
            .retrieve()
            .bodyToMono(YandexDiskDefaultResponse.class)
            .doOnSuccess(it -> LOGGER.info("nrhf Ссылка для скачивания на диск успешно получена"));
    }

    public Mono<Integer> uploadFile(YandexDiskDefaultResponse uploadLink, byte[] file) {
        String href = uploadLink.href();

        return WebClient.builder()
            .baseUrl(href)
            .build()
            .put()
            .contentType(TEXT_PLAIN)
            .body(fromValue(file))
            .retrieve()
            .bodyToMono(Void.class)
            .thenReturn(file.length)
            .doOnSuccess(it -> LOGGER.info("aboq Файл успешно загружен на диск"));
    }

    public Mono<YandexDiskDefaultResponse> getDownloadLink(String path) {
        Function<UriBuilder, URI> uri = uriBuilder -> uriBuilder.path("/v1/disk/resources/download")
            .queryParam("path", path)
            .build();

        return yandexDiskRestClient.get()
            .uri(uri)
            .retrieve()
            .bodyToMono(YandexDiskDefaultResponse.class)
            .doOnSuccess(it -> LOGGER.info("nonf Ссылка для скачивания с диска успешно получена"));
    }

    public Mono<byte[]> downloadFile(URI fileLocation) {
        return yandexDiskDownloadingClient.get()
            .uri(fileLocation)
            .retrieve()
            .bodyToMono(byte[].class)
            .doOnSuccess(it -> LOGGER.info("rrew Файл успешно скачан с диска"));
    }

    public Mono<URI> getFileLocation(YandexDiskDefaultResponse downloadLink) {
        String href = downloadLink.href();
        URI uri = URI.create(href);

        return yandexDiskDownloadingClient.get()
            .uri(uri)
            .retrieve()
            .toBodilessEntity()
            .map(HttpEntity::getHeaders)
            .mapNotNull(HttpHeaders::getLocation)
            .doOnSuccess(it -> LOGGER.info("yhcj Местонахождение файла успешно получено"));
    }

    public Mono<String> deleteFile(String path) {
        Function<UriBuilder, URI> uri = uriBuilder -> uriBuilder.path("/v1/disk/resources")
            .queryParam("path", path)
            .queryParam("permanently", true)
            .build();

        return yandexDiskRestClient.delete()
            .uri(uri)
            .retrieve()
            .toBodilessEntity()
            .thenReturn(path)
            .doOnSuccess(it -> LOGGER.info("jace Файл {} успешно удален с диска", path));
    }
}
