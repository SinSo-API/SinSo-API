package com.vishalrashmika.sinso.api.Search;

/**
 * Enum representing sort order options
 */
public enum SearchSortOrder {
    ASC("asc"),
    DESC("desc");

    private final String value;

    SearchSortOrder(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SearchSortOrder fromString(String value) {
        for (SearchSortOrder order : SearchSortOrder.values()) {
            if (order.value.equalsIgnoreCase(value)) {
                return order;
            }
        }
        return ASC; // Default fallback
    }
}
