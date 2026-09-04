/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prueba.hmipanelsubproject;

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
public class HMIPanelSubprojectImpl implements Project {

    private final FileObject fo;
    private final ProjectState ps;
    private Lookup lkp;

    public HMIPanelSubprojectImpl(FileObject fo, ProjectState ps) {
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
                new HMIPanelSubprojectInfoImpl(),
                new HMIPanelSubprojectLogicalViewImpl(this),
                new HMIPanelCategorySubprojectProviderImpl(this), 
//                new HMIPanelCommunicationSubprojectProviderImpl(this), 
            //                new HMIPanelNotificationManagerSubprojectProviderImpl(this), 
            //                new HMIPanelRecipeSubprojectProviderImpl(this),                  
            //                new HMIPanelHistorialSubprojectProviderImpl(this),                  
            //                new HMIPanelScriptSubprojectProviderImpl(this),                  
            //                new HMIPanelReportSubprojectProviderImpl(this),                  
            //                new HMIPanelTextAndChartSubprojectProviderImpl(this),                  
            //                new HMIPanelUserManagementSubprojectProviderImpl(this),                  
            //                new HMIPanelOperatorConfigurationSubprojectProviderImpl(this),                  
            //                 
            });
        }
        return lkp;
    }

    public class HMIPanelSubprojectInfoImpl implements ProjectInformation {

        @StaticResource()
        public static final String PROJECT_ICON = "com/prueba/hmipanelsubproject/PanelOperador.png";

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
            return HMIPanelSubprojectImpl.this;
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

    public class HMIPanelSubprojectLogicalViewImpl implements LogicalViewProvider {

        @StaticResource()
        public static final String PANEL_SUBPROJECT_ICON = "com/prueba/hmipanelsubproject/PanelOperador.png";

        private final Project project;

        public HMIPanelSubprojectLogicalViewImpl(Project project) {
            this.project = project;
        }

        @Override
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
                                "Projects/com-prueba-hmipanelsubproject/Nodes"),
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
               //TODO: Agregar acciones para los proyectos
               return null;
            }

            @Override
            public Image getIcon(int type) {
                return ImageUtilities.loadImage(PANEL_SUBPROJECT_ICON);
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
