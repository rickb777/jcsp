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

import org.jcsp.util.ChannelDataStore;
import org.jcsp.util.ints.*;

/**
 * This implements a one-to-one integer channel with user-definable buffering.
 * <H2>Description</H2>
 * <TT>BufferedOne2OneChannelIntImpl</TT> implements a one-to-one integer channel with
 * user-definable buffering.  Multiple readers or multiple writers are
 * not allowed -- these are catered for by {@link BufferedAny2OneChannel},
 * {@link BufferedOne2AnyChannel} or {@link BufferedAny2AnyChannel}.
 * <P>
 * The reading process may {@link Alternative <TT>ALT</TT>} on this channel.
 * The writing process is committed (i.e. it may not back off).
 * <P>
 * The constructor requires the user to provide
 * the channel with a <I>plug-in</I> driver conforming to the
 * {@link org.jcsp.util.ints.ChannelDataStoreInt <TT>ChannelDataStoreInt</TT>}
 * interface.  This allows a variety of different channel semantics to be
 * introduced -- including buffered channels of user-defined capacity
 * (including infinite), overwriting channels (with various overwriting
 * policies) etc..
 * Standard examples are given in the <TT>org.jcsp.util</TT> package, but
 * <I>careful users</I> may write their own.
 *
 * @see org.jcsp.lang.Alternative
 * @see org.jcsp.lang.BufferedAny2OneChannelIntImpl
 * @see org.jcsp.lang.BufferedOne2AnyChannelIntImpl
 * @see org.jcsp.lang.BufferedAny2AnyChannelIntImpl
 * @see org.jcsp.util.ints.ChannelDataStoreInt
 *
 * @author P.D.Austin
 * @author P.H.Welch
 */

class BufferedOne2OneChannelIntImpl extends AltingChannelInputInt implements One2OneChannelInt, ChannelOutputInt
{
  /** The monitor synchronising reader and writer on this channel */
  private Object rwMonitor = new Object();

  /** The Alternative class that controls the selection */
  private Alternative alt;
  
    /** The ChannelDataStoreInt used to store the data for the channel */
    private final ChannelDataStoreInt data;  
    
    /*************Methods from One2OneChannelInt******************************/

    /**
     * Returns the <code>AltingChannelInputInt</code> object to use for this
     * channel. As <code>One2OneChannelIntImpl</code> implements
     * <code>AltingChannelInputInt</code> itself, this method simply returns
     * a reference to the object that it is called on.
     *
     * @return the <code>AltingChannelInputInt</code> object to use for this
     *          channel.
     */
    public AltingChannelInputInt in()
    {
        return this;
    }

    /**
     * Returns the <code>ChannelOutputInt</code> object to use for this
     * channel. As <code>One2OneChannelIntImpl</code> implements
     * <code>ChannelOutputInt</code> itself, this method simply returns
     * a reference to the object that it is called on.
     *
     * @return the <code>ChannelOutputInt</code> object to use for this
     *          channel.
     */
    public ChannelOutputInt out()
    {
        return this;
    }

    /**
     * Constructs a new BufferedOne2OneChannelIntImpl with the specified ChannelDataStoreInt.
     *
     * @param data the ChannelDataStoreInt used to store the data for the channel
     */
    public BufferedOne2OneChannelIntImpl(ChannelDataStoreInt data)
    {
        if (data == null)
            throw new IllegalArgumentException
                    ("Null ChannelDataStoreInt given to channel constructor ...\n");
        this.data = (ChannelDataStoreInt) data.clone();
    }

    /**
     * Reads an <TT>int</TT> from the channel.
     *
     * @return the integer read from the channel.
     */
    public int read () {
      synchronized (rwMonitor) {
        if (data.getState () == ChannelDataStoreInt.EMPTY) {
          try {
            rwMonitor.wait ();
  	  while (data.getState () == ChannelDataStoreInt.EMPTY) {
  	    if (Spurious.logging) {
  	      SpuriousLog.record (SpuriousLog.One2OneChannelIntXRead);
  	    }
  	    rwMonitor.wait ();
  	  }
          }
          catch (InterruptedException e) {
            throw new ProcessInterruptedException (
              "*** Thrown from One2OneChannelInt.read (int)\n" + e.toString ()
            );
          }
        }
        rwMonitor.notify ();
        return data.get ();
      }
    }

