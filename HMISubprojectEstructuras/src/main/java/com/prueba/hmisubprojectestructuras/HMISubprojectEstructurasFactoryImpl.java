/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prueba.hmisubprojectestructuras;

import java.io.IOException;
import javax.tools.FileObject;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ProjectFactory.class)
public class HMISubprojectEstructurasFactoryImpl implements ProjectFactory {
    public static final String PROJECT_FILE = "structures.cfg";

    @Override
    public void saveProject(Project prjct) throws IOException, ClassCastException {
        //empty
    }

    @Override
    public boolean isProject(org.openide.filesystems.FileObject fo) {
        return fo.getFileObject(PROJECT_FILE) != null;
    }

    @Override
    public Project loadProject(org.openide.filesystems.FileObject fo, ProjectState ps) throws IOException {
        return isProject(fo) ? new HMISubprojectEstructurasImpl(fo, ps) : null;
    }
    
}
