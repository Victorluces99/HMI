/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prueba.hmipanelsubproject.action;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import org.apache.plc4x.malbec.s88.api.S88Element;
import org.apache.plc4x.malbec.s88.api.S88PlantModel;
import org.apache.plc4x.malbec.s88.api.S88Storage;
import org.apache.plc4x.malbec.s88.plant.services.S88ProjectServices;
import org.openide.filesystems.FileObject;


public class HMIPanelImportAction extends AbstractAction {

    private final FileObject panel;

    public HMIPanelImportAction(FileObject panel) {
        super("Importar proyecto");
        this.panel = panel;
    }

    private record Storage(File file) implements S88Storage {

        @Override
        public InputStream openInput() throws IOException, FileNotFoundException {
            return new FileInputStream(file);
        }

        @Override
        public OutputStream openOutput() throws IOException {
            return new FileOutputStream(file);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 1. Ventana de exploración (solo .xml)
        FileDialog chooser = new FileDialog((Frame) null, "SELECT FILE", FileDialog.LOAD);
        chooser.setVisible(true);
        String dir = chooser.getDirectory();
        String name = chooser.getFile();
        File file = new File(dir, name);
        Storage storage = new Storage(file);
        S88PlantModel modelo = S88ProjectServices
                .createRepository("xml", storage).loadPlant();
        preOrderIterativo(modelo.getRoot(), new File(dir));

    }

    public void createFolder(S88Element node, File parentDir) {
        File folder = new File(parentDir, node.getId());
        folder.mkdirs();
    }

    public void createCfgFile(File parentDir) {
        File cfgFile = new File(parentDir, "project.cfg");
        try {
            cfgFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createBobFile(S88Element node, File parentDir) {
        File bobFile = new File(parentDir, node.getId() + ".bob");
        try {
            bobFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void preOrderIterativo(S88Element root, File baseDir) {
        if (root == null || baseDir == null) {
            return;
        }

        Deque<S88Element> stack = new ArrayDeque<>();
        Map<S88Element, File> dirMap = new HashMap<>();

        stack.push(root);
        dirMap.put(root, baseDir);

        while (!stack.isEmpty()) {
            S88Element node = stack.poll();
            /* .remove() obtiene el valor y lo elimina  del mapa para liberar 
                memoria, antes se usaba File parentDir = dirMap.get(node); 
                TODO: ver si no es necesario remover el nodo del mapa.
             */
            File parentDir = dirMap.remove(node);
            System.out.println(node.getId() + " [" + node.getLevel() + "]");

            createFolder(node, parentDir);
            File currentDir = new File(parentDir, node.getId());
            createCfgFile(currentDir);

            // Comprobación segura de Boolean en 1 sola línea
            if (Boolean.TRUE.equals(node.getProperty("Check"))) {
                System.out.println("Tengo un check " + node.getId());
                createBobFile(node, currentDir);
            }

            List<S88Element> children = node.getChildren();
            if (children != null) {
                int size = children.size();
                for (int i = size - 1; i >= 0; i--) {
                    S88Element child = children.get(i);
                    dirMap.put(child, currentDir);
                    stack.push(child);
                }
            }
        }
    }

}
