/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prueba.hmipanelsubprojectcategory.action;

import com.prueba.hmipanelsubprojectcategory.panel.HMICategoryCreateDisplayForm;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.OutputStream;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import org.netbeans.api.project.Project;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.util.ContextAwareAction;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author Victor
 */
public class HMICategoryCreateDisplayAction extends AbstractAction implements ContextAwareAction {

    private final static String FILE_NAME_DISPLAY_EXT = "bob";
    private Project project;

    public HMICategoryCreateDisplayAction(Project project) {
        super("Crear pantalla");
        this.project = project;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileObject targetFolder = project.getProjectDirectory();

        HMICategoryCreateDisplayForm cd = new HMICategoryCreateDisplayForm();

        cd.setVisible(true);

        DialogDescriptor descriptor = new DialogDescriptor(
                cd,
                "Crear Display", // Título de la ventana
                true, // Ventana modal
                new Object[0], // Sin botones por defecto de NetBeans
                null,
                DialogDescriptor.DEFAULT_ALIGN,
                null,
                null
        );

        DialogDisplayer.getDefault().notify(descriptor);
        String fileName = cd.getFileName();
        if (cd.isConfirmed()) {
            if (fileName.equals(null)) {
                fileName = "display";
            }
            
            OutputStream output = new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    
                }
            };
            
            FileObject newFile = targetFolder.getFileObject(fileName, FILE_NAME_DISPLAY_EXT);
            
            
            try {
                newFile = targetFolder.createData(fileName, FILE_NAME_DISPLAY_EXT);
                JOptionPane.showMessageDialog(null, "Pantalla creada");

            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return null;
    }

}
