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

package org.jcsp.plugNplay.ints;

import org.jcsp.lang.*;

/**
 * Bitwise <I>nands</I> two integer streams to one stream.
 *
 * <H2>Process Diagram</H2>
 * <p><IMG SRC="doc-files\NandInt1.gif"></p>
 * <H2>Description</H2>
 * <TT>NandInt</TT> is a process whose output strean is the bitwise <I>nand</I>
 * of the integers on its input streams.
 * <P>
 * <H2>Channel Protocols</H2>
 * <TABLE BORDER="2">
 *   <TR>
 *     <TH COLSPAN="3">Input Channels</TH>
 *   </TR>
 *   <TR>
 *     <TH>in0, in1</TH>
 *     <TD>int</TD>
 *       All channels in this package carry integers.
 *     </TD>
 *   </TR>
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
 * <P>
 * <H2>Example</H2>
 * The following example shows how to use the NandInt process in a small program.
 * The program also uses some of the other building block processes. The
 * program generates the sequence of natural numbers, calculates their negative
 * values and prints this on the screen.
 *
 * <PRE>
 * import org.jcsp.lang.*;
 * import org.jcsp.util.ints.*;
 * <I></I>
 * public final class NandIntExample {
 * <I></I>
 *   public static void main (String[] argv) {
 * <I></I>
 *     final One2OneChannelInt a = ChannelInt.createOne2One ();
 *     final One2OneChannelInt b = ChannelInt.createOne2One ();
 *     final One2OneChannelInt c = ChannelInt.createOne2One ();
 *     final One2OneChannelInt d = ChannelInt.createOne2One ();
 * <I></I>
 *     new Parallel (
 *       new CSProcess[] {
 *         new NumbersInt (a.out ()),
 *         new GenerateInt (b.out (), Integer.MAX_VALUE),
 *         new NandInt (a.in (), b.in (), c.out ()),
 *         new SuccessorInt (c.in (), d.out ()),
 *         new PrinterInt (d.in ())
 *       }
 *     ).run ();
 * <I></I>
 *   }
 * <I></I>
 * }
 * </PRE>
 *
 * @author P.D.Austin
 */
public final class NandInt implements CSProcess
{
   /** The first input Channel */
   private final ChannelInputInt in0;
   
   /** The second input Channel */
   private final ChannelInputInt in1;
   
   /** The output Channel */
   private final ChannelOutputInt out;
   
   /**
    * Construct a new NandInt process with the input Channels in0 and in1 and the
    * output Channel out. The ordering of the Channels in0 and in1 make
    * no difference to the functionality of this process.
    *
    * @param in0 the first input Channel
    * @param in1 the second input Channel
    * @param out the output Channel
    */
   public NandInt(final ChannelInputInt in0, final ChannelInputInt in1, final ChannelOutputInt out)
   {
      this.in0 = in0;
      this.in1 = in1;
      this.out = out;
   }
   
   /**
    * The main body of this process.
    */
   public void run()
   {
      final ProcessReadInt[] procs = {new ProcessReadInt(in0), new ProcessReadInt(in1)};
      final Parallel par = new Parallel(procs);
      
      while (true)
      {
         par.run();
         final int i0 = procs[0].value;
         final int i1 = procs[1].value;
         out.write(~ (i0 & i1));
      }
   }
}