package org.gdteam.appupdater4j.ui.dialog;

import info.clearthought.layout.TableLayout;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public class UpdateActionCellRenderer extends JPanel implements TableCellRenderer {

    private JProgressBar progressBar;
    private JLabel description;
    private JPanel progressPane;
    
    public UpdateActionCellRenderer() {
        
        double sizes[][] = {{TableLayout.FILL, 0.5}, {TableLayout.FILL}};
        
        this.setLayout(new TableLayout(sizes));
        
        this.setOpaque(true);
        
        this.progressBar = new JProgressBar(0, 100);
        this.progressBar.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 5));
        
        JLabel emptyLabel = new JLabel();
        emptyLabel.setOpaque(false);
        
        this.progressPane = new JPanel(new CardLayout());
        this.progressPane.setOpaque(false);
        progressPane.add(emptyLabel, "empty");
        progressPane.add(this.progressBar, "progress");
        
        this.add(progressPane, "0, 0");
        
        this.description = new JLabel();
        this.description.setOpaque(false);
        this.add(this.description, "1, 0");
    }
    
    
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        Color foreGround = UIManager.getLookAndFeelDefaults().getColor("Table.foreground");
        Color backGround = UIManager.getLookAndFeelDefaults().getColor("Table.background");
        
        if (isSelected) {
            foreGround = UIManager.getLookAndFeelDefaults().getColor("Table.selectionForeground");
            backGround = UIManager.getLookAndFeelDefaults().getColor("Table.selectionBackground");
        }
        
        this.setBackground(backGround);
        this.description.setForeground(foreGround);
        
        UpdateAction updateAction = (UpdateAction) value;
        
        CardLayout layout = (CardLayout) this.progressPane.getLayout();
        
        if (updateAction.getProgression() == 0) {
            layout.show(this.progressPane, "empty");
        } else {
            layout.show(this.progressPane, "progress");
        }
        
        this.progressBar.setValue(updateAction.getProgression());
        this.progressBar.setIndeterminate(updateAction.isIndeterminate());
        this.description.setText(updateAction.getDescription());
        
        
        
        return this;
    }

}
