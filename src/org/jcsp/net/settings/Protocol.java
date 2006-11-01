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

package org.jcsp.net.settings;

/**
 * Used internally within the JCSP network infrastructure to represent a single protocol.
 *
 * @author Quickstone Technologies Limited
 */
public class Protocol
{
   public Protocol(String protocolID, String protocolName, Class idClass, int position)
   {
      this.protocolID = protocolID;
      this.protocolName = protocolName;
      this.idClass = idClass;
      this.position = position;
   }
   
   public void addSpec(Spec spec)
   {
      specs.addSpec(spec);
   }
   
   public void removeSpec(Spec spec)
   {
      specs.removeSpec(spec);
   }
   
   public Spec[] getSpecs()
   {
      return specs.getSpecs();
   }
   
   public void addSetting(Setting setting)
   {
      settings.addSetting(setting);
   }
   
   public void removeSetting(Setting setting)
   {
      settings.removeSetting(setting);
   }
   
   public Setting[] getSettings()
   {
      return settings.getSettings();
   }
   
   public Setting getSetting(String name)
   {
      return settings.getSetting(name);
   }
   
   public String getProtocolID()
   {
      return protocolID;
   }
   
   public String getName()
   {
      return protocolName;
   }
   
   public Class getIDClass()
   {
      return idClass;
   }
   
   public int getPosition()
   {
      return position;
   }
   
   public String toString()
   {
      StringBuffer sb = new StringBuffer();
      sb.append("<Protocol protocolID=\"" + protocolID + "\" name=\"" + protocolName  + "\" idClass=\"" + 
                idClass + "\" position=\"" + position + "\">\n");
      sb.append(JCSPConfig.tabIn(settings.toString())).append("\n");
      sb.append(JCSPConfig.tabIn(specs.toString())).append("\n");
      sb.append("</Protocol>");
      return sb.toString();
   }
   
   public boolean equals(Object o)
   {
      if(o instanceof Protocol)
      {
         Protocol other = (Protocol) o;
         return protocolID.equals(other.protocolID) && protocolName.equals(other.protocolName) 
                && idClass.equals(other.idClass) && specs.equals(other.specs);
      }
      return false;
   }
   
   public int hashCode()
   {
      return protocolName.hashCode();
   }
   
   private String protocolID;
   private String protocolName;
   private Class idClass;
   private int position;
   private Specs specs = new Specs();
   private Settings settings = new Settings();
}