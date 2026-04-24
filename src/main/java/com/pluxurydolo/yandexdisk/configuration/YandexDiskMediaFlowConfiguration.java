package com.pluxurydolo.yandexdisk.configuration;

import com.pluxurydolo.yandexdisk.flow.DeleteFileFlow;
import com.pluxurydolo.yandexdisk.flow.DownloadFileFlow;
import com.pluxurydolo.yandexdisk.flow.UploadFileFlow;
import com.pluxurydolo.yandexdisk.web.YandexDiskApiWebClient;
import com.pluxurydolo.yandexdisk.web.YandexDiskDownloaderWebClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YandexDiskMediaFlowConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public UploadFileFlow uploadFileFlow(YandexDiskApiWebClient yandexDiskApiWebClient) {
        return new UploadFileFlow(yandexDiskApiWebClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public DownloadFileFlow downloadFileFlow(
        YandexDiskApiWebClient yandexDiskApiWebClient,
        YandexDiskDownloaderWebClient yandexDiskDownloaderWebClient
    ) {
        return new DownloadFileFlow(yandexDiskApiWebClient, yandexDiskDownloaderWebClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public DeleteFileFlow deleteFileFlow(YandexDiskApiWebClient yandexDiskApiWebClient) {
        return new DeleteFileFlow(yandexDiskApiWebClient);
    }
}
