package org.example;

public class ConfigurationServiceImpl implements ConfigurationService {
    private final String tokenTg;

    public ConfigurationServiceImpl(String tokenTg) {
        this.tokenTg = tokenTg;
    }

    @Override
    public String getToken() {
        return tokenTg;
    }

}
