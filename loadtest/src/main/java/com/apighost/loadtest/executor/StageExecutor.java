package com.apighost.loadtest.executor;

import com.apighost.loadtest.parameter.StageExecuteParameter;
import java.util.concurrent.CompletableFuture;

public interface StageExecutor {

    void execute(StageExecuteParameter parameter, CompletableFuture<Boolean> stageCompletion);
}
