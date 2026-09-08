/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prueba.hmipanelsubprojectcategory.action;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.AbstractAction;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;

/**
 *
 * @author Victor
 */
public class HMICategoryDuplicateDisplayAction extends AbstractAction{
     private final FileObject bobFile;

    public HMICategoryDuplicateDisplayAction(FileObject bobFile) {
        super("Duplicar pantalla");
        this.bobFile = bobFile;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        RequestProcessor.getDefault().post(() ->Duplicate(this.bobFile));
    }
    
    private void Duplicate(FileObject nuevobobFile){
        FileObject parent = nuevobobFile.getParent();
        if (parent == null) {
            return;
        }
        //Nombre base sin extensión
        String baseName = nuevobobFile.getName();
        String ext = nuevobobFile.getExt();
        //Buscar un nombre libre: base - copia, base - copia2, ...
        String newName = baseName + "_copia";
        int n = 2;
        while (parent.getFileObject(newName, ext) != null) {
            newName = baseName + "_copia" + n++;
        }
        try {
            FileObject newFile = parent.createData(newName, ext);
            try (InputStream in = nuevobobFile.getInputStream();
                 OutputStream out = newFile.getOutputStream()) {
                in.transferTo(out);
            }
            System.out.println("[Duplicar] Creado: " + newFile.getNameExt());
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
