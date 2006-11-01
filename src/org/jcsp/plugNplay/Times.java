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
 * <I>Multiplies</I> two <TT>Integer</TT> streams to one stream.
 *
 * <H2>Process Diagram</H2>
 * <p><img src="doc-files\Times1.gif"></p>
 * <H2>Description</H2>
 * <TT>Times</TT> is a process whose output stream is the product
 * of the Integers on its input streams.
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
 *       possible to send Floats down one channel and Integers down the
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
 * The following example shows how to use the Times process in a small program.
 * The program also uses some of the other building block processes. The
 * program generates a sequence of numbers and squares them and prints
 * this on the screen.
 *
 * <PRE>
 * import org.jcsp.lang.*;
 * import org.jcsp.util.*;
 *
 * public class TimesExample {
 * <I></I>
 *   public static void main (String[] argv) {
 * <I></I>
 *     One2OneChannel a = Channel.createOne2One ();
 *     One2OneChannel b = Channel.createOne2One ();
 *     One2OneChannel c = Channel.createOne2One ();
 * <I></I>
 *     new Prallel (
 *       new CSProcess[] {
 *         new Numbers (a.out ()),
 *         new Numbers (b.out ()),
 *         new Times (a.in (), b.in (), c.out ()),
 *         new Printer (c.in ())
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
public final class Times implements CSProcess
{
   /** The first input Channel */
   private ChannelInput in1;
   
   /** The second input Channel */
   private ChannelInput in2;
   
   /** The output Channel */
   private ChannelOutput out;
   
   /**
    * Construct a new Times process with the input Channels in1 and in2 and the
    * output Channel out. The ordering of the Channels in1 and in2 make
    * no difference to the functionality of this process.
    *
    * @param in1 the first input Channel
    * @param in2 the second input Channel
    * @param out the output Channel
    */
   public Times(ChannelInput in1, ChannelInput in2, ChannelOutput out)
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
      ProcessRead[] parRead = {new ProcessRead(in1), new ProcessRead(in2)};
      Parallel par = new Parallel(parRead);
      
      while (true)
      {
         par.run();
         int i1 = ((Number) parRead[0].value).intValue();
         int i2 = ((Number) parRead[1].value).intValue();
         out.write(new Integer(i1 * i2));
      }
   }
}