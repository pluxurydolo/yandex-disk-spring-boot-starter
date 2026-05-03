package com.pluxurydolo.yandexdisk.flow;

import com.pluxurydolo.yandexdisk.dto.request.DownloadFileRequest;
import com.pluxurydolo.yandexdisk.dto.response.YandexDiskMediaResponse;
import com.pluxurydolo.yandexdisk.web.YandexDiskApiWebClient;
import com.pluxurydolo.yandexdisk.web.YandexDiskDownloaderWebClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class DownloadFileFlowTests {

    @Mock
    private YandexDiskApiWebClient yandexDiskApiWebClient;

    @Mock
    private YandexDiskDownloaderWebClient yandexDiskDownloaderWebClient;

    @InjectMocks
    private DownloadFileFlow downloadFileFlow;

    @Test
    void testDownloadFile() {
        byte[] bytes = {};
        when(yandexDiskApiWebClient.getDownloadLink(anyString()))
            .thenReturn(Mono.just(yandexDiskMediaResponse()));
        when(yandexDiskDownloaderWebClient.getFileLocation(any()))
            .thenReturn(Mono.just(URI.create("")));
        when(yandexDiskDownloaderWebClient.downloadFile(any()))
            .thenReturn(Mono.just(bytes));

        Mono<byte[]> result = downloadFileFlow.downloadFile(downloadFileRequest());

        create(result)
            .expectNext(bytes)
            .verifyComplete();
    }

    @Test
    void testDownloadFileWhenExceptionOccurred() {
        when(yandexDiskApiWebClient.getDownloadLink(anyString()))
            .thenReturn(Mono.error(new RuntimeException()));

        Mono<byte[]> result = downloadFileFlow.downloadFile(downloadFileRequest());

        create(result)
            .verifyErrorMatches(throwable -> throwable.getClass().equals(RuntimeException.class));
    }

    private static DownloadFileRequest downloadFileRequest() {
        return new DownloadFileRequest("path");
    }

    private static YandexDiskMediaResponse yandexDiskMediaResponse() {
        return new YandexDiskMediaResponse("method", "href", true);
    }
}
