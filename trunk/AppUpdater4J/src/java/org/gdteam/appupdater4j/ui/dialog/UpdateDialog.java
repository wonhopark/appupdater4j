package org.gdteam.appupdater4j.ui.dialog;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.gdteam.appupdater4j.UpdateController;
import org.gdteam.appupdater4j.UpdateControllerListener;
import org.gdteam.appupdater4j.model.ApplicationVersion;

public class UpdateDialog extends JFrame implements UpdateController {
    
    public static final int UPLOAD_STATE_COLUMN = 3;
    
    private JButton installButton, cancelButton;
    private JTable updateTable;
    
    private List<ApplicationVersion> versionList;
    private int currentTableIndex = -1;
    private long currentFileSize = 0;
    private long currentDownloadedSize = 0;
    private long elapsedTime = 0;
    
    private List<UpdateControllerListener> listenerList = new ArrayList<UpdateControllerListener>();
    
    private Timer timer = new Timer();
    private boolean downloading = false;


    public UpdateDialog() {
        
        this.setPreferredSize(new Dimension(512, 584));
        
        JPanel mainPane = new JPanel(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 20, 5, 20);
        
        mainPane.add(new JLabel(new ImageIcon(this.getClass().getClassLoader().getResource("refresh-64.png"))), gbc);
        
        gbc.gridx++;
        gbc.weighty = 0;
        gbc.weightx = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(20, 0, 5, 20);
        
        JLabel title = new JLabel("Nouvelle(s) version(s) disponible(s)");
        title.setFont(title.getFont().deriveFont(Font.BOLD));
        
        mainPane.add(title, gbc);
        
        gbc.gridy++;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(5, 0, 10, 20);
        
        mainPane.add(new JLabel("<html>L'installation de ce logiciel peut prendre quelques temps. Si vous n'etes pas pret a l'effectuer immediatement, vous pouvez selectionner le bouton \"Plus tard\".</html>"), gbc);
        
        this.getContentPane().add(mainPane);
        
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weighty = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 20, 0, 20);
        
        this.updateTable = new JTable(new DefaultTableModel(0, 4){

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
        });
        
        TableColumnModel columnModel = this.updateTable.getColumnModel();
        
        TableColumn stateColumn = columnModel.getColumn(0);
        stateColumn.setHeaderValue("");
        stateColumn.setMaxWidth(20);
        stateColumn.setMinWidth(20);
        
        TableColumn versionColumn = columnModel.getColumn(1);
        versionColumn.setHeaderValue("Version");
        
        TableColumn sizeColumn = columnModel.getColumn(2);
        sizeColumn.setHeaderValue("Taille");
        
        TableColumn downloadColumn = columnModel.getColumn(3);
        downloadColumn.setHeaderValue("Telechargement/Installation");
        downloadColumn.setCellRenderer(new UpdateActionCellRenderer());
        
