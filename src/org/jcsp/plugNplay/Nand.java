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

package org.jcsp.plugNplay;

import org.jcsp.lang.*;

/**
 * Bitwise <I>nands</I> two <TT>Integer</TT> streams to one stream.
 *
 * <H2>Process Diagram</H2>
 * <!-- INCORRECT DIAGRAM: <p><img src="doc-files/Nand1.gif"></p> -->
 * <PRE>
 *    in0  ______
 *   -->--|      | out
 *    in1 | Nand |-->--
 *   -->--|______|
 * </PRE>
 * <H2>Description</H2>
 * This is a process with an infinite loop that waits for
 * a Object of type Number to be sent down each of its input channels.
 * The loop body then calculates the bitwise NAND on the values of the
 * two Numbers and writes the result as a new Integer to its output channel.
 * <P>
 * <H2>Channel Protocols</H2>
 * <TABLE BORDER="2">
 *   <TR>
 *     <TH COLSPAN="3">Input Channels</TH>
 *   </TR>
 *   <TR>
 *     <TH>in1,in2</TH>
 *     <TD>java.lang.Number</TD>
 *     <TD>
 *       Both Channels can accept data from any subclass of Number. It is
 *       possible to send Floats down one channel Nand Integers down the
 *       other. However all values will be converted to ints.
 *     </TD>
 *   </TR>
 *   <TR>
 *     <TH COLSPAN="3">Output Channels</TH>
 *   </TR>
 *   <TR>
 *     <TH>out</TH>
 *     <TD>java.lang.Integer</TD>
 *     <TD>
 *       The output will always be of type Integer.
 *     </TD>
 *   </TR>
 * </TABLE>
 * <P>
 * <H2>Example</H2>
 * The following example shows how the use of this process in a small program.
 * The program also uses some of the other building block processes.
 * It generates a sequence of numbers, rounds each odd number down to
 * the nearest even number, negates them and prints them to the screen.
 *
 * <PRE>
 * import org.jcsp.lang.*;
 * import org.jcsp.plugNplay.*;
 * 
 * public class NandExample {
 * 
 *   public static void main (String[] argv) {
 * 
 *     One2OneChannel a = Channel.one2one ();
 *     One2OneChannel b = Channel.one2one ();
 *     One2OneChannel c = Channel.one2one ();
 *     One2OneChannel d = Channel.one2one ();
 * 
 *     new Parallel (
 *       new CSProcess[] {
 *         new Numbers (a.out ()),
 *         new Generate (b.out (), Integer.MAX_VALUE - 1),
 *         new Nand (a.in (), b.in (), c.out ()),
 *         new Successor (c.in (), d.out ()),
 *         new Printer (d.in (), "--> ", "\n")
 *       }
 *     ).run ();
 * 
 *   }
 * 
 * }
 * </PRE>
 *
 * @author P.D.Austin
 */
public final class Nand implements CSProcess
{
   /** The first input Channel */
   private ChannelInput in1;
   
   /** The second input Channel */
   private ChannelInput in2;
   
   /** The output Channel */
   private ChannelOutput out;
   
   /**
    * Construct a new Nand process with the input Channels in1 and in2 and the
    * output Channel out. The ordering of the Channels in1 and in2 make
    * no difference to the functionality of this process.
    *
    * @param in1 the first input Channel
    * @param in2 the second input Channel
    * @param out the output Channel
    */
   public Nand(ChannelInput in1, ChannelInput in2, ChannelOutput out)
   {
      this.in1 = in1;
      this.in2 = in2;
      this.out = out;
   }
   
   /**
    * The main body of this process.
    */
   public void run()
   {
      ProcessRead[] procs = {new ProcessRead(in1), new ProcessRead(in2)};
      Parallel par = new Parallel(procs);
      
      while (true)
      {
         par.run();
         int i1 = ((Number)procs[0].value).intValue();
         int i2 = ((Number)procs[1].value).intValue();
         out.write(new Integer(~ (i1 & i2)));
      }
   }
}
