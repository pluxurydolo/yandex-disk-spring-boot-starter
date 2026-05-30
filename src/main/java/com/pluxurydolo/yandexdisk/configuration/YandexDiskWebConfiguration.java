package com.pluxurydolo.yandexdisk.configuration;

import com.pluxurydolo.yandexdisk.properties.YandexDiskProperties;
import com.pluxurydolo.yandexdisk.web.YandexDiskApiHttpClient;
import com.pluxurydolo.yandexdisk.web.factory.YandexDiskDownloaderClientFactory;
import com.pluxurydolo.yandexdisk.web.factory.YandexDiskUploadClientFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Configuration
public class YandexDiskWebConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public YandexDiskApiHttpClient yandexDiskApiHttpClient(YandexDiskProperties yandexDiskProperties) {
        WebClient webClient = WebClient.builder()
            .baseUrl("https://cloud-api.yandex.net")
            .defaultHeader(AUTHORIZATION, authorizationHeader(yandexDiskProperties))
            .build();

        WebClientAdapter exchangeAdapter = WebClientAdapter.create(webClient);

        return HttpServiceProxyFactory.builderFor(exchangeAdapter)
            .build()
            .createClient(YandexDiskApiHttpClient.class);
    }

    @Bean
    @ConditionalOnMissingBean
    public YandexDiskDownloaderClientFactory yandexDiskDownloaderClientFactory() {
        return new YandexDiskDownloaderClientFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public YandexDiskUploadClientFactory yandexDiskUploadClientFactory() {
        return new YandexDiskUploadClientFactory();
    }

    private static String authorizationHeader(YandexDiskProperties yandexDiskProperties) {
        return "OAuth " + yandexDiskProperties.token();
    }
}
