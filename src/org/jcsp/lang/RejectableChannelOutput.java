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
 * Defines an interface for a channel output end which may reject data if the reader is not prepared
 * to receive it and calls <code>reject</code> instead of <code>read</code> on the input channel end.
 *
 * @author Quickstone Technologies Limited
 */
public interface RejectableChannelOutput extends ChannelOutput
{
    /**
     * Writes data over the channel.
     *
     * @param o an object to write over the channel.
     * @throws ChannelDataRejectedException if the reader rejects the data instead of reading it from the channel.
     */
    public void write(Object o) throws ChannelDataRejectedException;
}
