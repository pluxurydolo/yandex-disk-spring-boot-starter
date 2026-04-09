package com.pluxurydolo.yandexdisk.config;

import com.pluxurydolo.yandexdisk.properties.YandexDiskProperties;
import com.pluxurydolo.yandexdisk.web.YandexDiskApiWebClient;
import com.pluxurydolo.yandexdisk.web.YandexDiskDownloaderWebClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YandexDiskWebConfiguration {

    @Bean
    public YandexDiskApiWebClient yandexDiskApiWebClient(YandexDiskProperties yandexDiskProperties) {
        return new YandexDiskApiWebClient(yandexDiskProperties);
    }

    @Bean
    public YandexDiskDownloaderWebClient yandexDiskDownloaderWebClient() {
        return new YandexDiskDownloaderWebClient();
    }
}
