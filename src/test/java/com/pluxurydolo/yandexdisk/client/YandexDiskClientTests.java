package com.pluxurydolo.yandexdisk.client;

import com.pluxurydolo.yandexdisk.dto.request.DeleteFileRequest;
import com.pluxurydolo.yandexdisk.dto.request.DownloadFileRequest;
import com.pluxurydolo.yandexdisk.dto.request.UploadFileRequest;
import com.pluxurydolo.yandexdisk.flow.DeleteFileFlow;
import com.pluxurydolo.yandexdisk.flow.DownloadFileFlow;
import com.pluxurydolo.yandexdisk.flow.UploadFileFlow;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class YandexDiskClientTests {

    @Mock
    private UploadFileFlow uploadFileFlow;

    @Mock
    private DownloadFileFlow downloadFileFlow;

    @Mock
    private DeleteFileFlow deleteFileFlow;

    @InjectMocks
    private YandexDiskClient yandexDiskClient;

    @Test
    void testUploadFile() {
        when(uploadFileFlow.uploadFile(any()))
            .thenReturn(Mono.just(""));

        Mono<String> result = yandexDiskClient.uploadFile(uploadFileRequest());

        create(result)
            .expectNext("")
            .verifyComplete();
    }

    @Test
    void testUploadFileWhenExceptionOccurred() {
        when(uploadFileFlow.uploadFile(any()))
            .thenReturn(Mono.error(new RuntimeException()));

        Mono<String> result = yandexDiskClient.uploadFile(uploadFileRequest());

        create(result)
            .expectError(RuntimeException.class)
            .verify();
    }

    @Test
    void testDownloadFile() {
        byte[] bytes = {};
        when(downloadFileFlow.downloadFile(any()))
            .thenReturn(Mono.just(bytes));

        Mono<byte[]> result = yandexDiskClient.downloadFile(downloadFileRequest());

        create(result)
            .expectNext(bytes)
            .verifyComplete();
    }

    @Test
    void testDownloadFileWhenExceptionOccurred() {
        when(downloadFileFlow.downloadFile(any()))
            .thenReturn(Mono.error(new RuntimeException()));

        Mono<byte[]> result = yandexDiskClient.downloadFile(downloadFileRequest());

        create(result)
            .expectError(RuntimeException.class)
            .verify();
    }

    @Test
    void testDeleteFile() {
        when(deleteFileFlow.deleteFile(any()))
            .thenReturn(Mono.just(""));

        Mono<String> result = yandexDiskClient.deleteFile(deleteFileRequest());

        create(result)
            .expectNext("")
            .verifyComplete();
    }

    @Test
    void testDeleteFileWhenExceptionOccurred() {
        when(deleteFileFlow.deleteFile(any()))
            .thenReturn(Mono.error(new RuntimeException()));

        Mono<String> result = yandexDiskClient.deleteFile(deleteFileRequest());

        create(result)
            .expectError(RuntimeException.class)
            .verify();
    }

    private static UploadFileRequest uploadFileRequest() {
        return new UploadFileRequest("path", new byte[]{});
    }

    private static DownloadFileRequest downloadFileRequest() {
        return new DownloadFileRequest("path");
    }

    private static DeleteFileRequest deleteFileRequest() {
        return new DeleteFileRequest("path");
    }
}
