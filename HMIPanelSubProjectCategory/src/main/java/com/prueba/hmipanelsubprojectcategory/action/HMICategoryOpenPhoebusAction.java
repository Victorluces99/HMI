/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prueba.hmipanelsubprojectcategory.action;

import com.hmi.configphoebus.PhoebusOptionsPanelController;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.AbstractAction;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

public class HMICategoryOpenPhoebusAction extends AbstractAction {

    private final FileObject bobFile;
    private static final String MEMENTO_TEMPLATE
            = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
            + "<memento default_application=\"Display Editor\" last_opened_file=\"\" show_menu=\"true\" show_statusbar=\"true\" show_tabs=\"true\" show_toolbar=\"true\">\n"
            + "    <DockStage_MAIN height=\"1000.0\" maximized=\"true\" width=\"1296.0\" x=\"-8.0\" y=\"-8.0\">\n"
            + "        <pane selected=\"0\">\n"
            + "            <DockItem_%ID% application=\"display_editor\" user_name=\"%USER%\" input_uri=\"%URI%\"/>\n"
            + "        </pane>\n"
            + "    </DockStage_MAIN>\n"
            + "</memento>\n";

    public HMICategoryOpenPhoebusAction(FileObject bobFile) {
        this.bobFile = bobFile;
        putValue(NAME, "Abrir en Phoebus");
    }

    public void actionPerformed(ActionEvent e) {
        RequestProcessor.getDefault().post(() -> OpenNativeBob(this.bobFile));
        System.out.println("Acción abrir ejecutada sobre: " + bobFile.getNameExt());
    }

    private void OpenNativeBob(FileObject nuevoArchivoBob) {
        File file = FileUtil.toFile(nuevoArchivoBob);
        if (file == null) {
            return;
        }

        java.util.prefs.Preferences prefs = org.openide.util.NbPreferences.forModule(PhoebusOptionsPanelController.class);
        String rutaPhoebus = prefs.get("phoebus.path", "").trim();

        if (rutaPhoebus.isEmpty()) {
            org.openide.DialogDisplayer.getDefault().notify(
                    new org.openide.NotifyDescriptor.Message("Por favor, configure la ruta de Phoebus en Tools -> Options.",
                            org.openide.NotifyDescriptor.INFORMATION_MESSAGE)
            );
            return;
        }

       
        boolean esWindows = System.getProperty("os.name").toLowerCase().contains("win");
        File ejecutablePhoebus = new File(rutaPhoebus);

        if (esWindows) {
            if (!rutaPhoebus.toLowerCase().endsWith(".bat")) {
                org.openide.DialogDisplayer.getDefault().notify(
                        new org.openide.NotifyDescriptor.Message(
                                "Error de configuración: En Windows, la herramienta externa de Phoebus debe apuntar a un archivo ejecutable '.bat'.\nPor favor, corríjalo en Tools -> Options.",
                                org.openide.NotifyDescriptor.ERROR_MESSAGE)
                );
                return;
            }
        } else {
            if (!rutaPhoebus.toLowerCase().endsWith(".sh")) {
                org.openide.DialogDisplayer.getDefault().notify(
                        new org.openide.NotifyDescriptor.Message(
                                "Error de configuración: En sistemas Unix/Linux, la herramienta externa de Phoebus debe apuntar a un script '.sh'.\nPor favor, corríjalo en Tools -> Options.",
                                org.openide.NotifyDescriptor.ERROR_MESSAGE)
                );
                return;
            }
        }

        if (!ejecutablePhoebus.exists()) {
            org.openide.DialogDisplayer.getDefault().notify(
                    new org.openide.NotifyDescriptor.Message(
                            "El archivo configurado para Phoebus no existe en la ruta especificada:\n" + rutaPhoebus,
                            org.openide.NotifyDescriptor.ERROR_MESSAGE)
            );
            return;
        }

        try {
            OverwriteMemento(file);
            ProcessBuilder pb = new ProcessBuilder(
                    rutaPhoebus,
                    "-nosplash"
            );

            pb.redirectErrorStream(true);
            pb.start();
            System.out.println("Phoebus lanzado de forma segura para: " + file.getName());

        } catch (Exception ex) {
            org.openide.DialogDisplayer.getDefault().notify(
                    new org.openide.NotifyDescriptor.Message("Error al intentar ejecutar Phoebus: " + ex.getMessage(),
                            org.openide.NotifyDescriptor.ERROR_MESSAGE)
            );
            Exceptions.printStackTrace(ex);
        }
    }

    private void OverwriteMemento(File file) throws IOException {

        String userHome = System.getProperty("user.home");
        Path rutaMemento = Paths.get(userHome, ".phoebus", "memento");
        File mementoFile = rutaMemento.toFile();

        //Construir la URI del archivo (siempre con / y escapada para XML)
        String nuevaUri = "file:/" + file.getAbsolutePath().replace("\\", "/");
        nuevaUri = XmlEscaping(nuevaUri);

        //Si el memento no existe, crearlo con su estructura base y el usuario
        if (!mementoFile.exists()) {
            String user = System.getProperty("user.name", "user");
            String uid = "DockItem_" + UUID.randomUUID().toString().replace("-", "_");
            String base = MEMENTO_TEMPLATE
                    .replace("%ID%", uid)
                    .replace("%USER%", XmlEscaping(user))
                    .replace("%URI%", nuevaUri);
            Files.createDirectories(rutaMemento.getParent());
            Files.write(rutaMemento, base.getBytes(StandardCharsets.UTF_8));
            System.out.println("[abrirBobNativo] Memento creado en: " + rutaMemento);
            return;
        }

        /*Leer todo el contenido del memento como un arreglo de bytes y
        los convierte a una cadena de texto */
        String contenido = new String(Files.readAllBytes(rutaMemento), StandardCharsets.UTF_8);
        System.out.println("[abrirBobNativo] Contenido ANTES: " + contenido);
        System.out.println("[abrirBobNativo] Nueva URI: " + nuevaUri);

        //Reemplazar el input_uri anterior por el nuevo y en caso de que no exita lo agrega.
        if (contenido.contains("input_uri=")) {
            contenido = contenido.replaceFirst("input_uri=\"[^\"]*\"", "input_uri=\"" + nuevaUri + "\"");
        } else {
            // Recrear memento desde plantilla
            String user = System.getProperty("user.name", "user");
            String uid = "DockItem_" + UUID.randomUUID().toString().replace("-", "_");
            contenido = MEMENTO_TEMPLATE
                    .replace("%ID%", uid)
                    .replace("%USER%", XmlEscaping(user))
                    .replace("%URI%", nuevaUri);
        }
        System.out.println("[abrirBobNativo] Contenido DESPUÉS: " + contenido);

        //Sobrescribe el archivo memento con la nueva info
        Files.write(rutaMemento, contenido.getBytes(StandardCharsets.UTF_8));
    }

    private static String XmlEscaping(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("&", "&amp;")
                .replace("\"", "&quot;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

}
