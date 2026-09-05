package com.gfb.designpatterns.creational.singleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Padrão Singleton implementado através do padrão 'Bill Pugh' / Lazy Initialization Holder.
 * Garante thread-safety sem a necessidade de sincronização explícita (synchronized),
 * pois o carregamento da classe interna ocorre de forma atômica pela JVM.
 */
public class ConfigurationManager {

    private final Map<String, String> settings;

    private ConfigurationManager() {
        settings = new HashMap<>();
        // Configurações padrão da aplicação
        settings.put("app.name", "E-Commerce Pattern System");
        settings.put("app.version", "1.0.0");
        settings.put("app.environment", "production");
        settings.put("currency.default", "BRL");
    }

    private static class InstanceHolder {
        private static final ConfigurationManager INSTANCE = new ConfigurationManager();
    }

    public static ConfigurationManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public String getProperty(String key) {
        return settings.get(key);
    }

    public void setProperty(String key, String value) {
        settings.put(key, value);
    }

    public Map<String, String> getAllSettings() {
        return Map.copyOf(settings);
    }
}