    public int startRead() {
      synchronized (rwMonitor) {
        if (data.getState () == ChannelDataStore.EMPTY) {
          try {
            rwMonitor.wait ();
      while (data.getState () == ChannelDataStore.EMPTY) {
        if (Spurious.logging) {
          SpuriousLog.record (SpuriousLog.One2OneChannelXRead);
        }
        rwMonitor.wait ();
      }
          }
          catch (InterruptedException e) {
            throw new ProcessInterruptedException (
              "*** Thrown from One2OneChannel.read (int)\n" + e.toString ()
            );
          }
        }
        
        return data.startGet();
      }
    }
    
    public void endRead() {
      synchronized(rwMonitor) {
        data.endGet();
        rwMonitor.notify ();
      }
    }    
    
    /**
     * Writes an <TT>int</TT> to the channel.
     *
     * @param value the integer to write to the channel.
     */
    public void write (int value) {
      synchronized (rwMonitor) {
        data.put (value);
        if (alt != null) {
          alt.schedule ();
        } else {
          rwMonitor.notify ();
        }
        if (data.getState () == ChannelDataStoreInt.FULL) {
          try {
            rwMonitor.wait ();
  	  while (data.getState () == ChannelDataStoreInt.FULL) {
  	    if (Spurious.logging) {
  	      SpuriousLog.record (SpuriousLog.One2OneChannelIntXWrite);
  	    }
  	    rwMonitor.wait ();
  	  }
          }
          catch (InterruptedException e) {
            throw new ProcessInterruptedException (
              "*** Thrown from One2OneChannelInt.write (int)\n" + e.toString ()
            );
          }
        }
      }
    }

    /**
     * turns on Alternative selection for the channel. Returns true if the
     * channel has data that can be read immediately.
     * <P>
     * <I>Note: this method should only be called by the Alternative class</I>
     *
     * @param alt the Alternative class which will control the selection
     * @return true if the channel has data that can be read, else false
     */
    boolean enable (Alternative alt) {
      synchronized (rwMonitor) {
        if (data.getState () == ChannelDataStoreInt.EMPTY) {
          this.alt = alt;
          return false;
        }
        else {
          return true;
        }
      }
    }

    /**
     * turns off Alternative selection for the channel. Returns true if the
     * channel contained data that can be read.
     * <P>
     * <I>Note: this method should only be called by the Alternative class</I>
     *
     * @return true if the channel has data that can be read, else false
     */
    boolean disable () {
      synchronized (rwMonitor) {
        alt = null;
        return data.getState () != ChannelDataStoreInt.EMPTY;
      }
    }

    /**
     * Returns whether there is data pending on this channel.
     * <P>
     * <I>Note: if there is, it won't go away until you read it.  But if there
     * isn't, there may be some by the time you check the result of this method.</I>
     * <P>
     * This method is provided for convenience.  Its functionality can be provided
     * by <I>Pri Alting</I> the channel against a <TT>SKIP</TT> guard, although
     * at greater run-time and syntactic cost.  For example, the following code
     * fragment:
     * <PRE>
     *   if (c.pending ()) {
     *     int x = c.read ();
     *     ...  do something with x
     *   } else (
     *     ...  do something else
     *   }
     * </PRE>
     * is equivalent to:
     * <PRE>
     *   if (c_pending.priSelect () == 0) {
     *     int x = c.read ();
     *     ...  do something with x
     *   } else (
     *     ...  do something else
     * }
     * </PRE>
     * where earlier would have had to have been declared:
     * <PRE>
     * final Alternative c_pending =
     *   new Alternative (new Guard[] {c, new Skip ()});
     * </PRE>
     *
     * @return state of the channel.
     */
    public boolean pending () {
      synchronized (rwMonitor) {
        return (data.getState () != ChannelDataStoreInt.EMPTY);
      }
    }
}
