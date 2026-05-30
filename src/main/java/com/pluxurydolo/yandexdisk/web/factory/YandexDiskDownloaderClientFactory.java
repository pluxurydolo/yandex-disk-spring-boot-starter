package com.pluxurydolo.yandexdisk.web.factory;

import com.pluxurydolo.yandexdisk.web.YandexDiskDownloaderHttpClient;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.util.unit.DataSize;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.function.Consumer;

public class YandexDiskDownloaderClientFactory {
    public YandexDiskDownloaderHttpClient create() {
        int maxSize = (int) DataSize.ofMegabytes(64).toBytes();

        Consumer<ClientCodecConfigurer> codec = codecConfigurer -> codecConfigurer
            .defaultCodecs()
            .maxInMemorySize(maxSize);

        WebClient webClient = WebClient.builder()
            .codecs(codec)
            .build();

        WebClientAdapter exchangeAdapter = WebClientAdapter.create(webClient);

        return HttpServiceProxyFactory.builderFor(exchangeAdapter)
            .build()
            .createClient(YandexDiskDownloaderHttpClient.class);
    }
}
