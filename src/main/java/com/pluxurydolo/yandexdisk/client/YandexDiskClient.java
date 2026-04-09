package com.pluxurydolo.yandexdisk.client;

import com.pluxurydolo.yandexdisk.dto.request.DeleteFileRequest;
import com.pluxurydolo.yandexdisk.dto.request.DownloadFileRequest;
import com.pluxurydolo.yandexdisk.dto.request.UploadFileRequest;
import com.pluxurydolo.yandexdisk.flow.DeleteFileFlow;
import com.pluxurydolo.yandexdisk.flow.DownloadFileFlow;
import com.pluxurydolo.yandexdisk.flow.UploadFileFlow;
import reactor.core.publisher.Mono;

public class YandexDiskClient {
    private final UploadFileFlow uploadFileFlow;
    private final DownloadFileFlow downloadFileFlow;
    private final DeleteFileFlow deleteFileFlow;

    public YandexDiskClient(
        UploadFileFlow uploadFileFlow,
        DownloadFileFlow downloadFileFlow,
        DeleteFileFlow deleteFileFlow
    ) {
        this.uploadFileFlow = uploadFileFlow;
        this.downloadFileFlow = downloadFileFlow;
        this.deleteFileFlow = deleteFileFlow;
    }

    public Mono<String> uploadFile(UploadFileRequest request) {
        return uploadFileFlow.uploadFile(request);
    }

    public Mono<byte[]> downloadFile(DownloadFileRequest request) {
        return downloadFileFlow.downloadFile(request);
    }

    public Mono<String> deleteFile(DeleteFileRequest request) {
        return deleteFileFlow.deleteFile(request);
    }
}
