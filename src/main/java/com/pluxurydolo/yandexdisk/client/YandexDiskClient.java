package com.pluxurydolo.yandexdisk.client;

import com.pluxurydolo.yandexdisk.dto.YandexDiskDefaultResponse;
import com.pluxurydolo.yandexdisk.wrapper.YandexDiskDownloadingClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Function;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

public class YandexDiskClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(YandexDiskClient.class);

    private final YandexDiskDownloadingClient yandexDiskDownloadingClient;
    private final WebClient yandexDiskRestClient;

    public YandexDiskClient(YandexDiskDownloadingClient yandexDiskDownloadingClient, String yandexDiskToken) {
        this.yandexDiskDownloadingClient = yandexDiskDownloadingClient;

        this.yandexDiskRestClient = WebClient.builder()
            .defaultHeader(AUTHORIZATION, authorizationHeader(yandexDiskToken))
            .baseUrl("https://cloud-api.yandex.net")
            .build();
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
            .doOnSuccess(_ -> LOGGER.info("nrhf [yandex-disk-starter] Ссылка для скачивания на диск успешно получена"));
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
            .doOnSuccess(_ -> LOGGER.info("aboq [yandex-disk-starter] Файл успешно загружен на диск"));
    }

    public Mono<YandexDiskDefaultResponse> getDownloadLink(String path) {
        Function<UriBuilder, URI> uri = uriBuilder -> uriBuilder.path("/v1/disk/resources/download")
            .queryParam("path", path)
            .build();

        return yandexDiskRestClient.get()
            .uri(uri)
            .retrieve()
            .bodyToMono(YandexDiskDefaultResponse.class)
            .doOnSuccess(_ -> LOGGER.info("nonf [yandex-disk-starter] Ссылка для скачивания с диска успешно получена"));
    }

    public Mono<byte[]> downloadFile(URI fileLocation) {
        return yandexDiskDownloadingClient.webClient()
            .get()
            .uri(fileLocation)
            .retrieve()
            .bodyToMono(byte[].class)
            .doOnSuccess(_ -> LOGGER.info("rrew [yandex-disk-starter] Файл успешно скачан с диска"));
    }

    public Mono<URI> getFileLocation(YandexDiskDefaultResponse downloadLink) {
        String href = downloadLink.href();
        URI uri = URI.create(href);

        return yandexDiskDownloadingClient.webClient()
            .get()
            .uri(uri)
            .retrieve()
            .toBodilessEntity()
            .map(HttpEntity::getHeaders)
            .mapNotNull(HttpHeaders::getLocation)
            .doOnSuccess(_ -> LOGGER.info("yhcj [yandex-disk-starter] Местонахождение файла успешно получено"));
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
            .doOnSuccess(_ -> LOGGER.info("jace [yandex-disk-starter] Файл {} успешно удален с диска", path));
    }

    private static String authorizationHeader(String yandexDiskToken) {
        return String.format("OAuth %s", yandexDiskToken);
    }
}
