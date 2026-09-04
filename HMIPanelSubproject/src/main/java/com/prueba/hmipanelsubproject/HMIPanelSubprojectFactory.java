/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prueba.hmipanelsubproject;

import java.io.IOException;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author luis
 */
@ServiceProvider(service = ProjectFactory.class)
public class HMIPanelSubprojectFactory implements ProjectFactory {

    public static final String PROJECT_FILE = "panel.cfg";

    @Override
    public boolean isProject(FileObject fo) {
        return fo.getFileObject(PROJECT_FILE) != null;
    }

    @Override
    public Project loadProject(FileObject fo, ProjectState ps) throws IOException {
        return isProject(fo) ? new HMIPanelSubprojectImpl(fo, ps) : null;
    }

    @Override
    public void saveProject(Project prjct) throws IOException, ClassCastException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
