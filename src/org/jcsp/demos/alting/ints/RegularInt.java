    //////////////////////////////////////////////////////////////////////
    //                                                                  //
    //  JCSP ("CSP for Java") Libraries                                 //
    //  Copyright (C) 1996-2006 Peter Welch and Paul Austin.            //
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
    //                                                                  //
    //                                                                  //
    //////////////////////////////////////////////////////////////////////

package org.jcsp.demos.alting.ints;

import org.jcsp.lang.*;

/**
 * @author P.H.Welch
 */
public class RegularInt implements CSProcess {

  final private ChannelOutputInt out;
  final private int n;
  final private long interval;

  public RegularInt (final ChannelOutputInt out, final int n, final long interval) {
    this.out = out;
    this.n = n;
    this.interval = interval;
  }

  public void run () {

    final CSTimer tim = new CSTimer ();
    long timeout = tim.read ();          // read the (absolute) time

    while (true) {
      out.write (n);
      timeout += interval;               // set the next (absolute) timeout
      tim.after (timeout);               // wait until that (absolute) timeout
    }
  }

}
