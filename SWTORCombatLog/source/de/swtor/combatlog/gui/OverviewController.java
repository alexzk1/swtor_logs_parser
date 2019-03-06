package de.swtor.combatlog.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.Layer;
import org.jfree.ui.LengthAdjustmentType;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

import de.swtor.combatlog.Constants;
import de.swtor.combatlog.data.AbstractValuedData;
import de.swtor.combatlog.data.CalculatedResult;
import de.swtor.combatlog.data.Damage;
import de.swtor.combatlog.data.DataStore;
import de.swtor.combatlog.data.Fight;
import de.swtor.combatlog.data.Heal;
import de.swtor.combatlog.data.HumanPlayerCharacter;
import de.swtor.combatlog.gui.table.TableRenderer;
import de.swtor.combatlog.localization.Localization;

/*
 * Copyright (c) 2012 Thomas Rath
 */

public class OverviewController extends AbstractGuiController {		
	
	private ResultController activeResult = null; 
	private XYSeries healSeries;
	private XYSeries damageSeries;
	private JFreeChart actualJFreeChart = null;
	private ValueMarker dpsValueMarker = null;
	private ValueMarker hpsValueMarker = null;
	
	public OverviewController(GuiController parent) {
		super(parent);
	}

	@Override
	public void updateActions() {
		getParent().updateActions();
	}

