/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prueba.hmipanelsubprojectcategory.action;

import com.prueba.hmipanelsubprojectcategory.panel.HMICategoryCreateDisplayForm;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.netbeans.api.project.Project;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.filesystems.FileObject;

import org.openide.util.ContextAwareAction;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

public class HMICategoryCreateRecordAction extends AbstractAction implements ContextAwareAction {
    private final static String FILE_NAME_DISPLAY_EXT = "record";
 //   private final static String DISPLAY_TEMPLATE_PATH = 
    private Project project;

    public HMICategoryCreateRecordAction(Project project) {
        super("Crear Historial");
        this.project = project;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileObject targetFolder = project.getProjectDirectory();

        HMICategoryCreateDisplayForm cd = new HMICategoryCreateDisplayForm();

        DialogDescriptor descriptor = new DialogDescriptor(
                cd,
                "Crear Historial",
                true,
                new Object[0],
                null,
                DialogDescriptor.DEFAULT_ALIGN,
                null,
                null
        );

        DialogDisplayer.getDefault().notify(descriptor);

        if (cd.isConfirmed()) {
            String fileName = cd.getFileName();
            try {
                FileObject newFile = targetFolder.createData(fileName, FILE_NAME_DISPLAY_EXT);

//                try (InputStream in = getClass().getClassLoader()
//                        .getResourceAsStream(DISPLAY_TEMPLATE_PATH); OutputStream out = newFile.getOutputStream()) {
//                    if (in != null) {
//                        in.transferTo(out);
//                    }
//                }

//                JOptionPane.showMessageDialog(null, "Pantalla creada");
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
