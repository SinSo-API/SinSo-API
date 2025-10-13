package com.vishalrashmika.sinso.api.Search;

/**
 * Enum representing different types of search matches
 */
public enum SearchMatchType {
    CONTAINS("contains"),     // Default: partial match using LIKE
    EXACT("exact"),          // Exact match using equals
    STARTS_WITH("starts_with"); // Starts with match

    private final String value;

    SearchMatchType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SearchMatchType fromString(String value) {
        for (SearchMatchType type : SearchMatchType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return CONTAINS; // Default fallback
    }
}
