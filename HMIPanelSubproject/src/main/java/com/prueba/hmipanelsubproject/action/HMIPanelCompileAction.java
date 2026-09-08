/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prueba.hmipanelsubproject.action;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.filesystems.FileObject;
import java.io.*;
import java.nio.file.*;
import java.util.zip.*;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

/**
 *
 * @author Victor
 */
public class HMIPanelCompileAction extends AbstractAction {

    private final FileObject panel;

    public HMIPanelCompileAction(FileObject panel) {
        super("Compilar Proyecto");
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CompilePanel(this.panel);
    }

    private void CompilePanel(FileObject panel) {
        File folderOrigen = FileUtil.toFile(panel);
        if (folderOrigen == null || !folderOrigen.isDirectory()) {
            StatusDisplayer.getDefault().setStatusText("No se pudo obtener la carpeta del panel.");
            return;
        }
        //Elegir destino
        File zipDestino = new FileChooserBuilder("ZipOutputKey")
                .setTitle("Guardar ZIP del panel como...")
                .setApproveText("Guardar")
                .setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivo ZIP (*.zip)", "zip"))
                .showSaveDialog();
        if (zipDestino == null) {
            return;
        }
        if (!zipDestino.getName().toLowerCase().endsWith(".zip")) {
            zipDestino = new File(zipDestino.getAbsolutePath() + ".zip");
        }
        try {
            ZipUtils.zipFolder(folderOrigen.toPath(), zipDestino.toPath());
            StatusDisplayer.getDefault().setStatusText("Panel comprimido: " + zipDestino.getName());
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public class ZipUtils {

        public static void zipFolder(Path sourceFolderPath, Path zipPath) throws IOException {
            try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()))) {
                Files.walk(sourceFolderPath)
                        .filter(path -> !Files.isDirectory(path)) // Solo procesar archivos
                        .forEach(path -> {
                            // Obtener la ruta relativa para mantener la estructura dentro del ZIP
                            String relativePath = sourceFolderPath.relativize(path).toString().replace("\\", "/");
                            ZipEntry zipEntry = new ZipEntry(relativePath);
                            try {
                                zos.putNextEntry(zipEntry);
                                Files.copy(path, zos);
                                zos.closeEntry();
                            } catch (IOException e) {
                                throw new UncheckedIOException(e);
                            }
                        });
            }
        }
    }

}
