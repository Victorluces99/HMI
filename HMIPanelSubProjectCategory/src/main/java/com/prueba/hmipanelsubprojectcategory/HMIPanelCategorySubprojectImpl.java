/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prueba.hmipanelsubprojectcategory;

import com.prueba.hmipanelsubprojectcategory.action.HMICategoryCreateDisplayAction;
import java.awt.Image;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author luis
 */
public class HMIPanelCategorySubprojectImpl implements Project {

    private final FileObject fo;
    private final ProjectState ps;
    private Lookup lkp;

    public HMIPanelCategorySubprojectImpl(FileObject fo, ProjectState ps) {
        this.fo = fo;
        this.ps = ps;
    }

    @Override
    public FileObject getProjectDirectory() {
        return fo;
    }

    @Override
    public Lookup getLookup() {
        if (lkp == null) {
            lkp = Lookups.fixed(new Object[]{
                // register your features here
                this,
                new HMIPanelCategorySubprojectInfoImpl(),
                new HMIPanelCategorySubprojectLogicalViewImpl(this),});
        }
        return lkp;

    }

    public class HMIPanelCategorySubprojectInfoImpl implements ProjectInformation {

        @StaticResource()
        public static final String PROJECT_ICON = "com/prueba/hmipanelsubprojectcategory/FolderBlue.png";

        @Override
        public String getName() {
            return getProjectDirectory().getName();
        }

        @Override
        public String getDisplayName() {
            return getName();
        }

        @Override
        public Icon getIcon() {
            return new ImageIcon(ImageUtilities.loadImage(PROJECT_ICON));
        }

        @Override
        public Project getProject() {
            return HMIPanelCategorySubprojectImpl.this;
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener pl) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener pl) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
    }

    public class HMIPanelCategorySubprojectLogicalViewImpl implements LogicalViewProvider {

        @StaticResource()
        public static final String CATEGORY_SUBPROJECT_ICON = "com/prueba/hmipanelsubprojectcategory/FolderBlue.png";

        private final Project project;

        public HMIPanelCategorySubprojectLogicalViewImpl(Project project) {
            this.project = project;
        }

        public Node createLogicalView() {
            try {
                //Obtain the project directory's node:
                FileObject projectDirectory = project.getProjectDirectory();
                DataFolder projectFolder = DataFolder.findFolder(projectDirectory);
                Node nodeOfProjectFolder = projectFolder.getNodeDelegate();
                //Decorate the project directory's node:
                return new PanelProjectNode(nodeOfProjectFolder, project);
            } catch (DataObjectNotFoundException donfe) {
                Exceptions.printStackTrace(donfe);
                //Fallback-the directory couldn't be created -
                //read-only filesystem or something evil happened
                return new AbstractNode(Children.LEAF);
            }
        }

        @Override
        public Node findPath(Node node, Object o) {
            return null;
        }

        private final class PanelProjectNode extends FilterNode {

            final Project project;

            public PanelProjectNode(Node node, Project project)
                    throws DataObjectNotFoundException {
                super(node,
                        NodeFactorySupport.createCompositeChildren(project,
                                "Projects/com-prueba-hmipanelsubprojectcategory/Nodes"),
                        //                  new FilterNode.Children(node),

                        new ProxyLookup(
                                new Lookup[]{
                                    Lookups.singleton(project),
                                    node.getLookup()
                                }));
                this.project = project;
            }

            @Override
            public Action[] getActions(boolean context) {
                /*
                1. Identificar el tipo de carpeta.
                Image: .bob
                Comunicaciones: .merlot
                
                2. Con el tipo de carpeta, generar las acciones correspondientes
                3. Retornar las acciones
                 */

                FileObject projectDir = this.project.getProjectDirectory();

                if (hasChildFile(projectDir, "template.bob")) {
                    return new Action[]{
                        CommonProjectActions.closeProjectAction(),
                        CommonProjectActions.deleteProjectAction(),
                        new HMICategoryCreateDisplayAction(this.project)
                    };
                } else if (hasChildFile(projectDir, "comunicaciones.merlot")) {
                    // Acciones para el tipo Comunicaciones
                    return new Action[]{
                        CommonProjectActions.closeProjectAction(),
                        
                    };
                }

                // NUNCA retornes null; usa un arreglo vacío o super.getActions(context)
                return new Action[0];

            }

            private boolean hasChildFile(FileObject folder, String fileName) {
                for (FileObject child : folder.getChildren()) {
                    if (child.isFolder()) {
                        if (child.getFileObject(fileName) != null || hasChildFile(child, fileName)) {
                            return true;
                        }
                    } else if (child.getNameExt().equalsIgnoreCase(fileName)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public Image getIcon(int type) {
                return ImageUtilities.loadImage(CATEGORY_SUBPROJECT_ICON);
            }

            @Override
            public Image getOpenedIcon(int type) {
                return getIcon(type);
            }

            @Override
            public String getDisplayName() {
                return project.getProjectDirectory().getName();
            }

        }
    }

}
