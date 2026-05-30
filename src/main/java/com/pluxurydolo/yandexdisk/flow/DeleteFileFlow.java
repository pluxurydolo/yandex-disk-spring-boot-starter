package com.pluxurydolo.yandexdisk.flow;

import com.pluxurydolo.yandexdisk.dto.request.DeleteFileRequest;
import com.pluxurydolo.yandexdisk.web.YandexDiskApiHttpClient;
import reactor.core.publisher.Mono;

public class DeleteFileFlow {
    private final YandexDiskApiHttpClient yandexDiskApiHttpClient;

    public DeleteFileFlow(YandexDiskApiHttpClient yandexDiskApiHttpClient) {
        this.yandexDiskApiHttpClient = yandexDiskApiHttpClient;
    }

    public Mono<String> deleteFile(DeleteFileRequest request) {
        String path = request.path();
        boolean permanently = true;

        return yandexDiskApiHttpClient.deleteFile(path, permanently)
            .thenReturn(path);
    }
}
