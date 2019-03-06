package de.swtor.combatlog.gui;

/*
 * Copyright (c) 2012 Thomas Rath
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GlassPane extends JPanel implements MouseListener,
        MouseMotionListener, FocusListener, KeyListener
{

    // helpers for redispatch logic
    private Toolkit toolkit;

    private JMenuBar menuBar;

    private GlassPane()
    {
    }

    public GlassPane(JMenuBar mb)
    {
        super();

        toolkit = Toolkit.getDefaultToolkit();
        menuBar = mb;

        addMouseListener(this);
        addMouseMotionListener(this);
        addFocusListener(this);
    }

    public void setVisible(boolean v)
    {
        // Make sure we grab the focus so that key events don't go astray.
        if (v)
        {
            requestFocus();
        }

        super.setVisible(v);
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    public void mouseDragged(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    public void mouseMoved(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    public void focusGained(FocusEvent e)
    {
        // TODO Auto-generated method stub

    }

    public void focusLost(FocusEvent e)
    {
        if (isVisible())
        {
            requestFocus();
        }
    }

    public void keyPressed(KeyEvent e)
    {
        // TODO Auto-generated method stub

    }

    public void keyReleased(KeyEvent e)
    {
        // TODO Auto-generated method stub

    }

    public void keyTyped(KeyEvent e)
    {
        // TODO Auto-generated method stub

    }

}

