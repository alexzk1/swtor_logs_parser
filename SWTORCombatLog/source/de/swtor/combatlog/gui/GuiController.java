package de.swtor.combatlog.gui;

/*
 * Copyright (c) 2012 Thomas Rath
 */

import java.awt.*;
import java.awt.event.ActionListener;

public interface GuiController extends ActionListener
{

    public Container getView();

    public void setView(Container view);

    public void updateActions();

    public GuiController getParent();

    public void startController();

    public void stopController();

    public void startProgress();

    public void stopProgress();

    public void addChildViewToContainer(Component child);

    public void removeChildViewFromContainer(Component child);

    public void performAction(String actionCommand);
}
