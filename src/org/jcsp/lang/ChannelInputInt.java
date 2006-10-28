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

package org.jcsp.lang;

/**
 * This defines the interface for reading from integer channels.
 * <H2>Description</H2>
 * <TT>ChannelInputInt</TT> defines the interface for reading from integer channels.
 * The interface contains only one method - <TT>read()</TT>.  This method
 * will block the calling process until an <TT>int</TT> has been written
 * to the channel by a process at the other end.  If an <TT>int</TT> has
 * already been written when this method is called, the method will return
 * without blocking.  Either way, the method returns the <TT>int</TT>
 * sent down the channel.
 * <P>
 * <TT>ChannelInputInt</TT> variables are used to hold integer channels
 * that are going to be used only for <I>input</I> by the declaring process.
 * This is a security matter -- by declaring a <TT>ChannelInputInt</TT>
 * interface, any attempt to <I>output</I> to the channel will generate
 * a compile-time error.  For example, the following code fragment will
 * not compile:
 *
 * <PRE>
 * void doWrite (ChannelInputInt c, int i) {
 *   c.write (i);   // illegal
 * }
 * </PRE>
 *
 * When configuring a <TT>CSProcess</TT> with input integer channels, they should
 * be declared as <TT>ChannelInputInt</TT> (or, if we wish to be able to make
 * choices between events, as <TT>AltingChannelInputInt</TT>)
 * variables.  The actual channel passed,
 * of course, may belong to <I>any</I> channel class that implements
 * <TT>ChannelInputInt</TT> (or <TT>AltingChannelInputInt</TT>).
 * <H2>Example</H2>
 * <H3>Discard data</H3>
 * <PRE>
 * void doRead (ChannelInputInt c) {
 *   c.read ();                       // clear the channel
 * }
 * </PRE>
 *
 * @see org.jcsp.lang.AltingChannelInputInt
 * @see org.jcsp.lang.ChannelOutputInt
 * @author P.D.Austin
 */

public interface ChannelInputInt
{
    /**
     * Read an <TT>int</TT> from the channel.
     *
     * @return the integer read from the channel
     */
    public int read();
}
