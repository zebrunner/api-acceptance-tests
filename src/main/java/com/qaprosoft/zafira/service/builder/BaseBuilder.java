package com.qaprosoft.zafira.service.builder;

import com.qaprosoft.zafira.client.ZafiraClient;
import com.qaprosoft.zafira.models.dto.AbstractType;
import com.qaprosoft.zafira.util.http.HttpClient;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Optional;
import java.util.function.Function;

public abstract class BaseBuilder {

    protected static ZafiraClient client() {
        return ServiceSingleton.INSTANCE.getClient();
    }

    protected static String generateRandomString() {
        return RandomStringUtils.randomAlphabetic(15);
    }

    protected static <T extends AbstractType> Optional<T> callItem(Function<ZafiraClient, HttpClient.Response<T>> itemGetter) {
        T result = null;
        HttpClient.Response<T> response =  itemGetter.apply(client());
        if(response.getObject() != null) {
            result = response.getObject();
        }
        return Optional.ofNullable(result);
    }

}
