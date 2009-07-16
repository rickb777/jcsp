
  /*************************************************************************
  *                                                                        *
  *  JCSP ("CSP for Java") libraries                                       *
  *  Copyright (C) 1996-2001 Peter Welch and Paul Austin.                  *
  *                                                                        *
  *  This library is free software; you can redistribute it and/or         *
  *  modify it under the terms of the GNU Lesser General Public            *
  *  License as published by the Free Software Foundation; either          *
  *  version 2.1 of the License, or (at your option) any later version.    *
  *                                                                        *
  *  This library is distributed in the hope that it will be useful,       *
  *  but WITHOUT ANY WARRANTY; without even the implied warranty of        *
  *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU     *
  *  Lesser General Public License for more details.                       *
  *                                                                        *
  *  You should have received a copy of the GNU Lesser General Public      *
  *  License along with this library; if not, write to the Free Software   *
  *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307,  *
  *  USA.                                                                  *
  *                                                                        *
  *  Author contact: P.H.Welch@kent.ac.uk                                   *
  *                                                                        *
  *************************************************************************/

package org.jcsp.lang;

class AltingChannelOutputSymmetricImpl<T> extends AltingChannelOutput<T>
  implements MultiwaySynchronisation {

  private final AltingBarrier ab;

  private final ChannelOutput<T> out;

  private boolean syncDone = false;
  
  public AltingChannelOutputSymmetricImpl (
    AltingBarrier ab, ChannelOutput<T> out) {
    this.ab = ab;
    this.out = out;
  }

  boolean enable (Alternative alt) {
    syncDone = ab.enable (alt);
    return syncDone;
  }

  boolean disable () {
    syncDone = ab.disable ();
    return syncDone;
  }

  public void write (T o) {
    if (!syncDone) ab.sync ();
    syncDone = false;
    out.write (o);
  }

  public boolean pending () {
    syncDone = ab.poll (10);
    return syncDone;
  }

  public void poison(int strength) {
	out.poison(strength);
  }
  
  

}
