package com.pluxurydolo.yandexdisk.flow;

import com.pluxurydolo.yandexdisk.dto.request.DeleteFileRequest;
import com.pluxurydolo.yandexdisk.web.YandexDiskApiWebClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class DeleteFileFlowTests {

    @Mock
    private YandexDiskApiWebClient yandexDiskApiWebClient;

    @InjectMocks
    private DeleteFileFlow deleteFileFlow;

    @Test
    void testDeleteFile() {
        when(yandexDiskApiWebClient.deleteFile(anyString()))
            .thenReturn(Mono.just(""));

        Mono<String> result = deleteFileFlow.deleteFile(deleteFileRequest());

        create(result)
            .expectNext("")
            .verifyComplete();
    }

    @Test
    void testDeleteFileWhenExceptionOccurred() {
        when(yandexDiskApiWebClient.deleteFile(anyString()))
            .thenReturn(Mono.error(new RuntimeException()));

        Mono<String> result = deleteFileFlow.deleteFile(deleteFileRequest());

        create(result)
            .verifyErrorMatches(throwable -> throwable.getClass().equals(RuntimeException.class));
    }

    private static DeleteFileRequest deleteFileRequest() {
        return new DeleteFileRequest("path");
    }
}
