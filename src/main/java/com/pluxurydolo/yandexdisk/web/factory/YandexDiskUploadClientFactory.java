package com.pluxurydolo.yandexdisk.web.factory;

import com.pluxurydolo.yandexdisk.web.YandexDiskUploadHttpClient;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

public class YandexDiskUploadClientFactory {
    public YandexDiskUploadHttpClient create(String uploadUrl) {
        WebClient webClient = WebClient.builder()
            .baseUrl(uploadUrl)
            .build();

        WebClientAdapter exchangeAdapter = WebClientAdapter.create(webClient);

        return HttpServiceProxyFactory.builderFor(exchangeAdapter)
            .build()
            .createClient(YandexDiskUploadHttpClient.class);
    }
}
