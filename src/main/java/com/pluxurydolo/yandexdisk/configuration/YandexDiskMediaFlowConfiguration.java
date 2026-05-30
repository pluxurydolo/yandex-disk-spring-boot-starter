package com.pluxurydolo.yandexdisk.configuration;

import com.pluxurydolo.yandexdisk.flow.DeleteFileFlow;
import com.pluxurydolo.yandexdisk.flow.DownloadFileFlow;
import com.pluxurydolo.yandexdisk.flow.UploadFileFlow;
import com.pluxurydolo.yandexdisk.web.YandexDiskApiHttpClient;
import com.pluxurydolo.yandexdisk.web.factory.YandexDiskDownloaderClientFactory;
import com.pluxurydolo.yandexdisk.web.factory.YandexDiskUploadClientFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YandexDiskMediaFlowConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public UploadFileFlow uploadFileFlow(
        YandexDiskApiHttpClient yandexDiskApiHttpClient,
        YandexDiskUploadClientFactory yandexDiskUploadClientFactory
    ) {
        return new UploadFileFlow(yandexDiskApiHttpClient, yandexDiskUploadClientFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public DownloadFileFlow downloadFileFlow(
        YandexDiskApiHttpClient yandexDiskApiHttpClient,
        YandexDiskDownloaderClientFactory yandexDiskDownloaderClientFactory
    ) {
        return new DownloadFileFlow(yandexDiskApiHttpClient, yandexDiskDownloaderClientFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public DeleteFileFlow deleteFileFlow(YandexDiskApiHttpClient yandexDiskApiHttpClient) {
        return new DeleteFileFlow(yandexDiskApiHttpClient);
    }
}
