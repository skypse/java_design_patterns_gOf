package com.gfb.designpatterns.creational.singleton;

/**
 * Padrão Singleton implementado via Enum.
 * De acordo com Joshua Bloch (Effective Java), o Singleton baseado em Enum é
 * a forma mais concisa e segura contra serialização e ataques de reflexão (Reflection API).
 */
public enum AppTheme {
    DARK_MODE("Dark Mode", "#1E1E2E", "#CDD6F4"),
    LIGHT_MODE("Light Mode", "#FFFFFF", "#1E1E2E");

    private final String themeName;
    private final String backgroundColor;
    private final String textColor;

    AppTheme(String themeName, String backgroundColor, String textColor) {
        this.themeName = themeName;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }

    public String getThemeName() {
        return themeName;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void applyTheme() {
        System.out.printf("  [Tema Ativo] %s (Fundo: %s, Texto: %s)%n", themeName, backgroundColor, textColor);
    }
}
