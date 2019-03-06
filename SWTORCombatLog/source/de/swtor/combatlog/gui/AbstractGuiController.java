package de.swtor.combatlog.gui;

/*
 * Copyright (c) 2012 Thomas Rath
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public abstract class AbstractGuiController implements GuiController, Actions
{
    private GuiController parent;
    private Container view;
    private Hashtable actionElements = new Hashtable();

    public AbstractGuiController()
    {
    }

    public AbstractGuiController(GuiController parent)
    {
        this.parent = parent;
    }

    public GuiController getParent()
    {
        return parent;
    }

    public Container getView()
    {
        return view;
    }

    public void setView(Container view)
    {
        this.view = view;
    }

    public void startProgress()
    {
        if (parent != null)
        {
            parent.startProgress();
        }
    }

    public void stopProgress()
    {
        if (parent != null)
        {
            parent.stopProgress();
        }
    }

    public void stopController()
    {
        if (getView() != null && getParent() != null && getParent().getView() != null)
        {
            getParent().getView().remove(getView());
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        performAction(e.getActionCommand());
    }

    @Override
    public void addChildViewToContainer(Component child)
    {
        getView().add(child);
    }

    @Override
    public void removeChildViewFromContainer(Component child)
    {
        getView().remove(child);
    }

    protected void setActionCommand(String actionCommand, AbstractButton button)
    {
        button.addActionListener(this);
        button.setActionCommand(actionCommand);

        java.util.List list = (java.util.List) actionElements.get(actionCommand);
        if (list == null)
            list = new ArrayList();

        list.add(button);
        actionElements.put(actionCommand, list);
    }

    /*
  * Possibility to enable/disable certain GUI components
  * @param items the set of GUI components
  * @param enable if true enable all elements in vector, if false disable all
  *               and vice versa
  */
    protected void updateWidgets(Vector<String> items, boolean enable)
    {
        if (items == null) return;

        boolean opposite = !enable;

        for (Iterator e = actionElements.entrySet().iterator(); e.hasNext(); )
        {
            Map.Entry entry = (Map.Entry) e.next();
            java.util.List elementList = (java.util.List) (entry.getValue());

            for (int i = 0; i < elementList.size(); i++)
            {
                JComponent element = (JComponent) elementList.get(i);
                if (items.contains(entry.getKey()))
                {
                    element.setEnabled(enable);
                } else
                {
                    element.setEnabled(opposite);
                }
            }
        }
    }

    /**
     * @return the actionElements
     */
    public Hashtable getActionElements()
    {
        return actionElements;
    }
}
