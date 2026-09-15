/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prueba.hmipanelsubproject;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Victor
 */
public class HMIPanelDataBaseFactory {

    private static final String createdb = null;

    //Variables::
    //Primero Variable de la estructura leida de planta
    /*
    1. Generar la base de datos en el proyecto actual.
    2. Generar las tablas asociadas
    3. Leer xml de planta de daniel.
    4. Determinar El Device (PLC), Item (Area de Memoria), Group (Tiempo de escaneo)
    y los PVRecords ( de proceso) del Control Module.
    5. Con el punto 4 listo, realizar las inserciones en cada tabla correspondiente.
     */
    private static final String SQL_CREATE_TABLE_DEVICES
            = "CREATE TABLE IF NOT EXISTS Devices("
            + "DeviceUuId TEXT NOT NULL PRIMARY KEY,"
            + "DriverName TEXT,"
            + "DeviceKey TEXT,"
            + "DeviceUrl TEXT,"
            + "DeviceName TEXT,"
            + "DeviceDescription TEXT,"
            + "DeviceEnable TEXT,"
            + "Md5 TEXT)";

    private static final String SQL_CREATE_TABLE_GROUPS
            = "CREATE TABLE IF NOT EXISTS Groups("
            + "GroupUuid TEXT NOT NULL PRIMARY KEY,"
            + "DeviceUuid TEXT,"
            + "GroupName TEXT,"
            + "GroupDescription TEXT,"
            + "GroupScantime TEXT,"
            + "GroupEnable TEXT,"
            + "Md5 TEXT)";

    private static final String SQL_CREATE_TABLE_ITEMS
            = "CREATE TABLE IF NOT EXISTS Items("
            + "ItemUuid TEXT NOT NULL PRIMARY KEY,"
            + "DeviceUuid TEXT,"
            + "GroupUuid TEXT,"
            + "ItemName TEXT,"
            + "ItemDescription TEXT,"
            + "ItemTag TEXT,"
            + "ItemEnable TEXT,"
            + "Md5 TEXT)";

    private static final String SQL_CREATE_TABLE_PVRECORDS
            = "CREATE TABLE IF NOT EXISTS PvRecords("
            + "PvUuId TEXT NOT NULL PRIMARY KEY,"
            + "PvName TEXT,"
            + "PvType TEXT,"
            + "PvId TEXT,"
            + "PvOffset TEXT,"
            + "PvDescriptor TEXT,"
            + "PvScanTime TEXT,"
            + "PvScanEnable TEXT,"
            + "PvWriteEnable TEXT,"
            + "PvDisplayLimitLow TEXT,"
            + "PvDisplayLimitHigh TEXT,"
            + "PvDisplayDescription TEXT,"
            + "PvDisplayFormat TEXT,"
            + "PvDisplayUnits TEXT,"
            + "PvControlLimitLow TEXT,"
            + "PvControlLimitHigh TEXT,"
            + "PvControlMinStep TEXT,"
            + "Md5 TEXT)";

    public static void createDB(String rutaCarpeta, String nombreArchivo) {
        // Asegurar la existencia de la carpeta
        File carpeta = new File(rutaCarpeta);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        // Construir la URL de JDBC con la ruta completa
        File archivoDB = new File(carpeta, nombreArchivo);
        String url = "jdbc:sqlite:" + archivoDB.getAbsolutePath();

        System.out.println("Creando/Conectando base de datos en: " + archivoDB.getAbsolutePath());

        // Ejecutar la creación de tablas dentro de una sola transacción
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            if (conn != null) {
                // Ejecutar las sentencias
                stmt.execute(SQL_CREATE_TABLE_DEVICES);
                stmt.execute(SQL_CREATE_TABLE_GROUPS);
                stmt.execute(SQL_CREATE_TABLE_ITEMS);
                stmt.execute(SQL_CREATE_TABLE_PVRECORDS);

                System.out.println("✅ Las 4 tablas fueron creadas exitosamente (vacías).");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al crear las tablas: " + e.getMessage());
        }
    }

}
