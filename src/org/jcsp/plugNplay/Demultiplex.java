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
 * This demultiplexes data from its input channel to its output channel array.
 *
 * <H2>Process Diagram</H2>
 * <p><img src="doc-files\Demultiplex1.gif"></p>
 * <H2>Description</H2>
 * <TT>Demultiplex</TT> is a process to convert the single stream of
 * (Integer, Object) messages sent from a <TT>Multiplex</TT> process on the other
 * end of its <TT>in</TT> channel back into separate streams (its <TT>out</TT>
 * channels).  It assumes that <TT>Multiplex</TT> operates on the same
 * size array of channels as its <TT>out</TT> array.
 * <P>
 * The <I>protocol</I> on the incoming multiplexed stream consists of
 * an <TT>Integer</TT>, that represents the channel identity of the
 * multiplexed data, followed by the multiplexed data.
 * <H2>Channel Protocols</H2>
 * <TABLE BORDER="2">
 *   <TR>
 *     <TH COLSPAN="3">Input Channels</TH>
 *   </TR>
 *   <TR>
 *     <TH>in</TH>
 *     <TD>java.lang.Integer, java.lang.Object</TD>
 *     <TD>
 *       A channel index followed by the multiplexed data.
 *     </TD>
 *   </TR>
 *   <TR>
 *     <TH COLSPAN="3">Output Channels</TH>
 *   </TR>
 *   <TR>
 *     <TH>out[]</TH>
 *     <TD>java.lang.Object</TD>
 *     <TD>
 *       All channels in this package carry integers.
 *     </TD>
 *   </TR>
 * </TABLE>
 *
 * @see org.jcsp.plugNplay.Multiplex
 * @see org.jcsp.plugNplay.Paraplex
 * @see org.jcsp.plugNplay.Deparaplex
 *
 * @author P.D.Austin
 */
public final class Demultiplex implements CSProcess
{
   /** The input Channel */
   private final ChannelInput in;
   
   /** The output Channels */
   private final ChannelOutput[] out;
   
   /**
    * Construct a new Demultiplex process with the input Channel in and the output
    * Channels out. The ordering of the Channels in the out array make
    * no difference to the functionality of this process.
    *
    * @param in the input channel
    * @param out the output channels
    */
   public Demultiplex(final ChannelInput in, final ChannelOutput[] out)
   {
      this.in = in;
      this.out = out;
   }
   
   /**
    * The main body of this process.
    */
   public void run()
   {
      while (true)
      {
         int index = ((Integer) in.read()).intValue();
         out[index].write(in.read());
      }
   }
}