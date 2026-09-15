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
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

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
        File[] selected = chooser.getFiles();
        if (selected == null || selected.length == 0) {
            return; // usuario canceló
        }
        File file = selected[0];
        Storage storage = new Storage(file);
        S88PlantModel modelo = S88ProjectServices
                .createRepository("xml", storage).loadPlant();

        // 2. Resolver la carpeta "image" dentro del panel (crear si no existe)
        FileObject imageFo = panel.getFileObject("image");
        if (imageFo == null || !imageFo.isFolder()) {
            try {
                imageFo = panel.createFolder("image");
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
                return;
            }
        }

        // 3. La carpeta "image" actúa como categoría contenedora
        try {
            if (imageFo.getFileObject("category.cfg") == null) {
                imageFo.createData("category.cfg");
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        // 4. Omitir el nodo raíz: iterar sus hijos con "image" como base
        S88Element root = modelo.getRoot();
        if (root != null && root.getChildren() != null) {
            for (S88Element child : root.getChildren()) {
                preOrderIterativo(child, imageFo);
            }
        }

        // 5. Red de seguridad: refrescar el filesystem del panel
        File panelFile = FileUtil.toFile(panel);
        if (panelFile != null) {
            FileUtil.refreshFor(panelFile);
        }
    }

    private FileObject createFolder(S88Element node, FileObject parentDir) throws IOException {
        FileObject folder = parentDir.getFileObject(node.getId());
        return (folder == null) ? parentDir.createFolder(node.getId()) : folder;
    }

    private void createCfgFile(FileObject folder) throws IOException {
        if (folder.getFileObject("category.cfg") == null) {
            folder.createData("category.cfg");
        }
    }

    private void createBobFile(S88Element node, FileObject folder) throws IOException {
        if (folder.getFileObject(node.getId() + ".bob") == null) {
            folder.createData(node.getId(), "bob");
        }
    }

    private Object getPropertyInsensitive(S88Element node, String key) {
        if (node == null || key == null) {
            return null;
        }

        Object val = node.getProperty(key);
        if (val != null && !"".equals(val)) {
            return val;
        }

        String[] variants = {
            key.toLowerCase(),
            key.toUpperCase(),
            key.substring(0, 1).toUpperCase() + key.substring(1).toLowerCase()
        };

        for (String variant : variants) {
            val = node.getProperty(variant);
            if (val != null && !"".equals(val)) {
                return val;
            }
        }

        return null;
    }

    public void preOrderIterativo(S88Element root, FileObject baseDir) {
        if (root == null || baseDir == null) {
            return;
        }

        Deque<S88Element> stack = new ArrayDeque<>();
        Map<S88Element, FileObject> dirMap = new HashMap<>();

        stack.push(root);
        dirMap.put(root, baseDir);

        while (!stack.isEmpty()) {
            S88Element node = stack.poll();
            FileObject parentDir = dirMap.remove(node);

            if (parentDir == null) {
                continue;
            }

            try {
                FileObject currentDir = createFolder(node, parentDir);
                createCfgFile(currentDir);

                System.out.println("Propiedades en " + node.getId() + ": " + node.getProperties());

                Object checkValue = getPropertyInsensitive(node, "Check");
                boolean isChecked = false;
                if (checkValue instanceof Boolean) {
                    isChecked = (Boolean) checkValue;
                } else if (checkValue != null) {
                    isChecked = "true".equalsIgnoreCase(checkValue.toString().trim());
                }

                if (isChecked) {
                    System.out.println("-> Creando BOB File para: " + node.getId());
                    createBobFile(node, currentDir);
                }

                List<S88Element> children = node.getChildren();
                if (children != null) {
                    for (int i = children.size() - 1; i >= 0; i--) {
                        S88Element child = children.get(i);
                        dirMap.put(child, currentDir);
                        stack.push(child);
                    }
                }
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

}
