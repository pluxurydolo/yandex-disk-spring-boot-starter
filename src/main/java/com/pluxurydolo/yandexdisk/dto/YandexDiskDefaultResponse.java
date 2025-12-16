package com.pluxurydolo.yandexdisk.dto;

public record YandexDiskDefaultResponse(
    String method,
    String href,
    boolean templated
) {
}
