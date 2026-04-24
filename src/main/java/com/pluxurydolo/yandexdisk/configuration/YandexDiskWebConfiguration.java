package com.pluxurydolo.yandexdisk.configuration;

import com.pluxurydolo.yandexdisk.properties.YandexDiskProperties;
import com.pluxurydolo.yandexdisk.web.YandexDiskApiWebClient;
import com.pluxurydolo.yandexdisk.web.YandexDiskDownloaderWebClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YandexDiskWebConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public YandexDiskApiWebClient yandexDiskApiWebClient(YandexDiskProperties yandexDiskProperties) {
        return new YandexDiskApiWebClient(yandexDiskProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public YandexDiskDownloaderWebClient yandexDiskDownloaderWebClient() {
        return new YandexDiskDownloaderWebClient();
    }
}
