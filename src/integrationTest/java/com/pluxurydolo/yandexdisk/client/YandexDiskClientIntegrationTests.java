package com.pluxurydolo.yandexdisk.client;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import ch.qos.logback.core.spi.AppenderAttachable;
import com.pluxurydolo.yandexdisk.base.AbstractIntegrationTests;
import com.pluxurydolo.yandexdisk.dto.request.DeleteFileRequest;
import com.pluxurydolo.yandexdisk.dto.request.DownloadFileRequest;
import com.pluxurydolo.yandexdisk.dto.request.UploadFileRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.slf4j.LoggerFactory.getLogger;

class YandexDiskClientIntegrationTests extends AbstractIntegrationTests {
    private static final AppenderAttachable<ILoggingEvent> LOGGER =
        (Logger) getLogger(YandexDiskClient.class);

    @Autowired
    private YandexDiskClient yandexDiskClient;

    @Test
    void testUploadFile() {
        List<ILoggingEvent> logs = listAppender().list;

        UploadFileRequest request = new UploadFileRequest("path", bytes());
        yandexDiskClient.uploadFile(request)
            .subscribe();

        await().atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> {
                assertThat(logs)
                    .hasSize(1);

                assertThat(logs.getFirst().getFormattedMessage())
                    .isEqualTo("egwf [yandex-disk-starter] Загрузка файла успешно завершена");
            });
    }

    @Test
    void testDownloadFile() {
        List<ILoggingEvent> logs = listAppender().list;

        DownloadFileRequest request = new DownloadFileRequest("path");
        yandexDiskClient.downloadFile(request)
            .subscribe();

        await().atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> {
                assertThat(logs)
                    .hasSize(1);

                assertThat(logs.getFirst().getFormattedMessage())
                    .isEqualTo("jfuq [yandex-disk-starter] Скачивание файла успешно завершено");
            });
    }

    @Test
    void testDeleteFile() {
        List<ILoggingEvent> logs = listAppender().list;

        DeleteFileRequest request = new DeleteFileRequest("path");
        yandexDiskClient.deleteFile(request)
            .subscribe();

        await().atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> {
                assertThat(logs)
                    .hasSize(1);

                assertThat(logs.getFirst().getFormattedMessage())
                    .isEqualTo("pyak [yandex-disk-starter] Удаление файла успешно завершено");
            });
    }

    private static ListAppender<ILoggingEvent> listAppender() {
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        LOGGER.addAppender(listAppender);
        return listAppender;
    }

    private static byte[] bytes() {
        return new byte[]{1, 2, 3};
    }
}
