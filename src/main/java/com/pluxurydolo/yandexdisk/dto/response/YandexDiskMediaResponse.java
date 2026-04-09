package com.pluxurydolo.yandexdisk.dto.response;

public record YandexDiskMediaResponse(
    String method,
    String href,
    boolean templated
) {
}
