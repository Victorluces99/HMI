/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.prueba.hmipanelsubprojectcategory;

/**
 *
 * @author luis
 */
public enum HMICategoryDefinition {
  IMAGE("Images", "com/prueba/hmipanelsubproject/category/FolderBlue.png"),
    COMUNICATION("Communication", "com/prueba/hmipanelsubproject/category/FolderBlue.png"),
    NOTICE_MANAGEMENT("Notification Management", "com/prueba/hmipanelsubproject/category/FolderBlue.png"),
    RECIPE("Recipes", "com/prueba/hmipanelsubproject/category/FolderBlue.png"),
    HISTORIAL("Historial", "com/prueba/hmipanelsubproject/category/FolderBlue.png"),
    SCRIPTS("Scripts", "com/prueba/hmipanelsubproject/category/FolderBlue.png"),
    REPORT("Reports", "com/prueba/hmipanelsubproject/category/FolderBlue.png"),
    TEXT_GRAPHIC("Text and List of Charts", "com/prueba/hmipanelsubproject/category/FolderBlue.png"),
    ADMIN_USER("Runtime User Management", "com/prueba/hmipanelsubproject/category/FolderBlue.png"),
    CONFIG_PANEL("Operator Panel Configuration", "com/prueba/hmipanelsubproject/category/FolderBlue.png");

    private final String displayName;
    private final String iconPath;

    HMICategoryDefinition(String displayName, String iconPath) {
        this.displayName = displayName;
        this.iconPath = iconPath;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIconPath() {
        return iconPath;
    }

    public class HMIUtil {

    }
}
