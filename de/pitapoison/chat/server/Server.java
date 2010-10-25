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

package de.pitapoison.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    public final static int SERVER_PORT=9000;
    
    public Server()
    {
        try
        {
            ServerSocket serverSocket=new ServerSocket(SERVER_PORT);
            ServerGUI.message("Chat Server auf Port " + SERVER_PORT + " gestartet");
            
            while(true)
            {
                Socket socket=serverSocket.accept();
                new ClientThread(socket);
            }
        }
        catch(IOException e)
        {
            ServerGUI.message("Fehler beim Erzeugen des Server Sockets");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
