/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prueba.hmipanelsubprojectcategory.action;

import com.prueba.hmipanelsubprojectcategory.panel.HMICategoryCreateDisplayForm;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
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
public class HMICategoryRuntimeUserManagementAction extends AbstractAction{
    private final FileObject fileObject;

    public HMICategoryRuntimeUserManagementAction(FileObject fileObject) {
        super("Editar RunTime User Management");
        this.fileObject = fileObject;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Acción ejecutada sobre: " + fileObject.getNameExt());
    }   
}
