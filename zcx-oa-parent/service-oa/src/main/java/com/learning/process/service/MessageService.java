package com.learning.process.service;

public interface MessageService {

    void pushPendingMessage(Long processId, Long userId, String taskId);

    public void pushProcessedMessage(Long processId, Long userId, Integer status);
}
