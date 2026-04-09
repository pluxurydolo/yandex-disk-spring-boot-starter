package com.pluxurydolo.yandexdisk.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "yandex.disk")
public record YandexDiskProperties(String token) {
}
