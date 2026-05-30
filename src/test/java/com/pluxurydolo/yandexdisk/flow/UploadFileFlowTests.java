package com.pluxurydolo.yandexdisk.flow;

import com.pluxurydolo.yandexdisk.dto.request.UploadFileRequest;
import com.pluxurydolo.yandexdisk.dto.response.YandexDiskMediaResponse;
import com.pluxurydolo.yandexdisk.web.YandexDiskApiHttpClient;
import com.pluxurydolo.yandexdisk.web.factory.YandexDiskUploadClientFactory;
import com.pluxurydolo.yandexdisk.web.YandexDiskUploadHttpClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class UploadFileFlowTests {

    @Mock
    private YandexDiskApiHttpClient yandexDiskApiHttpClient;

    @Mock
    private YandexDiskUploadClientFactory yandexDiskUploadClientFactory;

    @Mock
    private YandexDiskUploadHttpClient yandexDiskUploadHttpClient;

    @InjectMocks
    private UploadFileFlow uploadFileFlow;

    @Test
    void testUploadFile() {
        when(yandexDiskApiHttpClient.getUploadLink(anyString(), anyBoolean()))
            .thenReturn(Mono.just(yandexDiskMediaResponse()));
        when(yandexDiskUploadClientFactory.create(anyString()))
            .thenReturn(yandexDiskUploadHttpClient);
        when(yandexDiskUploadHttpClient.upload(any()))
            .thenReturn(Mono.just(responseEntity()));

        Mono<String> result = uploadFileFlow.uploadFile(uploadFileRequest());

        create(result)
            .expectNext("path")
            .verifyComplete();
    }

    @Test
    void testUploadFileWhenExceptionOccurred() {
        when(yandexDiskApiHttpClient.getUploadLink(anyString(), anyBoolean()))
            .thenReturn(Mono.error(new RuntimeException()));

        Mono<String> result = uploadFileFlow.uploadFile(uploadFileRequest());

        create(result)
            .verifyErrorMatches(throwable -> throwable.getClass().equals(RuntimeException.class));
    }

    private static UploadFileRequest uploadFileRequest() {
        return new UploadFileRequest("path", new byte[]{});
    }

    private static YandexDiskMediaResponse yandexDiskMediaResponse() {
        return new YandexDiskMediaResponse("method", "href", true);
    }

    private static ResponseEntity<Void> responseEntity() {
        return ResponseEntity.ok().build();
    }
}
