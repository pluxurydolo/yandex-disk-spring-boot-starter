package com.pluxurydolo.yandexdisk.flow;

import com.pluxurydolo.yandexdisk.dto.request.UploadFileRequest;
import com.pluxurydolo.yandexdisk.web.YandexDiskApiWebClient;
import reactor.core.publisher.Mono;

public class UploadFileFlow {
    private final YandexDiskApiWebClient yandexDiskApiWebClient;

    public UploadFileFlow(YandexDiskApiWebClient yandexDiskApiWebClient) {
        this.yandexDiskApiWebClient = yandexDiskApiWebClient;
    }

    public Mono<String> uploadFile(UploadFileRequest request) {
        String path = request.path();
        byte[] file = request.file();

        return yandexDiskApiWebClient.getUploadLink(path)
            .flatMap(uploadLink -> yandexDiskApiWebClient.uploadFile(uploadLink, file));
    }
}