        JTextArea description = new JTextArea();
        description.setEditable(false);
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(this.updateTable), new JScrollPane(description));
        
        mainPane.add(splitPane, gbc);
        
        gbc.gridy++;
        gbc.weighty = 0;
        gbc.insets = new Insets(10, 20, 20, 20);
        
        JLabel remark = new JLabel("<html>Remarque : l’utilisation de ce logiciel est soumise a l’acceptation du ou des contrats de licence de logiciel fourni(s) avec le logiciel en cours de mise a jour.</html>");
        
        mainPane.add(remark, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(10, 20, 20, 20);
        
        this.installButton = new JButton("Installer");
        this.installButton.setSelected(true);
        this.installButton.requestFocus();
        this.cancelButton = new JButton("Plus tard");
        
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(cancelButton);
        buttonPane.add(installButton);
        
        this.installButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                for (UpdateControllerListener listener : listenerList) {
                    listener.startUpdate(UpdateDialog.this, versionList);
                }
            }
        });
        
        mainPane.add(buttonPane, gbc);
        
        this.pack();
        
        //Resize table header sizes
        int updateTableWidth = this.updateTable.getWidth();
        int downloadWidth = ((updateTableWidth - 20) * 50 ) / 100;
        int versionWidth = ((updateTableWidth - 20) * 33 ) / 100;
        int sizeWidth = ((updateTableWidth - 20) * 17 ) / 100;
        
        versionColumn.setPreferredWidth(versionWidth);
        sizeColumn.setPreferredWidth(sizeWidth);
        downloadColumn.setPreferredWidth(downloadWidth);
        this.setLocationRelativeTo(null);
        
        splitPane.setDividerLocation(0.3);
        
        this.timer.schedule(new DownloadTask(), 0, 1000);
    }

    public void addUpdateControllerListener(UpdateControllerListener listener) {
        this.listenerList.add(listener);
    }

    public void displayController() {
        this.setVisible(true);
    }

    public void removeUpdateControllerListener(UpdateControllerListener listener) {
        this.listenerList.remove(listener);
    }

    public void setVersionToInstall(List<ApplicationVersion> versionList) {
        
        this.versionList = versionList;
        
        DefaultTableModel model = (DefaultTableModel) this.updateTable.getModel();
        int rowCount = model.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            model.removeRow(i);
        }
        
        
        for (ApplicationVersion applicationVersion : versionList) {
            
            Long fileSize = applicationVersion.getFileSize();
            String fileSizeToDisplay = "";
            if (fileSize != null) {
                fileSizeToDisplay = this.getHumanFriendlySize(fileSize.doubleValue());
            }
            
            UpdateAction updateAction = new UpdateAction();
            updateAction.setProgression(0);
            updateAction.setDescription("En attente");
            
            Object[] data = {"",
                             applicationVersion.getName(),
                             fileSizeToDisplay,
                             updateAction};
            
            model.addRow(data);
        }
        model.fireTableDataChanged();
    }
    
    private String getHumanFriendlySize(double length) {
        String[] sizes = {"B", "KB", "MB", "GB"};
        return getHumanFriendlySizeRec(length, sizes, 0);
    }
    
    private String getHumanFriendlySizeRec(double length, String[] sizes, int index) {
        DecimalFormat df = new DecimalFormat("#.#");
        if (length <= 1024) {
            return df.format(length) + " " + sizes[index];
        } else {
            //length > 1024
            double newLength = length / 1024;
            if (sizes.length > index) {
                //there are available sizes... Continue
                return getHumanFriendlySizeRec(newLength, sizes, index + 1);
            } else {
                //No avaialabe size.
                return df.format(newLength) + " " + sizes[index];
            }
        }
    }

    public void installationEnded() {
        // TODO Auto-generated method stub
        
    }

    public void installationFailed(Exception e) {
        // TODO Auto-generated method stub
        
    }

    public void installationStarted(String basedir) {
        // TODO Auto-generated method stub
        
    }

    public void downloadDone() {
        this.downloading = false;
        
        UpdateAction action = (UpdateAction) this.updateTable.getModel().getValueAt(this.currentTableIndex, UPLOAD_STATE_COLUMN);
        action.setDescription("Telecharge");
        action.setProgression(100);
        ((DefaultTableModel) this.updateTable.getModel()).setValueAt(action, currentTableIndex, UPLOAD_STATE_COLUMN);
        ((DefaultTableModel) this.updateTable.getModel()).fireTableCellUpdated(currentTableIndex, UPLOAD_STATE_COLUMN);
    }

    public void downloadFailed() {
        this.downloading = false;
        
        UpdateAction action = (UpdateAction) this.updateTable.getModel().getValueAt(this.currentTableIndex, UPLOAD_STATE_COLUMN);
        action.setDescription("Erreur");
        ((DefaultTableModel) this.updateTable.getModel()).setValueAt(action, currentTableIndex, UPLOAD_STATE_COLUMN);
        ((DefaultTableModel) this.updateTable.getModel()).fireTableCellUpdated(currentTableIndex, UPLOAD_STATE_COLUMN);
    }

    public void downloadStarted(long size) {
        this.downloading = true;
        this.currentTableIndex++;
        
        this.currentFileSize = size;
        this.currentDownloadedSize = 0;
        
        UpdateAction action = (UpdateAction) this.updateTable.getModel().getValueAt(this.currentTableIndex, UPLOAD_STATE_COLUMN);
        action.setDescription("Telechargement");
        action.setProgression(0);
        ((DefaultTableModel) this.updateTable.getModel()).setValueAt(action, currentTableIndex, UPLOAD_STATE_COLUMN);
        ((DefaultTableModel) this.updateTable.getModel()).fireTableCellUpdated(currentTableIndex, UPLOAD_STATE_COLUMN);
    }

    public void downloadedDataChanged(long size) {
        
        this.currentDownloadedSize = size;
        
        double progressValue = (Long.valueOf(this.currentDownloadedSize).doubleValue() / Long.valueOf(this.currentFileSize).doubleValue()) * Double.valueOf(100).doubleValue();
        
        UpdateAction action = (UpdateAction) this.updateTable.getModel().getValueAt(this.currentTableIndex, UPLOAD_STATE_COLUMN);
        action.setProgression(Double.valueOf(progressValue).intValue());
        ((DefaultTableModel) this.updateTable.getModel()).setValueAt(action, currentTableIndex, UPLOAD_STATE_COLUMN);
        ((DefaultTableModel) this.updateTable.getModel()).fireTableCellUpdated(currentTableIndex, UPLOAD_STATE_COLUMN);
    }

    public void flowSizeChanged(long size) {
        this.elapsedTime = (this.currentFileSize - this.currentDownloadedSize) / size;
        
        UpdateAction action = (UpdateAction) this.updateTable.getModel().getValueAt(this.currentTableIndex, UPLOAD_STATE_COLUMN);
        action.setDescription(this.getCountDownText(elapsedTime));
        ((DefaultTableModel) this.updateTable.getModel()).setValueAt(action, currentTableIndex, UPLOAD_STATE_COLUMN);
        ((DefaultTableModel) this.updateTable.getModel()).fireTableCellUpdated(currentTableIndex, UPLOAD_STATE_COLUMN);
    }
    
    private String getCountDownText(long duration) {
        long hour = duration / 3600;
        long min = (duration - (hour * 3600)) / 60;
        long sec = duration - (hour * 3600) - (min * 60);

        StringBuilder res = new StringBuilder();
        if (hour > 0){
            res.append(hour).append(":");
        }
        
        if (min < 10){
            res.append("0").append(min).append(":");
        } else {
            res.append(min).append(":");
        }
        
        if (sec < 10){
            res.append("0").append(sec).append(" s");
        } else {
            res.append(sec).append(" s");
        }
        
        return res.toString();
    }

    private class DownloadTask extends TimerTask {

        @Override
        public void run() {
            if (elapsedTime > 0){
                elapsedTime--;
                
                UpdateAction action = (UpdateAction) updateTable.getModel().getValueAt(currentTableIndex, UPLOAD_STATE_COLUMN);
                action.setDescription(getCountDownText(elapsedTime));
                ((DefaultTableModel) updateTable.getModel()).setValueAt(action, currentTableIndex, UPLOAD_STATE_COLUMN);
                ((DefaultTableModel) updateTable.getModel()).fireTableCellUpdated(currentTableIndex, UPLOAD_STATE_COLUMN);
            }
        }
        
        
    }
}
