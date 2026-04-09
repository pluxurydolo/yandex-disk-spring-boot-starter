package com.pluxurydolo.yandexdisk.flow;

import com.pluxurydolo.yandexdisk.dto.request.DownloadFileRequest;
import com.pluxurydolo.yandexdisk.web.YandexDiskApiWebClient;
import com.pluxurydolo.yandexdisk.web.YandexDiskDownloaderWebClient;
import reactor.core.publisher.Mono;

public class DownloadFileFlow {
    private final YandexDiskApiWebClient yandexDiskApiWebClient;
    private final YandexDiskDownloaderWebClient yandexDiskDownloaderWebClient;

    public DownloadFileFlow(
        YandexDiskApiWebClient yandexDiskApiWebClient,
        YandexDiskDownloaderWebClient yandexDiskDownloaderWebClient
    ) {
        this.yandexDiskApiWebClient = yandexDiskApiWebClient;
        this.yandexDiskDownloaderWebClient = yandexDiskDownloaderWebClient;
    }

    public Mono<byte[]> downloadFile(DownloadFileRequest request) {
        String path = request.path();

        return yandexDiskApiWebClient.getDownloadLink(path)
            .flatMap(yandexDiskDownloaderWebClient::getFileLocation)
            .flatMap(yandexDiskDownloaderWebClient::downloadFile);
    }
}
