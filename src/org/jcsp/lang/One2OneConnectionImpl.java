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

import org.jcsp.util.Buffer;

/**
 * This class is an implementation of <code>One2OneConnection</code>.
 * Each end is safe to be used by one thread at a time.
 *
 * @author Quickstone Technologies Limited
 */
class One2OneConnectionImpl extends AbstractConnectionImpl implements One2OneConnection
{
    private AltingConnectionClient client;
    private AltingConnectionServer server;
    private One2OneChannel chanToServer;
    private One2OneChannel chanFromServer;

    /**
     * Initializes all the attributes to necessary values.
     * Channels are created using the static factory in the
     * <code>ChannelServer</code> inteface.
     *
     * Constructor for One2OneConnectionImpl.
     */
    public One2OneConnectionImpl()
    {
        super();
        One2OneChannel chanToServer = ConnectionServer.FACTORY.createOne2One(new Buffer(1));
        One2OneChannel chanFromServer = ConnectionServer.FACTORY.createOne2One(new Buffer(1));

        //create the client and server objects
        client = new AltingConnectionClientImpl(chanFromServer.in(),
                                                chanToServer.out(),
                                                chanToServer.out(),
                                                chanFromServer.out());
        server = new AltingConnectionServerImpl(chanToServer.in(),
                                                chanToServer.in());
    }

    /**
     * Returns the <code>AltingConnectionClient</code> that can
     * be used by a single process at any instance.
     *
     * This method will always return the same
     * <code>AltingConnectionClient</code> object.
     * <code>One2OneConnection</code> is only intendended to have two ends.
     *
     * @return the <code>AltingConnectionClient</code> object.
     */
    public AltingConnectionClient client()
    {
        return client;
    }

    /**
     * Returns the <code>AltingConnectionServer</code> that can
     * be used by a single process at any instance.
     *
     * This method will always return the same
     * <code>AltingConnectionServer</code> object.
     * <code>One2OneConnection</code> is only intendended to have two ends.
     *
     * @return the <code>AltingConnectionServer</code> object.
     */
    public AltingConnectionServer server()
    {
        return server;
    }
}
