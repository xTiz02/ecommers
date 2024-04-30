package org.prd.ecommerce.config.util.enums;

public enum FolderName {
    CATEGORY("categories"),
    PRODUCT("products"),
    USER("users");

    private String folderName;

    FolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }
}
