    //////////////////////////////////////////////////////////////////////
    //                                                                  //
    //  JCSP ("CSP for Java") Libraries                                 //
    //  Copyright (C) 1996-2008 Peter Welch and Paul Austin.            //
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
    //  Author contact: P.H.Welch@kent.ac.uk                             //
    //                                                                  //
    //                                                                  //
    //////////////////////////////////////////////////////////////////////

package org.jcsp.lang;

/**
 * This defines the interface for writing to integer channels.
 * <H2>Description</H2>
 * <TT>ChannelOutputInt</TT> defines the interface for writing to integer channels.
 * The interface contains only one method - <TT>write(int o)</TT>.
 * This method will block the calling process until the <TT>int</TT> has
 * been accepted by the channel.  In the (default) case of a zero-buffered
 * synchronising CSP channel, this happens only when a process at the other
 * end of the channel invokes (or has already invoked) a <TT>read()</TT>.
 * <P>
 * <TT>ChannelOutputInt</TT> variables are used to hold integer channels
 * that are going to be used only for <I>output</I> by the declaring process.
 * This is a security matter -- by declaring a <TT>ChannelOutputInt</TT>
 * interface, any attempt to <I>input</I> from the channel will generate
 * a compile-time error.  For example, the following code fragment will
 * not compile:
 *
 * <PRE>
 * int doRead (ChannelOutputInt c) {
 *   return c.read ();   // illegal
 * }
 * </PRE>
 *
 * When configuring a <TT>CSProcess</TT> with output integer channels, they should
 * be declared as <TT>ChannelOutputInt</TT> variables.  The actual channel passed,
 * of course, may belong to <I>any</I> channel class that implements
 * <TT>ChannelOutputInt</TT>.
 *
 * <H2>Example</H2>
 * <PRE>
 * void doWrite (ChannelOutputInt c, int i) {
 *   c.write (i);
 * }
 * </PRE>
 *
 * @see org.jcsp.lang.ChannelInputInt
 * @author P.D. Austin
 */

public interface ChannelOutputInt extends Poisonable
{
    /**
     * Write an int to the channel.
     *
     * @param i the integer to write to the channel
     */
    public void write(int i);
}
