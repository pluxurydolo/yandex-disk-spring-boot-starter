package com.pluxurydolo.yandexdisk.configuration;

import com.pluxurydolo.yandexdisk.properties.YandexDiskProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@EnableConfigurationProperties(YandexDiskProperties.class)
@Import({
    YandexDiskWebConfiguration.class,
    YandexDiskClientConfiguration.class,
    YandexDiskMediaFlowConfiguration.class
})
public class YandexDiskAutoConfiguration {
}
