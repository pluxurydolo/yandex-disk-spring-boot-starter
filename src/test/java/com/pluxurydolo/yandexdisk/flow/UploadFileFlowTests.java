package com.pluxurydolo.yandexdisk.flow;

import com.pluxurydolo.yandexdisk.dto.request.UploadFileRequest;
import com.pluxurydolo.yandexdisk.dto.response.YandexDiskMediaResponse;
import com.pluxurydolo.yandexdisk.web.YandexDiskApiWebClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class UploadFileFlowTests {

    @Mock
    private YandexDiskApiWebClient yandexDiskApiWebClient;

    @InjectMocks
    private UploadFileFlow uploadFileFlow;

    @Test
    void testUploadFile() {
        when(yandexDiskApiWebClient.getUploadLink(anyString()))
            .thenReturn(Mono.just(yandexDiskMediaResponse()));
        when(yandexDiskApiWebClient.uploadFile(any(), any()))
            .thenReturn(Mono.just(""));

        Mono<String> result = uploadFileFlow.uploadFile(uploadFileRequest());

        create(result)
            .expectNext("")
            .verifyComplete();
    }

    @Test
    void testUploadFileWhenExceptionOccurred() {
        when(yandexDiskApiWebClient.getUploadLink(anyString()))
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
}
