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

package org.jcsp.lang;

/**
 * <p>This interface should be implemented by classes that wish to
 * act as connection servers and to accept requests from
 * <code>ConnectionClient</code> objects.</p>
 *
 * <p>The server can call <code>request()</code> to allow a client
 * to establish a connection to the server and to obtain the client's
 * initial request. This should block until a client establishes a
 * connection.</p>
 *
 * <p>Once a request has been received, the server should reply to the client.
 * If the server wants to close the connection then the server should call
 * <code>replyAndClose(Object)</code> or alternatively
 * <code>reply(Object, boolean)</code> with the <code>boolean</code> set to
 * <code>true</code>. If the server wants to keep the connection open, then it
 * should call <code>reply(Object)</code> or alternatively
 * <code>reply(Object, boolean)</code> with the <code>boolean</code> set to
 * <code>false</code>.  The <code>reply(Object, boolean)</code> method is
 * provided for convenience in closing connections programatically.</p>
 *
 * @author Quickstone Technologies Limited
 */
public interface ConnectionServer
{
    /**
     * The factory for creating channels within servers.
     */
    static RiskyChannelFactory FACTORY = new RiskyChannelFactory();

    /**
     * <p>Receives a request from a client. This will block until the client
     * calls its <code>request(Object)</code> method. Implementations may
     * make this ALTable.</p>
     *
     * <p>After this method has returned, the server should call one of the
     * reply methods. Performing any external process synchronization
     * between these method calls could be potentially hazardous and could
     * lead to deadlock.</p>
     *
     * @return the <code>Object</code> sent by the client.
     */
    public Object request() throws IllegalStateException;

    /**
     * <p>Sends some data back to the client after a request
     * has been received but keeps the connection open. After calling
     * this method, the server should call <code>recieve()</code>
     * to receive a further request.</p>
     *
     * <p>A call to this method is equivalent to a call to
     * <code>reply(Object, boolean)</code> with the boolean set to
     * <code>false</code>.</p>
     *
     * @param	data	the data to send to the client.
     */
    public void reply(Object data) throws IllegalStateException;


    /**
     * <p>Sends some data back to the client after a request
     * has been received. The <code>boolean</code> close parameter
     * indicates whether the connection should be closed after this
     * reply has been sent.</p>
     *
     * <p>This method should not block.</p>
     *
     * @param data	  the data to send back to client.
     * @param close  <code>boolean</code> that should be <code>true</code>
     *                iff the connection should be dropped after the reply
     *                has been sent.
     */
    public void reply(Object data, boolean close);

    /**
     * <p>Sends some data back to the client and closes the connection.</p>
     *
     * <p>A call to this method is equivalent to a call to
     * <code>reply(Object, boolean)</code> with the boolean set to
     * <code>true</code>.</p>
     *
     * @param data	the data to send back to client.
     */
    public void replyAndClose(Object data) throws IllegalStateException;
}
