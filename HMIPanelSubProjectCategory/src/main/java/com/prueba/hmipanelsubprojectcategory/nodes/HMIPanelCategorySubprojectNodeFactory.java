/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prueba.hmipanelsubprojectcategory.nodes;

import com.prueba.hmipanelsubprojectcategory.HMIPanelCategorySubprojectImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

@NodeFactory.Registration(projectType = "com-prueba-hmipanelsubproject", position = 50)
public class HMIPanelCategorySubprojectNodeFactory implements NodeFactory {

    private static final String CATEGORY_FILE = "category.cfg";

    @Override
    public NodeList<?> createNodes(Project project) {
        return new CategoryNodeList(project.getProjectDirectory());
    }

    private static class CategoryNodeList implements NodeList<Project>, FileChangeListener {

        private final FileObject dir;
        private final List<ChangeListener> listeners = new ArrayList<>();
        private final List<Project> keys = new ArrayList<>();

        public CategoryNodeList(FileObject dir) {
            this.dir = dir;
            refreshKeys();
        }

        @Override
        public List<Project> keys() {
            return keys;
        }

        @Override
        public Node node(Project key) {
            LogicalViewProvider lvp = key.getLookup().lookup(LogicalViewProvider.class);
            return (lvp != null) ? lvp.createLogicalView() : null;
        }

        @Override
        public void addChangeListener(ChangeListener cl) {
            listeners.add(cl);
        }

        @Override
        public void removeChangeListener(ChangeListener cl) {
            listeners.remove(cl);
        }

        private void fireChange() {
            ChangeEvent event = new ChangeEvent(this);
            for (ChangeListener listener : new ArrayList<>(listeners)) {
                listener.stateChanged(event);
            }
        }

//        @Override
//        public void addNotify() {
//            dir.addFileChangeListener(this);
//            refreshKeys();
//        }
//
//        @Override
//        public void removeNotify() {
//            dir.removeFileChangeListener(this);
//        }
        @Override
        public void addNotify() {
            dir.addRecursiveListener(this);
            refreshKeys();
        }

        @Override
        public void removeNotify() {
            dir.removeRecursiveListener(this);
        }

        private void refreshKeys() {
            keys.clear();
            for (FileObject child : dir.getChildren()) {
                if (!child.isFolder()) {
                    continue;
                }
                try {
                    Project p = ProjectManager.getDefault().findProject(child);
                    if (p instanceof HMIPanelCategorySubprojectImpl) {
                        keys.add(p);
                    }
                } catch (IOException | IllegalArgumentException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            fireChange();
        }

        @Override
        public void fileFolderCreated(FileEvent fe) {
            refreshKeys();
        }

//        @Override
//        public void fileDataCreated(FileEvent fe) {
//            if (CATEGORY_FILE.equalsIgnoreCase(fe.getFile().getNameExt())) {
//                refreshKeys();
//            }
//        }
        @Override
        public void fileDataCreated(FileEvent fe) {
            refreshKeys();
        }

        @Override
        public void fileDeleted(FileEvent fe) {
            refreshKeys();
        }

        @Override
        public void fileRenamed(FileRenameEvent fre) {
            refreshKeys();
        }

        @Override
        public void fileChanged(FileEvent fe) {
            //TODO
        }

        @Override
        public void fileAttributeChanged(FileAttributeEvent fae) {
            //TODO
        }
    }
}
