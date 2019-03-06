package de.swtor.combatlog.gui;

/*
 * Copyright (c) 2012 Thomas Rath
 */

import de.swtor.combatlog.StartCombatLog;
import de.swtor.combatlog.data.AbsorbedDamage;
import de.swtor.combatlog.data.CalculatedResult;
import de.swtor.combatlog.data.Death;
import de.swtor.combatlog.gui.table.DamageAbsorbedTableModel;
import de.swtor.combatlog.gui.table.DeathKillsTableModel;
import de.swtor.combatlog.gui.table.TableRenderer;
import de.swtor.combatlog.gui.table.TableWithNumbersRowSorter;
import de.swtor.combatlog.icons.Icons;
import de.swtor.combatlog.localization.Localization;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ResultController extends AbstractGuiController
{

    private CalculatedResult calculatedResult = null;

    public ResultController(GuiController parent, CalculatedResult calculatedResult)
    {
        super(parent);

        this.calculatedResult = calculatedResult;
    }

    @Override
    public void updateActions()
    {
        // at the moment no actions

    }

    @Override
    public void startController()
    {
        try
        {
            startProgress();

            setView(new ResultPanel());

            localizeTabbedPane();

            displayValues(calculatedResult);

            getParent().addChildViewToContainer(getView());
        } catch (Exception e)
        {
            StartCombatLog.getLogger().log(Level.SEVERE, "Error during displaying values", e);
            JOptionPane
                    .showMessageDialog(MainController.getSingleton()
                                    .getMainWindow(), e.getMessage(),
                            "Error during displaying values",
                            JOptionPane.ERROR_MESSAGE);
        } finally
        {
            stopProgress();
        }
    }

    @Override
    public void stopController()
    {
        getParent().removeChildViewFromContainer(getView());
        getParent().getView().repaint();
    }

    @Override
    public void performAction(String actionCommand)
    {
        // at the moment no actions

    }

    @Override
    public ResultPanel getView()
    {
        return (ResultPanel) super.getView();
    }

    private void localizeTabbedPane()
    {
        getView().getTabbedPane().setTitleAt(0, Localization.getInstance().tr(Localization.TABBED_PANE_DAMAGE_DEALED) + " ");
        getView().getTabbedPane().setIconAt(0, Icons.DAMAGE_DONE_16x16);
        getView().getTabbedPane().setTitleAt(1, Localization.getInstance().tr(Localization.TABBED_PANE_DAMAGE_RECEIVED) + " ");
        getView().getTabbedPane().setIconAt(1, Icons.DAMAGE_RECEIVED_16x16);
        getView().getTabbedPane().setTitleAt(2, Localization.getInstance().tr(Localization.TABBED_PANE_DAMAGE_ABSORBED) + " ");
        getView().getTabbedPane().setIconAt(2, Icons.DAMAGE_ABSORBED_16x16);
        getView().getTabbedPane().setTitleAt(3, Localization.getInstance().tr(Localization.TABBED_PANE_HEALED) + " ");
        getView().getTabbedPane().setIconAt(3, Icons.HEALING_DONE_16x16);
        getView().getTabbedPane().setTitleAt(4, Localization.getInstance().tr(Localization.TABBED_PANE_HEALING_RECEIVED) + " ");
        getView().getTabbedPane().setIconAt(4, Icons.HEALING_RECEIVED_16x16);
        getView().getTabbedPane().setTitleAt(5, Localization.getInstance().tr(Localization.TABBED_PANE_KILLS_DEATHS) + " ");
        getView().getTabbedPane().setIconAt(5, Icons.KILLS_DEATHS_16x16);
    }

    public void displayValues(CalculatedResult calculatedResult)
    {
        this.calculatedResult = calculatedResult;

        summerizeDamageDealedForUser();
        summerizeDamageReceivedForUser();
        summerizeHealedForUser();
        summerizeHealingReceivedForUser();
        fillKillsDeathTable();
        fillDamageAbsorbedTable();
    }

    @SuppressWarnings("serial")
    private void summerizeDamageDealedForUser()
    {
        String[][] result = null;

        if (null != calculatedResult)
        {
            result = calculatedResult.getDamageDealed();
        } else
        {
            result = new String[0][10];
        }

        TableModel tableModel = new DefaultTableModel(result, new String[]{
                Localization.getInstance().tr(Localization.COLUMN_NAME),
                Localization.getInstance().tr(Localization.COLUMN_NUMBER),
                Localization.getInstance().tr(Localization.COLUMN_NUMBER_PERCENT),
                Localization.getInstance().tr(Localization.COLUMN_DAMAGE),
                Localization.getInstance().tr(Localization.COLUMN_DAMAGE_PERCENT),
                Localization.getInstance().tr(Localization.COLUMN_CRITS),
                Localization.getInstance().tr(Localization.COLUMN_CRITS_PERCENT),
                Localization.getInstance().tr(Localization.COLUMN_MIN),
                Localization.getInstance().tr(Localization.COLUMN_MAX),
                Localization.getInstance().tr(Localization.COLUMN_AVG)
        })
        {

            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }

        };
        getView().getDamageTable().setModel(tableModel);
        // Set the first visible column to 150 pixels wide
        getView().getDamageTable().getColumnModel().getColumn(0).setPreferredWidth(150);
        getView().getDamageTable().setDefaultRenderer(Object.class, new TableRenderer());
    }

    @SuppressWarnings("serial")
    private void summerizeDamageReceivedForUser()
    {
        String[][] result = null;

        if (null != calculatedResult)
        {
            result = calculatedResult.getDamageReceived();
        } else
        {
            result = new String[0][11];
        }

        TableModel tableModel = new DefaultTableModel(result, new String[]{
                Localization.getInstance().tr(Localization.COLUMN_PLAYER_NAME),
                Localization.getInstance().tr(Localization.COLUMN_NAME),
                Localization.getInstance().tr(Localization.COLUMN_NUMBER),
                Localization.getInstance().tr(Localization.COLUMN_NUMBER_PERCENT),
                Localization.getInstance().tr(Localization.COLUMN_DAMAGE),
                Localization.getInstance().tr(Localization.COLUMN_DAMAGE_PERCENT),
                Localization.getInstance().tr(Localization.COLUMN_CRITS),
                Localization.getInstance().tr(Localization.COLUMN_CRITS_PERCENT),
                Localization.getInstance().tr(Localization.COLUMN_MIN),
                Localization.getInstance().tr(Localization.COLUMN_MAX),
                Localization.getInstance().tr(Localization.COLUMN_AVG)
        })
        {

            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }

        };

        getView().getDamageReceivedTable().setModel(tableModel);
        // Set the first and second visible column to 150 pixels wide
        getView().getDamageReceivedTable().getColumnModel().getColumn(0).setPreferredWidth(150);
        getView().getDamageReceivedTable().getColumnModel().getColumn(1).setPreferredWidth(150);
        getView().getDamageReceivedTable().setDefaultRenderer(Object.class, new TableRenderer());
    }

    @SuppressWarnings("serial")
    private void summerizeHealedForUser()
    {
        String[][] result = null;

        if (null != calculatedResult)
        {
            result = calculatedResult.getHealedDealed();
        } else
        {
            result = new String[0][10];
        }

        TableModel tableModel = new DefaultTableModel(result, new String[]{
                Localization.getInstance().tr(Localization.COLUMN_NAME),
                Localization.getInstance().tr(Localization.COLUMN_NUMBER),
                Localization.getInstance().tr(Localization.COLUMN_NUMBER_PERCENT),
                Localization.getInstance().tr(Localization.COLUMN_HEAL),
                Localization.getInstance().tr(Localization.COLUMN_HEAL_PERCENT),
                Localization.getInstance().tr(Localization.COLUMN_CRITS),
                Localization.getInstance().tr(Localization.COLUMN_CRITS_PERCENT),
                Localization.getInstance().tr(Localization.COLUMN_MIN),
                Localization.getInstance().tr(Localization.COLUMN_MAX),
                Localization.getInstance().tr(Localization.COLUMN_AVG)
        })
        {

            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }

        };
        getView().getHealedTable().setModel(tableModel);
        // Set the first visible column to 150 pixels wide
        getView().getHealedTable().getColumnModel().getColumn(0).setPreferredWidth(150);
        getView().getHealedTable().setDefaultRenderer(Object.class, new TableRenderer());
    }

    @SuppressWarnings("serial")
    private void summerizeHealingReceivedForUser()
    {
        String[][] result = null;

        if (null != calculatedResult)
        {
            result = calculatedResult.getHealingReceived();
        } else
        {
            result = new String[0][11];
        }

        TableModel tableModel = new DefaultTableModel(result, new String[]{
                Localization.getInstance().tr(Localization.COLUMN_HEALER),
                Localization.getInstance().tr(Localization.COLUMN_NAME),
                Localization.getInstance().tr(Localization.COLUMN_NUMBER),
                Localization.getInstance().tr(Localization.COLUMN_NUMBER_PERCENT),
                Localization.getInstance().tr(Localization.COLUMN_HEAL),
                Localization.getInstance().tr(Localization.COLUMN_HEAL_PERCENT),
                Localization.getInstance().tr(Localization.COLUMN_CRITS),
                Localization.getInstance().tr(Localization.COLUMN_CRITS_PERCENT),
                Localization.getInstance().tr(Localization.COLUMN_MIN),
                Localization.getInstance().tr(Localization.COLUMN_MAX),
                Localization.getInstance().tr(Localization.COLUMN_AVG)
        })
        {

            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }

        };
        getView().getHealingReceivedTable().setModel(tableModel);
        // Set the first and second visible column to 150 pixels wide
        getView().getHealingReceivedTable().getColumnModel().getColumn(0).setPreferredWidth(150);
        getView().getHealingReceivedTable().getColumnModel().getColumn(1).setPreferredWidth(150);
        getView().getHealingReceivedTable().setDefaultRenderer(Object.class, new TableRenderer());
    }

    private void fillKillsDeathTable()
    {
        List<Death> deathKills = null;
        if (null != calculatedResult)
        {
            deathKills = calculatedResult.getDeaths();
        } else
        {
            deathKills = new ArrayList<Death>();
        }

        DeathKillsTableModel tableModel = new DeathKillsTableModel(deathKills);
        getView().getKillsDeathsTable().setModel(tableModel);
        // Set the first and second visible column to 150 pixels wide
        getView().getKillsDeathsTable().getColumnModel().getColumn(1).setPreferredWidth(150);
        getView().getKillsDeathsTable().getColumnModel().getColumn(2).setPreferredWidth(150);
        getView().getKillsDeathsTable().getColumnModel().getColumn(3).setPreferredWidth(150);
        getView().getKillsDeathsTable().setDefaultRenderer(Object.class, new TableRenderer(false));

        // Make table sortable
        TableWithNumbersRowSorter<TableModel> sorter = new TableWithNumbersRowSorter<TableModel>();
        getView().getKillsDeathsTable().setRowSorter(sorter);
        sorter.setModel(tableModel);
        // special comparator for number columns
        sorter.createComparatorsForNumberColumns(tableModel.getColumnsWithNumberValues());
    }

    private void fillDamageAbsorbedTable()
    {
        List<AbsorbedDamage> absorbedDamages = null;
        if (null != calculatedResult)
        {
            absorbedDamages = calculatedResult.getAbsorbedDamages();
        } else
        {
            absorbedDamages = new ArrayList<AbsorbedDamage>();
        }

        DamageAbsorbedTableModel tableModel = new DamageAbsorbedTableModel(absorbedDamages);
        getView().getDamageAbsorbedTable().setModel(tableModel);
        // Set the first and second visible column to 150 pixels wide
        getView().getDamageAbsorbedTable().getColumnModel().getColumn(1).setPreferredWidth(150);
        getView().getDamageAbsorbedTable().getColumnModel().getColumn(2).setPreferredWidth(150);
        getView().getDamageAbsorbedTable().getColumnModel().getColumn(3).setPreferredWidth(150);
        getView().getDamageAbsorbedTable().setDefaultRenderer(Object.class, new TableRenderer(false));

        // Make table sortable
        TableWithNumbersRowSorter<TableModel> sorter = new TableWithNumbersRowSorter<TableModel>();
        getView().getDamageAbsorbedTable().setRowSorter(sorter);
        sorter.setModel(tableModel);
        // special comparator for number columns
        sorter.createComparatorsForNumberColumns(tableModel.getColumnsWithNumberValues());
    }
}
