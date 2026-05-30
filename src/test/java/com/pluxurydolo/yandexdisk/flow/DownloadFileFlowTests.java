package com.pluxurydolo.yandexdisk.flow;

import com.pluxurydolo.yandexdisk.dto.request.DownloadFileRequest;
import com.pluxurydolo.yandexdisk.dto.response.YandexDiskMediaResponse;
import com.pluxurydolo.yandexdisk.web.YandexDiskApiHttpClient;
import com.pluxurydolo.yandexdisk.web.factory.YandexDiskDownloaderClientFactory;
import com.pluxurydolo.yandexdisk.web.YandexDiskDownloaderHttpClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.copyOf;
import static org.springframework.util.MultiValueMap.fromSingleValue;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class DownloadFileFlowTests {

    @Mock
    private YandexDiskApiHttpClient yandexDiskApiHttpClient;

    @Mock
    private YandexDiskDownloaderClientFactory yandexDiskDownloaderClientFactory;

    @Mock
    private YandexDiskDownloaderHttpClient yandexDiskDownloaderHttpClient;

    @InjectMocks
    private DownloadFileFlow downloadFileFlow;

    @Test
    void testDownloadFile() {
        byte[] bytes = {};
        when(yandexDiskApiHttpClient.getDownloadLink(anyString()))
            .thenReturn(Mono.just(yandexDiskMediaResponse()));
        when(yandexDiskDownloaderClientFactory.create())
            .thenReturn(yandexDiskDownloaderHttpClient);
        when(yandexDiskDownloaderHttpClient.getFileLocation(any()))
            .thenReturn(Mono.just(responseEntity()));
        when(yandexDiskDownloaderHttpClient.downloadFile(any()))
            .thenReturn(Mono.just(bytes));

        Mono<byte[]> result = downloadFileFlow.downloadFile(downloadFileRequest());

        create(result)
            .expectNext(bytes)
            .verifyComplete();
    }

    @Test
    void testDownloadFileWhenExceptionOccurred() {
        when(yandexDiskApiHttpClient.getDownloadLink(anyString()))
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

    private static ResponseEntity<Void> responseEntity() {
        HttpHeaders headers = copyOf(fromSingleValue(Map.of("Location", "location")));

        return ResponseEntity.ok()
            .headers(headers)
            .build();
    }
}
