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
 * This wraps up a Any2OneChannelInt object so that its
 * input and output ends are separate objects.
 *
 * @author Quickstone Technologies Limited
 */
class SafeAny2OneChannelInt implements Any2OneChannelInt
{
    /** The input end. */
    private AltingChannelInputInt in;
    /** The output end. */
    private SharedChannelOutputInt out;

    /**
     * Constructs a new wrapper around the given channel.
     *
     * @param chan the existing channel.
     */
    SafeAny2OneChannelInt(Any2OneChannelInt chan)
    {
        in = new AltingChannelInputIntWrapper(chan.in());
        out = new SharedChannelOutputIntWrapper(chan.out());
    }

    public AltingChannelInputInt in()
    {
        return in;
    }

    public SharedChannelOutputInt out()
    {
        return out;
    }
}
