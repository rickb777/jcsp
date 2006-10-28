    //////////////////////////////////////////////////////////////////////
    //                                                                  //
    //  JCSP ("CSP for Java") Libraries                                 //
    //  Copyright (C) 1996-2001 Peter Welch and Paul Austin.            //
    //                2001-2004 Quickstone Technologies Limited.        //
    //                                                                  //
    //  This library is free software; you can redistribute it and/or   //
    //  modify it under the terms of the GNU Lesser General Public      //
    //  License as published by the Free Software Foundation; either    //
    //  version 2.1 of the License, or (at your option) any later       //
    //  version.                                                        //
    //                                                                  //
    //  This library is distributed in the hope that it will be         //
    //  useful, but WITHOUT ANY WARRANTY; without even the implied      //
    //  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR         //
    //  PURPOSE. See the GNU Lesser General Public License for more     //
    //  details.                                                        //
    //                                                                  //
    //  You should have received a copy of the GNU Lesser General       //
    //  Public License along with this library; if not, write to the    //
    //  Free Software Foundation, Inc., 59 Temple Place, Suite 330,     //
    //  Boston, MA 02111-1307, USA.                                     //
    //                                                                  //
    //  Author contact: P.H.Welch@ukc.ac.uk                             //
    //                  mailbox@quickstone.com                          //
    //                                                                  //
    //////////////////////////////////////////////////////////////////////

package org.jcsp.demos.jcspchat;
import java.io.*;

/**
 * @author Quickstone Technologies Limited
 */
public class MessageObject implements Serializable {
  public static final int USERMESSAGE = 0;
  public static final int CONNECT = 1;
  public static final int DISCONNECT = 2;
  public String user;
  public String message;
  public int sysCommand;

  public MessageObject(String user, String message) {
    this.user = user;
    this.message = message;
    this.sysCommand = USERMESSAGE;
  }
  public MessageObject(String user, String message, int sysCommand) {
    this.user = user;
    this.message = message;
    this.sysCommand = sysCommand;


  }
}
