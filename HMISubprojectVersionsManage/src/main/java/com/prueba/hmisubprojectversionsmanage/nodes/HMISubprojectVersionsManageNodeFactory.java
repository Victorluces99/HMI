/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prueba.hmisubprojectversionsmanage.nodes;

import com.prueba.hmisubprojectversionsmanage.HMISubprojectVersionsManageImpl;
import java.io.IOException;
import java.util.ArrayList;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.filesystems.FileObject;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

@NodeFactory.Registration(projectType = "com-prueba-hmiproject", position = 110)
public class HMISubprojectVersionsManageNodeFactory implements NodeFactory{

    @Override
    public NodeList<?> createNodes(Project project) {
         ArrayList<Node> nodes = new ArrayList<>();

        FileObject dir = project.getProjectDirectory();

        for (Project panelProject : getAllSubProject(dir)) {

            System.out.println("Project: " + panelProject);
            LogicalViewProvider lvp = panelProject.getLookup().lookup(LogicalViewProvider.class);
            if (lvp != null) {
                nodes.add(lvp.createLogicalView());
            }
        }

        return NodeFactorySupport.fixedNodeList(nodes.toArray(new Node[0]));
    }
    
    private ArrayList<Project> getAllSubProject(FileObject dir) {
        ArrayList<Project> result = new ArrayList<>();

        for (FileObject child : dir.getChildren()) {
            if (!child.isFolder()) {
                continue;
            }
            try {
                Project p = ProjectManager.getDefault().findProject(child);
                if (p instanceof HMISubprojectVersionsManageImpl) {
                    result.add((HMISubprojectVersionsManageImpl) p);
                }
            } catch (IOException | IllegalArgumentException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        return result;

    }
    
}
