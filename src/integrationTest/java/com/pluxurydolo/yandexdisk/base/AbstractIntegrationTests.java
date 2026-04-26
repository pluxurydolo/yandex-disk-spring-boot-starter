package com.pluxurydolo.yandexdisk.base;

import com.pluxurydolo.yandexdisk.TestApplication;
import com.pluxurydolo.yandexdisk.configuration.YandexDiskTestConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest(classes = {
    TestApplication.class,
    YandexDiskTestConfiguration.class
})
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public abstract class AbstractIntegrationTests {
}
