package com.apighost.scenario.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class BasePathHolder {
    private Path basePath;

    private BasePathHolder() {
        this.basePath = Paths.get(System.getProperty("user.home"));
    }

    private static class Holder {
        private static final BasePathHolder INSTANCE = new BasePathHolder();
    }

    public static BasePathHolder getInstance() {
        return Holder.INSTANCE;
    }

    public Path getBasePath() {
        return basePath;
    }

    public void setBasePath(Path basePath) {
        this.basePath = basePath;
    }
}
