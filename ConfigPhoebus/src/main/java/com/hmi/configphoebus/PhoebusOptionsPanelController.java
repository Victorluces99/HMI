/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/NetBeansModuleDevelopment-files/template_mypluginOptionsPanelController.java to edit this template
 */
package com.hmi.configphoebus;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.prefs.Preferences;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbPreferences;

@OptionsPanelController.TopLevelRegistration(
        categoryName = "#OptionsCategory_Name_Phoebus",
        iconBase = "com/hmi/configphoebus/phoebus.png",
        keywords = "#OptionsCategory_Keywords_Phoebus",
        keywordsCategory = "Phoebus"
)
@org.openide.util.NbBundle.Messages({"OptionsCategory_Name_Phoebus=Phoebus", "OptionsCategory_Keywords_Phoebus=abc"})
public final class PhoebusOptionsPanelController extends OptionsPanelController {

    private PhoebusPanel panel;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private boolean changed;

    // 1. Carga los datos guardados cuando el usuario abre la ventana de Opciones
    @Override
    public void update() {
        Preferences prefs = NbPreferences.forModule(PhoebusPanel.class);
        // Recupera el valor. Si no existe, deja el campo vacío ""
        String rutaGuardada = prefs.get("phoebus.path", "");
         getPanel().setRutaPhoebus(rutaGuardada);
    }

    // 2. Guarda los datos cuando el usuario hace clic en "OK" o "Apply"
    @Override
    public void applyChanges() {
        Preferences prefs = NbPreferences.forModule(PhoebusPanel.class);
        String rutaActual = getPanel().getRutaPhoebus().trim();
        prefs.put("phoebus.path", rutaActual);
    }

    // 3. Activa o desactiva el botón "Apply" de NetBeans si detecta cambios en el texto
    @Override
    public boolean isChanged() {
        Preferences prefs = NbPreferences.forModule(PhoebusPanel.class);
        String rutaGuardada = prefs.get("phoebus.path", "");
        String rutaActual = getPanel().getRutaPhoebus().trim();
        
        // Si el texto actual es diferente al guardado, hay cambios
        return !rutaActual.equals(rutaGuardada);
    }

    // El asistente maneja este método para inicializar el panel visual
    private PhoebusPanel getPanel() {
        if (panel == null) {
            panel = new PhoebusPanel(this);
        }
        return panel;
    }
    @Override
    public void cancel() {
        // need not do anything special, if no changes have been persisted yet
    }

    @Override
    public boolean isValid() {
        return getPanel().valid();
    }


    @Override
    public HelpCtx getHelpCtx() {
        return null; // new HelpCtx("...ID") if you have a help set
    }

    @Override
    public JComponent getComponent(Lookup masterLookup) {
        return getPanel();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    void changed() {
        if (!changed) {
            changed = true;
            pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, false, true);
        }
        pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
    }

}
