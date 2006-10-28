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
 * <I>Fair</I> multiplexes two integer streams into one.
 * <H2>Process Diagram</H2>
 * <p><IMG SRC="doc-files\Plex2Int1.gif"></p>
 * <H2>Description</H2>
 * <TT>Plex2Int</TT> is a process whose output stream is a <I>fair</I> multiplexing
 * of its input streams.  It makes no assumptions about the traffic patterns occuring
 * on the input streams and ensures that no input stream will be starved by busy siblings.
 * It guarantees that if an input stream has data pending, no other stream will be
 * serviced <I>twice</I> before that data is serviced.
 * <H2>Channel Protocols</H2>
 * <TABLE BORDER="2">
 *   <TR>
 *     <TH COLSPAN="3">Input Channels</TH>
 *   </TR>
 *   <TR>
 *     <TH>in0, in1</TH>
 *     <TD>int</TD>
 *     <TD>
 *       The input streams.
 *     </TD>
 *   </TR>
 *   <TR>
 *     <TH COLSPAN="3">Output Channels</TH>
 *   </TR>
 *   <TR>
 *     <TH>out</TH>
 *     <TD>int</TD>
 *     <TD>
 *       The multiplexed output stream.
 *     </TD>
 *   </TR>
 * </TABLE>
 * <H2>Example</H2>
 * The following example shows how to use <TT>Plex2Int</TT> in a small program.
 * The program also uses some of the other <TT>plugNplay</TT> processes.
 *
 * <PRE>
 * import org.com.quickstone.jcsp.lang.*;
 * import org.com.quickstone.jcsp.plugNplay.ints.*;
 * <I></I>
 * public final class Plex2IntExample {
 * <I></I>
 *   public static void main (String[] argv) {
 * <I></I>
 *     final One2OneChannelInt a = ChannelInt.createOne2One ();
 *     final One2OneChannelInt b = ChannelInt.createOne2One ();
 *     final One2OneChannelInt c = ChannelInt.createOne2One ();
 * <I></I>
 *     new Parallel (
 *       new CSProcess[] {
 *         new FibonacciInt (a.out ()),
 *         new SquaresInt (b.out ()),
 *         new Plex2Int (a.in (), b.in (), c.out ()),
 *         new PrinterInt (c.in (), "--> ", "\n")
 *       }
 *     ).run ();
 * <I></I>
 *   }
 * <I></I>
 * }
 * </PRE>
 * Note: this example does not produce easily understandable output, since
 * the multiplexed stream contains only numbers -- there is no indication
 * of the streams from which they were sourced.  To get that indication,
 * we can either use {@link MultiplexInt} or <I>sign</I> each <TT>int</TT>
 * stream to be multiplexed with {@link SignInt} and multiplex with
 * {@link com.quickstone.jcsp.plugNplay.Plex2}.
 * <P>
 * <H2>Implemntation Note</H2>
 * For information, here is the <TT>run</TT> method for this process:
 *
 * <PRE>
 *   public void run () {
 *     AltingChannelInputInt[] input = {in0, in1};      // in0 and in1 are the input channels
 *     Alternative alt = new Alternative (input);
 *     while (true) {
 *       out.write (input[alt.fairSelect ()].in ().read ());  // out is the output channel
 *     }
 *   }
 * </PRE>
 *
 * @see org.jcsp.plugNplay.ints.PlexInt
 * @see org.jcsp.plugNplay.ints.MultiplexInt
 *
 * @author P.H.Welch
 */

public final class Plex2Int implements CSProcess
{
   /** The first input Channel */
   private final AltingChannelInputInt in0;
   
   /** The second input Channel */
   private final AltingChannelInputInt in1;
   
   /** The output Channel */
   private final ChannelOutputInt out;
   
   /**
    * Construct a new <TT>Plex2Int</TT> process with the input channels
    * <TT>in0</TT> and <TT>in1</TT> and the output channel <TT>out</TT>.
    * The ordering of the input channels makes no difference
    * to the behaviour of this process.
    *
    * @param in0 an input channel
    * @param in1 an input channel
    * @param out the output channel
    */
   public Plex2Int(AltingChannelInputInt in0, AltingChannelInputInt in1, ChannelOutputInt out)
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
      AltingChannelInputInt[] input = {in0, in1};
      Alternative alt = new Alternative(input);
      while (true)
         out.write(input[alt.fairSelect()].read());
   }
}