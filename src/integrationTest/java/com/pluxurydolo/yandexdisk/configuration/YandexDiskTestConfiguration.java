package com.pluxurydolo.yandexdisk.configuration;

import com.pluxurydolo.yandexdisk.dto.response.YandexDiskMediaResponse;
import com.pluxurydolo.yandexdisk.web.YandexDiskApiWebClient;
import com.pluxurydolo.yandexdisk.web.YandexDiskDownloaderWebClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
public class YandexDiskTestConfiguration {

    @Bean
    public YandexDiskApiWebClient yandexDiskApiWebClient() {
        YandexDiskApiWebClient mock = mock(YandexDiskApiWebClient.class);

        when(mock.getUploadLink(anyString()))
            .thenReturn(Mono.just(yandexDiskMediaResponse()));
        when(mock.uploadFile(any(), any()))
            .thenReturn(Mono.just(""));
        when(mock.getDownloadLink(anyString()))
            .thenReturn(Mono.just(yandexDiskMediaResponse()));
        when(mock.deleteFile(anyString()))
            .thenReturn(Mono.just(""));

        return mock;
    }

    @Bean
    public YandexDiskDownloaderWebClient yandexDiskDownloaderWebClient() {
        YandexDiskDownloaderWebClient client = mock(YandexDiskDownloaderWebClient.class);
        URI uri = mock(URI.class);

        when(client.getFileLocation(any()))
            .thenReturn(Mono.just(uri));
        when(client.downloadFile(any()))
            .thenReturn(Mono.just(bytes()));

        return client;
    }

    private static YandexDiskMediaResponse yandexDiskMediaResponse() {
        return new YandexDiskMediaResponse("method", "href", true);
    }

    private static byte[] bytes() {
        return new byte[]{1, 2, 3};
    }
}
