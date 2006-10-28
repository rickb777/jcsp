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
 * Defines a wrapper to go around a channel input end. This wrapper allows a channel end to be given
 * away without any risk of the user of that end casting it to a channel output because they cannot
 * gain access to the actual channel end.
 *
 * @author Quickstone Technologies Limited
 */
public class ChannelInputIntWrapper implements ChannelInputInt
{
    /**
     * The actual channel end.
     */
    private ChannelInputInt in;

    /**
     * Constructs a new wrapper around the given channel end.
     *
     * @param in the existing channel end.
     */
    public ChannelInputIntWrapper(ChannelInputInt in)
    {
        this.in = in;
    }

    /**
     * Reads a value from the channel.
     *
     * @see org.jcsp.lang.ChannelInputInt
     * @return the value read.
     */
    public int read()
    {
        return in.read();
    }
}
