package com.pluxurydolo.yandexdisk.web;

import com.pluxurydolo.yandexdisk.dto.response.YandexDiskMediaResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Mono;

@HttpExchange(url = "https://cloud-api.yandex.net")
public interface YandexDiskApiHttpClient {

    @GetExchange("/v1/disk/resources/upload")
    Mono<YandexDiskMediaResponse> getUploadLink(
        @RequestParam("path") String path,
        @RequestParam("overwrite") boolean overwrite
    );

    @GetExchange("/v1/disk/resources/download")
    Mono<YandexDiskMediaResponse> getDownloadLink(
        @RequestParam("path") String path
    );

    @DeleteExchange("/v1/disk/resources")
    Mono<ResponseEntity<Void>> deleteFile(
        @RequestParam("path") String path,
        @RequestParam("permanently") boolean permanently
    );
}
