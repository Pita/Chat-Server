/*
 * Copyright (C) 2010 Peter Martischka This program is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version. This program is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received
 * a copy of the GNU General Public License along with this program; if not, see
 * <http://www.gnu.org/licenses/ >.
 */

package de.pitapoison.chat.client;

import java.awt.BorderLayout;
import java.awt.SystemTray;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ClientGUI extends JFrame
{
    private Client2ServerInterface serverInterface;
    private JLabel chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JScrollPane chatAreaScrollPane;
    
    public ClientGUI(String servername, String username)
    {
        //JFrame erzeugen
        super("Chat - " + username);
        
        setLayout(new BorderLayout());
   
        //Chat feld plazieren
        chatArea=new JLabel("<html>");
        chatArea.setVerticalAlignment(SwingConstants.TOP);
        chatAreaScrollPane=new JScrollPane(chatArea);
        chatAreaScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        chatAreaScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(chatAreaScrollPane);
        
        //Unteres Panel initialiseren
        JPanel bottomPanel=new JPanel(new BorderLayout());
        add(bottomPanel, BorderLayout.SOUTH);
        
        //Eingabefeld initialiseren
        inputField=new JTextField();
        bottomPanel.add(inputField);
        
        inputField.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                send();
            }
        });
        
        //Send Button initialisieren
        sendButton=new JButton("Senden");
        bottomPanel.add(sendButton, BorderLayout.EAST);
        
        //OK Button sendet dem Interface die Eingabe
        sendButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                send();
            }  
        });
        
        //Schnittstelle zum Chat Server herstellen
        try
        {
            serverInterface=new Client2ServerInterface(this, servername, username);
        }
        catch(IOException e)
        {
            err("Fehler beim Verbinden zum Chatserver", e);
        }
        
        //Fenster anzeigen
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void send()
    {
        //Abrechen wenn kein Text vorhanden ist
        if(inputField.getText().length()==0)
            return;
        
        try
        {
            serverInterface.send(inputField.getText());
            inputField.setText("");
        }
        catch(IOException e1)
        {
            err("Fehler beim Senden zum Server", e1);
        }
    }
    
    public static void main(String args[])
    {
        //System Look And Feel einstellen
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        //Servername abfragen
        String servername=null;
        while(servername==null   )
            servername=JOptionPane.showInputDialog("Zu welchem Server wollen sie sich verbinden?");
        
        //Username abfragen
        String username=null;
        while(username==null   )
            username=JOptionPane.showInputDialog("Welchen Nickname wollen sie verwenden?");
        
        new ClientGUI(servername, username);
    }

    public void addMessage(String message)
    {
        if(message != null)
        {
            //Erzeuge einen Zeitstempel für die neue Nachricht
            String time=DateFormat.getTimeInstance().format(new Date());
            message="(" + time + ") " + message;
            
            //Setze den neuen Text, mitsamt Zeilenumbruch, in das Panel
            chatArea.setText(chatArea.getText() + message + "<br>\n");
            
            //Scroll das Panel herunter
            SwingUtilities.invokeLater( new Runnable() 
            {
                public void run() 
                {
                    JScrollBar bar=chatAreaScrollPane.getVerticalScrollBar();
                    bar.setValue(chatArea.getHeight());
                }
            });
            
            this.toFront();
        }
    }
    
    public void err(String message, Exception e)
    {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, message);
        //System.exit(1);
    }
}
