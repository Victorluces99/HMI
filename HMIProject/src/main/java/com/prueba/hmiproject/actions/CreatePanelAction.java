/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prueba.hmiproject.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;


public class CreatePanelAction extends AbstractAction implements ContextAwareAction{
    
    private final Lookup context;
    
    private CreatePanelAction(Lookup context){
        super("New Panel");
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
