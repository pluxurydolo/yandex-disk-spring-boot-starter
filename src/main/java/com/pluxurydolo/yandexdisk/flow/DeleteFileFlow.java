package com.pluxurydolo.yandexdisk.flow;

import com.pluxurydolo.yandexdisk.dto.request.DeleteFileRequest;
import com.pluxurydolo.yandexdisk.web.YandexDiskApiWebClient;
import reactor.core.publisher.Mono;

public class DeleteFileFlow {
    private final YandexDiskApiWebClient yandexDiskApiWebClient;

    public DeleteFileFlow(YandexDiskApiWebClient yandexDiskApiWebClient) {
        this.yandexDiskApiWebClient = yandexDiskApiWebClient;
    }

    public Mono<String> deleteFile(DeleteFileRequest request) {
        String path = request.path();
        return yandexDiskApiWebClient.deleteFile(path);
    }
}
