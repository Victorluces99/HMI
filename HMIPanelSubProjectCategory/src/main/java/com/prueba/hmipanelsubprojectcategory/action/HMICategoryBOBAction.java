/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prueba.hmipanelsubprojectcategory.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.filesystems.FileObject;



public class HMICategoryBOBAction extends AbstractAction {

    private final FileObject fileObject;

    public HMICategoryBOBAction(FileObject fileObject) {
        super("Editar en Phoebus");
        this.fileObject = fileObject;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Acción ejecutada sobre: " + fileObject.getNameExt());
    }
}
