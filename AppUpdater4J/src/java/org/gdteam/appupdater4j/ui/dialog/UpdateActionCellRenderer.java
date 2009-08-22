package org.gdteam.appupdater4j.ui.dialog;

import info.clearthought.layout.TableLayout;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class UpdateActionCellRenderer extends JPanel implements TableCellRenderer {

    
    private JProgressBar progressBar;
    private JLabel description;
    
    public UpdateActionCellRenderer() {
        
        double sizes[][] = {{TableLayout.FILL, 0.5}, {TableLayout.FILL}};
        
        this.setLayout(new TableLayout(sizes));
        
        this.setOpaque(false);
        
        this.progressBar = new JProgressBar(0, 100);
        this.progressBar.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 5));
        this.add(this.progressBar, "0, 0");
        
        this.description = new JLabel();
        this.description.setOpaque(false);
        this.add(this.description, "1, 0");
    }
    
    
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        UpdateAction updateAction = (UpdateAction) value;
        
        this.progressBar.setValue(updateAction.getProgression());
        this.progressBar.setIndeterminate(updateAction.isIndeterminate());
        this.description.setText(updateAction.getDescription());
        
        return this;
    }

}
