/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prueba.hmipanelsubprojectcategory.nodes;

import java.util.ArrayList;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.filesystems.AbstractFileSystem.List;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

@NodeFactory.Registration(projectType = "com-prueba-hmipanelsubprojectcategory", position = 100)
public class HMICategoryDisplayNodeFactory implements NodeFactory {

    @Override
    public NodeList<?> createNodes(Project project) {
        FileObject projectDir = project.getProjectDirectory();

        // Solo mostrar archivos .bob si es la carpeta "image"
        if (!"image".equalsIgnoreCase(projectDir.getName())) {
            return NodeFactorySupport.fixedNodeList();
        }

        ArrayList<Node> nodes = new ArrayList<>();

        for (FileObject child : projectDir.getChildren()) {
            if (!child.isFolder() && "bob".equalsIgnoreCase(child.getExt())) {
                try {
                    DataObject dataObj = DataObject.find(child);
                    nodes.add(dataObj.getNodeDelegate());
                } catch (DataObjectNotFoundException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }

        return NodeFactorySupport.fixedNodeList(nodes.toArray(new Node[0]));
    }

    
}
