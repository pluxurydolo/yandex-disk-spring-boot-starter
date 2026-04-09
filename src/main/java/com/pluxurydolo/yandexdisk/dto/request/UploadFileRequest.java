package com.pluxurydolo.yandexdisk.dto.request;

public record UploadFileRequest(
    String path,
    byte[] file
) {
}
