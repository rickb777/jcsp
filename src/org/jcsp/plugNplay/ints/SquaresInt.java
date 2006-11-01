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

package org.jcsp.plugNplay.ints;

import org.jcsp.lang.*;

/**
 * Generates the integer stream <TT>1*1, 2*2, 3*3, etc</TT> by a somewhat unusual route.
 *
 * <H2>Process Diagram</H2>
 * <H3>External View</H3>
 * <p><IMG SRC="doc-files\SquaresInt1.gif"></p>
 * <H3>Internal View</H3>
 * <p><IMG SRC="doc-files\SquaresInt2.gif"></p>
 * <H2>Description</H2>
 * <TT>SquaresInt</TT> generates the sequence of squares of the
 * Natural numbers (starting from 1).
 * <P>
 * <H2>Channel Protocols</H2>
 * <TABLE BORDER="2">
 *   <TR>
 *     <TH COLSPAN="3">Output Channels</TH>
 *   </TR>
 *   <TR>
 *     <TH>out</TH>
 *     <TD>int</TD>
 *     <TD>
 *       All channels in this package carry integers.
 *     </TD>
 *   </TR>
 * </TABLE>
 *
 * @author P.D.Austin
 */
public final class SquaresInt implements CSProcess
{
   /** The output Channel */
   private final ChannelOutputInt out;
   
   /**
    * Construct a new SquaresInt process with the output Channel out.
    *
    * @param out the output channel
    */
   public SquaresInt(final ChannelOutputInt out)
   {
      this.out = out;
   }
   
   /**
    * The main body of this process.
    */
   public void run()
   {
      final One2OneChannelInt a = ChannelInt.createOne2One();
      final One2OneChannelInt b = ChannelInt.createOne2One();
      
      new Parallel(new CSProcess[] 
                  {
                     new NumbersInt(a.out()),
                     new IntegrateInt(a.in(), b.out()),
                     new PairsInt(b.in(), out)
                  }).run();
   }
}