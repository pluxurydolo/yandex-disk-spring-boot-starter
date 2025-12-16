package com.pluxurydolo.yandexdisk.config;

import com.pluxurydolo.yandexdisk.client.YandexDiskClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.util.unit.DataSize;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Consumer;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@AutoConfiguration
@ConditionalOnProperty(
    prefix = "yandex.disk",
    name = "token"
)
public class YandexDiskAutoConfiguration {
    private final String yandexDiskToken;

    public YandexDiskAutoConfiguration(@Value("${yandex.disk.token}") String yandexDiskToken) {
        this.yandexDiskToken = yandexDiskToken;
    }

    @Bean
    public YandexDiskClient yandexDiskClient(WebClient yandexDiskRestClient, WebClient yandexDiskDownloadingClient) {
        return new YandexDiskClient(yandexDiskRestClient, yandexDiskDownloadingClient);
    }

    @Bean
    public WebClient yandexDiskRestClient() {
        return WebClient.builder()
            .defaultHeader(AUTHORIZATION, yandexDiskToken)
            .baseUrl("https://cloud-api.yandex.net")
            .build();
    }

    @Bean
    public WebClient yandexDiskDownloadingClient() {
        int maxInMemorySize = (int) DataSize.ofMegabytes(64)
            .toBytes();

        Consumer<ClientCodecConfigurer> clientCodecConfigurer = codecs -> codecs.defaultCodecs()
            .maxInMemorySize(maxInMemorySize);

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
            .codecs(clientCodecConfigurer)
            .build();

        return WebClient.builder()
            .exchangeStrategies(exchangeStrategies)
            .baseUrl("https://downloader.disk.yandex.ru")
            .build();
    }
}
