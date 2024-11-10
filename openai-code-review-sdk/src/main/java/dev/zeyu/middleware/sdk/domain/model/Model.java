package dev.zeyu.middleware.sdk.domain.model;

public enum Model {
    GPT_4O("gpt-4o","gpt-4o");
    private final String code;
    private final String info;

    Model(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