	@Override
	public void startController() {
		try {
			startProgress();
			
			setView(new OverviewPanel());
			
			fillOverViewPanel();
			
			activeResult = new ResultController(this, null);
			
			activeResult.startController();
			
			getView().getFightTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent e) {
					startProgress();
					
					int selection = getView().getFightTable().getSelectedRow();
										
					CalculatedResult calculatedResult = null;
					if (selection == 0) {
						calculatedResult = DataStore.calculatedResult;	
						fillTimeSeries(DataStore.findHumanPlayerCharacter(DataStore.mainHumanPlayerName), Localization.getInstance().tr(Localization.ANALYZER_TOTAL),
								       DataStore.startDate, DataStore.endDate, DataStore.totalDPS, DataStore.totalHPS);
					} else if (selection > 0) {
						Fight fight = DataStore.fights.get(selection-1);
						String chartTitle = fight.getName()+"\n"+fight.getTargets();
						fillTimeSeries(fight.findHumanPlayerCharacter(DataStore.mainHumanPlayerName), chartTitle,
								fight.getStartDate(), fight.getEndDate(), fight.getDps(), fight.getHps());
						calculatedResult = DataStore.fights.get(selection-1).getCalculatedResult();
					}					
					
					activeResult.displayValues(calculatedResult);
					
					updateActions();
					
					stopProgress();
				}
			});
			
			createChart();
			
			getView().getFightTable().getSelectionModel().setSelectionInterval(0, 0);	
			getView().getFightTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			getView().getFightTable().getColumnModel().getColumn(0).setPreferredWidth(70);
			getView().getFightTable().getColumnModel().getColumn(1).setPreferredWidth(150);
			getView().getFightTable().getColumnModel().getColumn(2).setPreferredWidth(75);
			getView().getFightTable().getColumnModel().getColumn(3).setPreferredWidth(75);
			getView().getFightTable().getColumnModel().getColumn(4).setPreferredWidth(55);
			getView().getFightTable().getColumnModel().getColumn(5).setPreferredWidth(55);
			
			getView().getSplitPane().setDividerLocation(300);
			
			getParent().getView().add(getView(), BorderLayout.CENTER);
		} finally {
			stopProgress();
		}

	}

	@Override
	public void performAction(String actionCommand) {
		// at the moment no actions
	}

	public OverviewPanel getView() {
		return (OverviewPanel)super.getView();
	}
	
	public void addChildViewToContainer(Component child) {
		getView().getDetailPanel().add(child, BorderLayout.CENTER);
	}
	
	@Override
	public void removeChildViewFromContainer(Component child) {
		getView().getDetailPanel().remove(child);
	}
	
	private void fillOverViewPanel() {
		String[][] fightArray = new String[DataStore.fights.size()+1][6];
		
		fightArray[0][0] = Localization.getInstance().tr(Localization.ANALYZER_TOTAL);
		fightArray[0][1] = Localization.getInstance().tr(Localization.ANALYZER_SUM);
		fightArray[0][2] = Constants.dateFormat.format(DataStore.startDate);
		fightArray[0][3] = Constants.dateFormat.format(DataStore.endDate);
		fightArray[0][4] = String.valueOf(DataStore.totalDPS);
		fightArray[0][5] = String.valueOf(DataStore.totalHPS);
		
		int i = 1;
		for (Fight fight : DataStore.fights) {
			fightArray[i][0] = fight.getName();
			fightArray[i][1] = fight.getTargets();
			fightArray[i][2] = Constants.dateFormat.format(fight.getStartDate());
			fightArray[i][3] = Constants.dateFormat.format(fight.getEndDate());
			fightArray[i][4] = String.valueOf(fight.getDps());
			fightArray[i][5] = String.valueOf(fight.getHps());
			
			i++;
		}
		
		@SuppressWarnings("serial")
		TableModel tableModel = new DefaultTableModel(fightArray, new String[] {
				Localization.getInstance().tr(Localization.FIGHT),
				Localization.getInstance().tr(Localization.TARGETS),
				Localization.getInstance().tr(Localization.FROM),
				Localization.getInstance().tr(Localization.TO),
				Localization.getInstance().tr(Localization.DPS),
				Localization.getInstance().tr(Localization.HPS)}) {

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};				
		
		getView().getFightTable().setModel(tableModel);	
		getView().getFightTable().setDefaultRenderer(Object.class, new TableRenderer());
		
		// TODO Special sorter for fight names
//		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>();
//		getView().getFightTable().setRowSorter(sorter);
//		sorter.setModel(tableModel);		
	}
	
	public boolean isSelected() {
		return (getView().getFightTable().getSelectedRow() > -1);
	}
	
	public List<AbstractValuedData> getSelectedDamageDone() {
		List<AbstractValuedData> selectedData = null;
		
		int selectedRow = getView().getFightTable().getSelectedRow();
		
		if (selectedRow > -1) {
			if (selectedRow == 0) {
				selectedData = new ArrayList<AbstractValuedData>(DataStore.findHumanPlayerCharacter(DataStore.mainHumanPlayerName).getDamageDealed());				
			} else if (selectedRow > 0) {
				Fight fight = DataStore.fights.get(selectedRow-1);
				selectedData = new ArrayList<AbstractValuedData>(fight.findHumanPlayerCharacter(DataStore.mainHumanPlayerName).getDamageDealed());
			}		
		}
		
		return selectedData;
	}
	
	public List<AbstractValuedData> getSelectedDamageReceived() {
		List<AbstractValuedData> selectedData = null;
		
		int selectedRow = getView().getFightTable().getSelectedRow();
		
		if (selectedRow > -1) {
			if (selectedRow == 0) {
				selectedData = new ArrayList<AbstractValuedData>(DataStore.findHumanPlayerCharacter(DataStore.mainHumanPlayerName).getDamageReceived());				
			} else if (selectedRow > 0) {
				Fight fight = DataStore.fights.get(selectedRow-1);
				selectedData = new ArrayList<AbstractValuedData>(fight.findHumanPlayerCharacter(DataStore.mainHumanPlayerName).getDamageReceived());
			}		
		}
		
		return selectedData;
	}	
	
	public List<AbstractValuedData> getSelectedHealingDone() {
		List<AbstractValuedData> selectedData = null;
		
		int selectedRow = getView().getFightTable().getSelectedRow();
		
		if (selectedRow > -1) {
			if (selectedRow == 0) {
				selectedData = new ArrayList<AbstractValuedData>(DataStore.findHumanPlayerCharacter(DataStore.mainHumanPlayerName).getHealDealed());				
			} else if (selectedRow > 0) {
				Fight fight = DataStore.fights.get(selectedRow-1);
				selectedData = new ArrayList<AbstractValuedData>(fight.findHumanPlayerCharacter(DataStore.mainHumanPlayerName).getHealDealed());
			}		
		}
		
		return selectedData;
	}	
	
	public List<AbstractValuedData> getSelectedHealingReceived() {
		List<AbstractValuedData> selectedData = null;
		
		int selectedRow = getView().getFightTable().getSelectedRow();
		
		if (selectedRow > -1) {
			if (selectedRow == 0) {
				selectedData = new ArrayList<AbstractValuedData>(DataStore.findHumanPlayerCharacter(DataStore.mainHumanPlayerName).getHealReceived());				
			} else if (selectedRow > 0) {
				Fight fight = DataStore.fights.get(selectedRow-1);
				selectedData = new ArrayList<AbstractValuedData>(fight.findHumanPlayerCharacter(DataStore.mainHumanPlayerName).getHealReceived());
			}		
		}
		
		return selectedData;
	}		
	
	public HumanPlayerCharacter getSelectedPlayer() {
		HumanPlayerCharacter selectedData = null;
		
		int selectedRow = getView().getFightTable().getSelectedRow();
		
		if (selectedRow > -1) {
			if (selectedRow == 0) {
				selectedData = DataStore.findHumanPlayerCharacter(DataStore.mainHumanPlayerName);				
			} else if (selectedRow > 0) {
				Fight fight = DataStore.fights.get(selectedRow-1);
				selectedData = fight.findHumanPlayerCharacter(DataStore.mainHumanPlayerName);
			}		
		}
		
		return selectedData;
	}			
	
	public String getSelectedDataName() {
		String selectedData = null;
		
		int selectedRow = getView().getFightTable().getSelectedRow();
		
		if (selectedRow > -1) {
			if (selectedRow == 0) {
				selectedData = Localization.getInstance().tr(Localization.ANALYZER_TOTAL);				
			} else if (selectedRow > 0) {
				Fight fight = DataStore.fights.get(selectedRow-1);
				selectedData = fight.getName();
			}		
		}
		
		return selectedData;
	}
	
	private void createChart() {
		actualJFreeChart = createChartIntern();
		ChartPanel chartPanel = new ChartPanel(actualJFreeChart);

		getView().getChartPanel().removeAll();
		getView().getChartPanel().add(chartPanel, BorderLayout.CENTER);		
	}

	private JFreeChart createChartIntern() {
		JFreeChart localJFreeChart = ChartFactory.createXYLineChart("EMPTY", Localization.getInstance().tr(Localization.SECONDS),
				Localization.getInstance().tr(Localization.COLUMN_DAMAGE) + "/" +
				Localization.getInstance().tr(Localization.COLUMN_HEAL), (XYDataset)null, PlotOrientation.VERTICAL, true, true, false);
		
	    ChartUtilities.applyCurrentTheme(localJFreeChart);

		XYPlot localXYPlot = (XYPlot) localJFreeChart.getPlot();
		localXYPlot.setDomainPannable(false);
		localXYPlot.setRangePannable(false);
		localXYPlot.setDomainCrosshairVisible(false);
		localXYPlot.setRangeCrosshairVisible(false);
		
		localXYPlot.getRenderer().setSeriesPaint(1,  Color.green);
		
	    // Add damage Collection
		XYSeriesCollection seriesCollection = new XYSeriesCollection();	    
		damageSeries = new XYSeries(Localization.getInstance().tr(Localization.COLUMN_DAMAGE));
		seriesCollection.addSeries(damageSeries);
		localXYPlot.setDataset(0, seriesCollection);
		
		 // Add heal Collection
		healSeries = new XYSeries(Localization.getInstance().tr(Localization.COLUMN_HEAL));	
		seriesCollection.addSeries(healSeries);
		
		// DPS marker
	    dpsValueMarker = new ValueMarker(0);
	    dpsValueMarker.setLabelOffsetType(LengthAdjustmentType.EXPAND);
	    dpsValueMarker.setPaint(ChartColor.DARK_RED);
	    dpsValueMarker.setStroke(new BasicStroke(2.0F));
	    dpsValueMarker.setLabel(Localization.getInstance().tr(Localization.DPS));
	    dpsValueMarker.setLabelFont(new Font("SansSerif", 0, 11));
	    dpsValueMarker.setLabelPaint(ChartColor.DARK_RED);
	    dpsValueMarker.setLabelAnchor(RectangleAnchor.TOP_LEFT);
	    dpsValueMarker.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT);
	    localXYPlot.addRangeMarker(dpsValueMarker, Layer.BACKGROUND);
	    
		// HPS marker
	    hpsValueMarker = new ValueMarker(0);
	    hpsValueMarker.setLabelOffsetType(LengthAdjustmentType.EXPAND);
	    hpsValueMarker.setPaint(ChartColor.DARK_GREEN);
	    hpsValueMarker.setStroke(new BasicStroke(2.0F));
	    hpsValueMarker.setLabel(Localization.getInstance().tr(Localization.HPS));
	    hpsValueMarker.setLabelFont(new Font("SansSerif", 0, 11));
	    hpsValueMarker.setLabelPaint(ChartColor.DARK_GREEN);
	    hpsValueMarker.setLabelAnchor(RectangleAnchor.TOP_LEFT);
	    hpsValueMarker.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT);
	    localXYPlot.addRangeMarker(hpsValueMarker, Layer.BACKGROUND);
			
		return (JFreeChart) localJFreeChart;
	}	 
	
	private void fillTimeSeries(HumanPlayerCharacter humanPlayerCharacter, String chartTitle, Date from, Date to,
			double dps, double hps) {
		actualJFreeChart.setTitle(chartTitle);		

		damageSeries.clear();
		healSeries.clear();

		if (null != humanPlayerCharacter) {
			
			long baseMilliSecond = from.getTime();			
			int maxPerSecond = 0;
			long currentSecond = -1;
			
			if (null == humanPlayerCharacter.getDamageCondensed()) {
				Hashtable<Long, Integer> damageHash = new Hashtable<Long, Integer>();
				humanPlayerCharacter.setDamageCondensed(damageHash);
				
				for (Damage damage : humanPlayerCharacter.getDamageDealed()) {
					long actualSecond = (damage.getDate().getTime() - baseMilliSecond) / 1000;
					if (currentSecond == -1) {
						currentSecond = actualSecond;
						damageHash.put(currentSecond, damage.getValue());
					}

					if (actualSecond > currentSecond) {
						damageHash.put(currentSecond, maxPerSecond);
						currentSecond = actualSecond;
						maxPerSecond = damage.getValue();
					} else {
						if (damage.getValue() > maxPerSecond) {
							maxPerSecond = damage.getValue();
						}
					}
				}
			}
			
			maxPerSecond = 0;
			currentSecond = -1;
			if (null == humanPlayerCharacter.getHealCondensed()) {
				Hashtable<Long, Integer> healHash = new Hashtable<Long, Integer>();
				humanPlayerCharacter.setHealCondensed(healHash);
				
				for (Heal heal : humanPlayerCharacter.getHealDealed()) {
					long actualSecond = (heal.getDate().getTime() - baseMilliSecond) / 1000;
					if (currentSecond == -1) {
						currentSecond = actualSecond;
						healHash.put(currentSecond, heal.getValue());
					}

					if (actualSecond > currentSecond) {
						healHash.put(currentSecond, maxPerSecond);
						currentSecond = actualSecond;
						maxPerSecond = heal.getValue();
					} else {
						if (heal.getValue() > maxPerSecond) {
							maxPerSecond = heal.getValue();
						}
					}
				}
			}						
			
			Enumeration<Long> keys = humanPlayerCharacter.getDamageCondensed().keys();
			while (keys.hasMoreElements()) {
				Long second = keys.nextElement();		
				damageSeries.addOrUpdate(second, humanPlayerCharacter.getDamageCondensed().get(second));			
			}	
			
			keys = humanPlayerCharacter.getHealCondensed().keys();
			while (keys.hasMoreElements()) {
				Long second = keys.nextElement();		
				healSeries.addOrUpdate(second, humanPlayerCharacter.getHealCondensed().get(second));			
			}			

			dpsValueMarker.setValue(dps);
			hpsValueMarker.setValue(hps);
		}
	}

}
