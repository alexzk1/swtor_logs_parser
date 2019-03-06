/*
 * Created by JFormDesigner on Tue Apr 24 17:59:22 CEST 2012
 */

package de.swtor.combatlog.gui;

import javax.swing.*;
import java.awt.*;

/*
 * Copyright (c) 2012 Thomas Rath
 */

/**
 * @author User #13
 */
public class OverviewPanel extends JPanel
{
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JSplitPane splitPane;
    private JPanel overviewPanel;
    private JScrollPane scrollPane1;
    private JTable fightTable;
    private JPanel chartPanel;
    private JPanel detailPanel;

    public OverviewPanel()
    {
        initComponents();
    }

    public JSplitPane getSplitPane()
    {
        return splitPane;
    }

    public JTable getFightTable()
    {
        return fightTable;
    }

    public JPanel getDetailPanel()
    {
        return detailPanel;
    }

    public JPanel getChartPanel()
    {
        return chartPanel;
    }

    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        splitPane = new JSplitPane();
        overviewPanel = new JPanel();
        scrollPane1 = new JScrollPane();
        fightTable = new JTable();
        chartPanel = new JPanel();
        detailPanel = new JPanel();

        //======== this ========
        setLayout(new BorderLayout());

        //======== splitPane ========
        {
            splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

            //======== overviewPanel ========
            {
                overviewPanel.setLayout(new GridBagLayout());
                ((GridBagLayout) overviewPanel.getLayout()).columnWidths = new int[]{0, 0, 0};
                ((GridBagLayout) overviewPanel.getLayout()).rowHeights = new int[]{0, 0};
                ((GridBagLayout) overviewPanel.getLayout()).columnWeights = new double[]{1.0, 1.0, 1.0E-4};
                ((GridBagLayout) overviewPanel.getLayout()).rowWeights = new double[]{1.0, 1.0E-4};

                //======== scrollPane1 ========
                {

                    //---- fightTable ----
                    fightTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    scrollPane1.setViewportView(fightTable);
                }
                overviewPanel.add(scrollPane1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

                //======== chartPanel ========
                {
                    chartPanel.setLayout(new BorderLayout());
                }
                overviewPanel.add(chartPanel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
            }
            splitPane.setTopComponent(overviewPanel);

            //======== detailPanel ========
            {
                detailPanel.setLayout(new BorderLayout());
            }
            splitPane.setBottomComponent(detailPanel);
        }
        add(splitPane, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
