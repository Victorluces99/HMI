/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prueba.hmiproject.impl;

import com.prueba.hmipanelsubproject.HMIPanelSubprojectImpl;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.spi.project.SubprojectProvider;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;


public class HMISubProjectProviderImpl implements SubprojectProvider {

    public static final String PANEL_SUBPROJECT_DIRECTORY = "panel";
    private final Project project;

    public HMISubProjectProviderImpl(Project project) {
        this.project = project;
    }

    @Override
    public Set<? extends Project> getSubprojects() {
        return loadProjects(project.getProjectDirectory());
    }

    @Override
    public void addChangeListener(ChangeListener cl) {
    }

    @Override
    public void removeChangeListener(ChangeListener cl) {
    }

    private Set loadProjects(FileObject dir) {
        Set<Project> result = new LinkedHashSet<>();

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

        return Collections.unmodifiableSet(result);
    }

}
