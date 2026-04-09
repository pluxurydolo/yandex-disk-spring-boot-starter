package com.pluxurydolo.yandexdisk.config;

import com.pluxurydolo.yandexdisk.client.YandexDiskClient;
import com.pluxurydolo.yandexdisk.flow.DeleteFileFlow;
import com.pluxurydolo.yandexdisk.flow.DownloadFileFlow;
import com.pluxurydolo.yandexdisk.flow.UploadFileFlow;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YandexDiskClientConfiguration {

    @Bean
    public YandexDiskClient yandexDiskClient(
        UploadFileFlow uploadFileFlow,
        DownloadFileFlow downloadFileFlow,
        DeleteFileFlow deleteFileFlow
    ) {
        return new YandexDiskClient(uploadFileFlow, downloadFileFlow, deleteFileFlow);
    }
}
