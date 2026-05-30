package com.pluxurydolo.yandexdisk.configuration;

import com.pluxurydolo.yandexdisk.dto.response.YandexDiskMediaResponse;
import com.pluxurydolo.yandexdisk.web.YandexDiskApiHttpClient;
import com.pluxurydolo.yandexdisk.web.YandexDiskDownloaderHttpClient;
import com.pluxurydolo.yandexdisk.web.YandexDiskUploadHttpClient;
import com.pluxurydolo.yandexdisk.web.factory.YandexDiskDownloaderClientFactory;
import com.pluxurydolo.yandexdisk.web.factory.YandexDiskUploadClientFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
public class YandexDiskTestConfiguration {

    @Bean
    public YandexDiskApiHttpClient yandexDiskApiHttpClient() {
        YandexDiskApiHttpClient mock = mock(YandexDiskApiHttpClient.class);

        when(mock.getUploadLink(anyString(), anyBoolean()))
            .thenReturn(Mono.just(yandexDiskMediaResponse()));
        when(mock.getDownloadLink(anyString()))
            .thenReturn(Mono.just(yandexDiskMediaResponse()));
        when(mock.deleteFile(anyString(), anyBoolean()))
            .thenReturn(Mono.just(responseEntity()));

        return mock;
    }

    @Bean
    public YandexDiskDownloaderClientFactory yandexDiskDownloaderClientFactory() {
        YandexDiskDownloaderClientFactory factory = mock(YandexDiskDownloaderClientFactory.class);
        YandexDiskDownloaderHttpClient client = mock(YandexDiskDownloaderHttpClient.class);

        when(factory.create())
            .thenReturn(client);
        when(client.getFileLocation(any()))
            .thenReturn(Mono.just(responseEntity()));
        when(client.downloadFile(any()))
            .thenReturn(Mono.just(bytes()));

        return factory;
    }

    @Bean
    public YandexDiskUploadClientFactory yandexDiskUploadClientFactory() {
        YandexDiskUploadClientFactory factory = mock(YandexDiskUploadClientFactory.class);
        YandexDiskUploadHttpClient client = mock(YandexDiskUploadHttpClient.class);

        when(factory.create(anyString()))
            .thenReturn(client);
        when(client.upload(any()))
            .thenReturn(Mono.just(responseEntity()));

        return factory;
    }

    private static byte[] bytes() {
        return new byte[]{1, 2, 3};
    }

    private static YandexDiskMediaResponse yandexDiskMediaResponse() {
        return new YandexDiskMediaResponse("method", "href", true);
    }

    private static ResponseEntity<Void> responseEntity() {
        return ResponseEntity.ok().build();
    }
}
