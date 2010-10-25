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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;

public class Client2ServerInterface extends Thread
{
    private ClientGUI gui;
    private BufferedReader reader;
    private BufferedWriter writer;
    
    public final static int SERVER_PORT=9000;
    
    public Client2ServerInterface(ClientGUI gui, String servername, String username) throws IOException
    {
        super();
        
        this.gui=gui;
        
        //Socket öffnen
        Socket serverSocket=new Socket(servername, SERVER_PORT);
        reader=new BufferedReader(new InputStreamReader(serverSocket.getInputStream(), Charset.forName("UTF8")));
        writer=new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream(), Charset.forName("UTF8")));
        
        //User authentifizieren
        writer.write("User:" + username + "\n");
        writer.write("\n");
        writer.flush();
        
        start();
    }

    public void send(String text) throws IOException
    {
        //Schreibe den Inhalt in den Stream
        writer.write(text + "\n");
        writer.flush();
    }

    public void run()
    {
        //Lese in Endlosschleife vom Server
        while(true)
        {
            try
            {
                gui.addMessage(reader.readLine());
            }
            catch(IOException e)
            {
                gui.err("Fehler beim Empfangen der Daten vom Server", e);
            }
        }
    }
}
