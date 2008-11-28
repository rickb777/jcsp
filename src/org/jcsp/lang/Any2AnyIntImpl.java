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
    //////////////////////////////////////////////////////////////////////

package org.jcsp.lang;

class Any2AnyIntImpl implements Any2AnyChannelInt, ChannelInternalsInt {

	private ChannelInternalsInt channel;
	/** The mutex on which readers must synchronize */
    private final Mutex readMutex = new Mutex();
    private final Object writeMonitor = new Object();
    
    Any2AnyIntImpl(ChannelInternalsInt _channel) {
		channel = _channel;
	}
	
	public SharedChannelInputInt in() {
		return new SharedChannelInputIntImpl(this,0);
	}

	public SharedChannelOutputInt out() { 
		return new SharedChannelOutputIntImpl(this,0);
	}

	public void endRead() {
		channel.endRead();
		readMutex.release();

	}

	public int read() {
		readMutex.claim();
//		A poison exception might be thrown, hence the try/finally:		
		try
		{
			return channel.read();
		}
		finally
		{
			readMutex.release();		
		}		
	}

	//begin never used:
	public boolean readerDisable() {
		return false;
	}

	public boolean readerEnable(Alternative alt) {
		return false;
	}

	public boolean readerPending() {
		return false;
	}
	//end never used

	public void readerPoison(int strength) {
		readMutex.claim();
		channel.readerPoison(strength);
		readMutex.release();
	}

	public int startRead() {
		readMutex.claim();		
		try
		{
			return channel.startRead();
		}
		catch (RuntimeException e)
		{
			channel.endRead();
			readMutex.release();
			throw e;
		}
		
	}

	public void write(int n) {
		synchronized (writeMonitor) {
			channel.write(n);
		}		
	}

	public void writerPoison(int strength) {
		synchronized (writeMonitor) {		
			channel.writerPoison(strength);
		}
	}

}
