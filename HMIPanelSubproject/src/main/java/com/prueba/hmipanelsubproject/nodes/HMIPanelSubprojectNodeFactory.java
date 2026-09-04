/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prueba.hmipanelsubproject.nodes;

import com.prueba.hmipanelsubproject.HMIPanelSubprojectImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.filesystems.AbstractFileSystem.List;
import org.openide.filesystems.FileObject;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;


@NodeFactory.Registration(projectType = "com-prueba-hmiproject", position = 10)
public class HMIPanelSubprojectNodeFactory implements NodeFactory {

    @Override
    public NodeList<?> createNodes(Project project) {
//        FileObject panelFolder = project.getProjectDirectory().getFileObject("panel");
//
//        if (panelFolder == null) {
//            return NodeFactorySupport.fixedNodeList();
//        }
//        try {
//            Project panelProject = ProjectManager.getDefault().findProject(panelFolder);
//            if (panelProject == null) {
//                return NodeFactorySupport.fixedNodeList();
//            }
//            Node panelNode = panelProject.getLookup()
//                    .lookup(LogicalViewProvider.class)
//                    .createLogicalView();
//            return NodeFactorySupport.fixedNodeList(panelNode);
//        } catch (IOException | IllegalArgumentException ex) {
//            Exceptions.printStackTrace(ex);
//            return NodeFactorySupport.fixedNodeList();
//        }

        ArrayList<Node> nodes = new ArrayList<>();

        FileObject dir = project.getProjectDirectory();

        for (Project panelProject : getAllSubProject(dir)) {

            System.out.println("Project: "+panelProject);
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
                if (p instanceof HMIPanelSubprojectImpl) {
                    result.add((HMIPanelSubprojectImpl) p);
                }
            } catch (IOException | IllegalArgumentException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        return result;

    }

}
