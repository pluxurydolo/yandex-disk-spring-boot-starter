package com.pluxurydolo.yandexdisk.flow;

import com.pluxurydolo.yandexdisk.dto.request.UploadFileRequest;
import com.pluxurydolo.yandexdisk.dto.response.YandexDiskMediaResponse;
import com.pluxurydolo.yandexdisk.web.YandexDiskApiHttpClient;
import com.pluxurydolo.yandexdisk.web.factory.YandexDiskUploadClientFactory;
import com.pluxurydolo.yandexdisk.web.YandexDiskUploadHttpClient;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public class UploadFileFlow {
    private final YandexDiskApiHttpClient yandexDiskApiHttpClient;
    private final YandexDiskUploadClientFactory yandexDiskUploadClientFactory;

    public UploadFileFlow(
        YandexDiskApiHttpClient yandexDiskApiHttpClient,
        YandexDiskUploadClientFactory yandexDiskUploadClientFactory
    ) {
        this.yandexDiskApiHttpClient = yandexDiskApiHttpClient;
        this.yandexDiskUploadClientFactory = yandexDiskUploadClientFactory;
    }

    public Mono<String> uploadFile(UploadFileRequest request) {
        String path = request.path();
        byte[] file = request.file();
        boolean overwrite = true;

        return yandexDiskApiHttpClient.getUploadLink(path, overwrite)
            .flatMap(uploadLink -> upload(uploadLink, file))
            .thenReturn(path);
    }

    private Mono<ResponseEntity<Void>> upload(YandexDiskMediaResponse uploadLink, byte[] file) {
        String href = uploadLink.href();
        YandexDiskUploadHttpClient client = yandexDiskUploadClientFactory.create(href);
        return client.upload(file);
    }
}
