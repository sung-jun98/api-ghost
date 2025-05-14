package com.apighost.util.file;

import java.nio.file.Path;
import java.nio.file.Paths;

public class BasePathHolder {

    private static final BasePathHolder INSTANCE = new BasePathHolder();

    private Path basePath;

    private BasePathHolder() {
        this.basePath = Paths.get(System.getProperty("user.home"));
    }

    public static BasePathHolder getInstance() {
        return INSTANCE;
    }

    public Path getBasePath() {
        return basePath;
    }

    public void setBasePath(Path basePath) {
        this.basePath = basePath;
    }
}
