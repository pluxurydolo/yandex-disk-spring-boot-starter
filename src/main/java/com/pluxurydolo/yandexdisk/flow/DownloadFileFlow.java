package com.pluxurydolo.yandexdisk.flow;

import com.pluxurydolo.yandexdisk.dto.request.DownloadFileRequest;
import com.pluxurydolo.yandexdisk.dto.response.YandexDiskMediaResponse;
import com.pluxurydolo.yandexdisk.web.YandexDiskApiHttpClient;
import com.pluxurydolo.yandexdisk.web.factory.YandexDiskDownloaderClientFactory;
import com.pluxurydolo.yandexdisk.web.YandexDiskDownloaderHttpClient;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.net.URI;

public class DownloadFileFlow {
    private final YandexDiskApiHttpClient yandexDiskApiHttpClient;
    private final YandexDiskDownloaderClientFactory yandexDiskDownloaderClientFactory;

    public DownloadFileFlow(
        YandexDiskApiHttpClient yandexDiskApiHttpClient,
        YandexDiskDownloaderClientFactory yandexDiskDownloaderClientFactory
    ) {
        this.yandexDiskApiHttpClient = yandexDiskApiHttpClient;
        this.yandexDiskDownloaderClientFactory = yandexDiskDownloaderClientFactory;
    }

    public Mono<byte[]> downloadFile(DownloadFileRequest request) {
        String path = request.path();

        return yandexDiskApiHttpClient.getDownloadLink(path)
            .flatMap(this::getFileLocation)
            .flatMap(this::download);
    }

    private Mono<ResponseEntity<Void>> getFileLocation(YandexDiskMediaResponse link) {
        String href = link.href();
        URI uri = URI.create(href);

        YandexDiskDownloaderHttpClient client = yandexDiskDownloaderClientFactory.create();
        return client.getFileLocation(uri);
    }

    private Mono<byte[]> download(ResponseEntity<Void> response) {
        URI location = response.getHeaders().getLocation();
        YandexDiskDownloaderHttpClient client = yandexDiskDownloaderClientFactory.create();
        return client.downloadFile(location);
    }
}
