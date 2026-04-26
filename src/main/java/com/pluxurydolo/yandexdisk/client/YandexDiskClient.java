package com.pluxurydolo.yandexdisk.client;

import com.pluxurydolo.yandexdisk.dto.request.DeleteFileRequest;
import com.pluxurydolo.yandexdisk.dto.request.DownloadFileRequest;
import com.pluxurydolo.yandexdisk.dto.request.UploadFileRequest;
import com.pluxurydolo.yandexdisk.exception.YandexDiskDeleteFileException;
import com.pluxurydolo.yandexdisk.exception.YandexDiskDownloadFileException;
import com.pluxurydolo.yandexdisk.exception.YandexDiskUploadFileException;
import com.pluxurydolo.yandexdisk.flow.DeleteFileFlow;
import com.pluxurydolo.yandexdisk.flow.DownloadFileFlow;
import com.pluxurydolo.yandexdisk.flow.UploadFileFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class YandexDiskClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(YandexDiskClient.class);

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
        return uploadFileFlow.uploadFile(request)
            .doOnSuccess(_ -> LOGGER.info("egwf [yandex-disk-starter] Загрузка файла успешно завершена"))
            .onErrorResume(throwable -> {
                LOGGER.error("dveb [yandex-disk-starter] Произошла ошибка при загрузке файла");
                return Mono.error(new YandexDiskUploadFileException(throwable));
            });
    }

    public Mono<byte[]> downloadFile(DownloadFileRequest request) {
        return downloadFileFlow.downloadFile(request)
            .doOnSuccess(_ -> LOGGER.info("jfuq [yandex-disk-starter] Скачивание файла успешно завершено"))
            .onErrorResume(throwable -> {
                LOGGER.error("wifj [yandex-disk-starter] Произошла ошибка при скачивании файла");
                return Mono.error(new YandexDiskDownloadFileException(throwable));
            });
    }

    public Mono<String> deleteFile(DeleteFileRequest request) {
        return deleteFileFlow.deleteFile(request)
            .doOnSuccess(_ -> LOGGER.info("pyak [yandex-disk-starter] Удаление файла успешно завершено"))
            .onErrorResume(throwable -> {
                LOGGER.error("khzy [yandex-disk-starter] Произошла ошибка при удалении файла");
                return Mono.error(new YandexDiskDeleteFileException(throwable));
            });
    }
}
