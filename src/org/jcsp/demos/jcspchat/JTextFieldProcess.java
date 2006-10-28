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

import javax.swing.*;
import javax.swing.event.*;
import org.jcsp.lang.*;
import org.jcsp.net.*;
import java.awt.event.*;

/**
 * @author Quickstone Technologies Limited
 */
public class JTextFieldProcess implements CSProcess {
  private ChannelOutput out;
  private JTextField jtf;
  private ChannelInput namein;

  public JTextFieldProcess(JTextField field, ChannelOutput chan, boolean needsConfirm) {
    jtf = field;
    out = chan;
    if (needsConfirm) {
      jtf.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          JTextFieldProcess.this.out.write(jtf.getText());
          jtf.setText("");
        }
      });
    }
    else {
      jtf.getDocument().addDocumentListener(new DocumentListener() {
        public void changedUpdate(DocumentEvent e) {
        }
        public void insertUpdate (DocumentEvent e) {
          JTextFieldProcess.this.out.write(jtf.getText());
        }
        public void removeUpdate (DocumentEvent e) {}
      });
    }
  }
  public void run() {
  }
}
