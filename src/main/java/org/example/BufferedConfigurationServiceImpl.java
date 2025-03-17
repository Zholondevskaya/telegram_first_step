package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BufferedConfigurationServiceImpl implements ConfigurationService {

    private final Map<String, String> parameters;

    public BufferedConfigurationServiceImpl() {
        this.parameters = getParameters(System.getProperty("telegram.config.location"));
    }

    @Override
    public String getConfigurationProperty(String propertyName) {
        return parameters.get(propertyName);
    }

    @SuppressWarnings("SameParameterValue")
    private Map<String, String> getParameters(String filePath) {
        File configFile = new File(filePath);
        try {
            String data = readFromFile(configFile);
            String[] lines = data.split("\n");
            Map<String, String> parameters = new HashMap<>();
            for (String line : lines) {
                String key;
                String value;
                String[] pair = line.split("=");
                key = pair[0];
                value = pair[1];
                parameters.put(key, value);
            }
            return parameters;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String readFromFile(File file) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

}
