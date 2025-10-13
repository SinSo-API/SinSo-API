package com.vishalrashmika.sinso.api.Search;

/**
 * Enum representing language options for search filtering
 */
public enum SearchLanguage {
    ENGLISH("english"),      // Only English content
    SINHALA("sinhala"),      // Only Sinhala content
    BOTH("both");            // Both languages

    private final String value;

    SearchLanguage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SearchLanguage fromString(String value) {
        for (SearchLanguage lang : SearchLanguage.values()) {
            if (lang.value.equalsIgnoreCase(value)) {
                return lang;
            }
        }
        return BOTH; // Default fallback
    }
}
