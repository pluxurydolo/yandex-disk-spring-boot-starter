package com.pluxurydolo.yandexdisk.config;

import com.pluxurydolo.yandexdisk.wrapper.YandexDiskDownloadingClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.util.unit.DataSize;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Consumer;

@AutoConfiguration
public class YandexDiskAutoConfiguration {

    @Bean
    public YandexDiskDownloadingClient yandexDiskDownloadingClient() {
        int maxInMemorySize = (int) DataSize.ofMegabytes(64)
            .toBytes();

        Consumer<ClientCodecConfigurer> clientCodecConfigurer = codecs -> codecs.defaultCodecs()
            .maxInMemorySize(maxInMemorySize);

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
            .codecs(clientCodecConfigurer)
            .build();

        WebClient webClient = WebClient.builder()
            .exchangeStrategies(exchangeStrategies)
            .baseUrl("https://downloader.disk.yandex.ru")
            .build();

        return new YandexDiskDownloadingClient(webClient);
    }
}
