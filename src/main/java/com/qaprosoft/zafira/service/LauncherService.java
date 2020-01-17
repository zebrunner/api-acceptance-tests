package com.qaprosoft.zafira.service;

import java.util.List;

public interface LauncherService {

    List<Integer> getAll(String accessToken);

    int getById(String accessToken, int id);

    int create(String accessToken, int autoServerId, int accountTypeId);

    int update(String accessToken, int id, String valueToUpdate);

    void deleteById(String accessToken, int id);

    String getQueueItemUrl(String accessToken, int scmAccountId);

    String getBuildNumber(String accessToken, String queueItemUrl);

}