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
    //                                                                  //
    //////////////////////////////////////////////////////////////////////


import org.jcsp.lang.*;
import org.jcsp.awt.*;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

/**
 * @author P.H. Welch
 */
public class Infection implements CSProcess {

  private final int N_EVOLVERS = 8;

  private int renderRate;        // invariant: 0 <= renderRate <= 100
  private int renderEvery;       // invariant: (renderRate == 0) ? Integer.MAX_VALUE : 100/renderRate

  private int infectRate;        // invariant: 0 <= infectRate <= 100 (or 127)
  private int convertRate;       // invariant: 0 <= convertRate <= 100 (or 127)
  private int recoverRate;       // invariant: 0 <= recoverRate <= 100 (or 127)
  private int reinfectRate;      // invariant: 0 <= reinfectRate <= 100 (or 127)

  private int sprayRadius;

  private CSTimer tim = new CSTimer ();     // frame-rate calculation fields
  private long firstFrameTime;
  private int nFrames = 0;
  private int fpsUpdate = 1;
  
  private final AltingChannelInput fromMouse;
  private final AltingChannelInput fromMouseMotion;

  private final AltingChannelInput resetEvent;
  private final ChannelOutput resetConfigure;

  private final AltingChannelInput freezeEvent;
  private final ChannelOutput freezeConfigure;

  private final AltingChannelInputInt renderRateBarEvent;
  private final ChannelOutput renderRateBarConfigure;

  private final AltingChannelInputInt infectRateBarEvent;
  private final ChannelOutput infectRateBarConfigure;

  private final AltingChannelInputInt convertRateBarEvent;
  private final ChannelOutput convertRateBarConfigure;
  
  private final AltingChannelInputInt recoverRateBarEvent;
  private final ChannelOutput recoverRateBarConfigure;
  
  private final ChannelOutput fpsConfigure;
  private final ChannelOutput infectedConfigure;
  private final ChannelOutput deadConfigure;
  
  private final ChannelOutput renderRateLabelConfigure;
  private final ChannelOutput infectRateLabelConfigure;
  private final ChannelOutput convertRateLabelConfigure;
  private final ChannelOutput recoverRateLabelConfigure;
  
  private final ChannelOutput toGraphics;
  private final ChannelInput fromGraphics;

  public Infection (final int infectRate,
                    final AltingChannelInput fromMouse,
                    final AltingChannelInput fromMouseMotion,
                    final AltingChannelInput resetEvent,
                    final ChannelOutput resetConfigure,
                    final AltingChannelInput freezeEvent,
                    final ChannelOutput freezeConfigure,
                    final AltingChannelInputInt renderRateBarEvent,
                    final ChannelOutput renderRateBarConfigure,
                    final AltingChannelInputInt infectRateBarEvent,
                    final ChannelOutput infectRateBarConfigure,
                    final AltingChannelInputInt convertRateBarEvent,
                    final ChannelOutput convertRateBarConfigure,
                    final AltingChannelInputInt recoverRateBarEvent,
                    final ChannelOutput recoverRateBarConfigure,
                    final ChannelOutput fpsConfigure,
                    final ChannelOutput infectedConfigure,
                    final ChannelOutput deadConfigure,
                    final ChannelOutput renderRateLabelConfigure,
                    final ChannelOutput infectRateLabelConfigure,
                    final ChannelOutput convertRateLabelConfigure,
                    final ChannelOutput recoverRateLabelConfigure,
                    final ChannelOutput toGraphics,
                    final ChannelInput fromGraphics) {

    this.renderRate = 100;
    this.renderEvery = (renderRate == 0) ? Integer.MAX_VALUE : 100/renderRate;
    this.infectRate = infectRate;
    this.convertRate = 80;
    this.recoverRate = 99;
    this.reinfectRate = 10;
    this.sprayRadius = 20;
    this.fromMouse = fromMouse;
    this.fromMouseMotion = fromMouseMotion;
    this.resetEvent = resetEvent;
    this.resetConfigure = resetConfigure;
    this.freezeEvent = freezeEvent;
    this.freezeConfigure = freezeConfigure;
    this.renderRateBarEvent = renderRateBarEvent;
    this.renderRateBarConfigure = renderRateBarConfigure;
    this.infectRateBarEvent = infectRateBarEvent;
    this.infectRateBarConfigure = infectRateBarConfigure;
    this.convertRateBarEvent = convertRateBarEvent;
    this.convertRateBarConfigure = convertRateBarConfigure;
    this.recoverRateBarEvent = recoverRateBarEvent;
    this.recoverRateBarConfigure = recoverRateBarConfigure;
    this.fpsConfigure = fpsConfigure;
    this.infectedConfigure = infectedConfigure;
    this.deadConfigure = deadConfigure;
    this.renderRateLabelConfigure = renderRateLabelConfigure;
    this.infectRateLabelConfigure = infectRateLabelConfigure;
    this.convertRateLabelConfigure = convertRateLabelConfigure;
    this.recoverRateLabelConfigure = recoverRateLabelConfigure;
    this.toGraphics = toGraphics;
    this.fromGraphics = fromGraphics;
  }

  //     colours          :       Cell.GREEN    Cell.INFECTED    Cell.DEAD
  //     -------                  ----------    -------------    ---------
  
  private final byte[] reds   = { (byte)0x00,    (byte)0xff,    (byte)0x00};
  private final byte[] greens = { (byte)0xff,    (byte)0x00,    (byte)0x00};
  private final byte[] blues  = { (byte)0x00,    (byte)0x00,    (byte)0xff};

  //     pixel array and key run-time parameters
  //     ---------------------------------------

  private byte[] pixels;                     // pixel array of Cell matrix

  private byte[][] cell, last_cell;          // matrix of Cells (plus spare)

  // Note: we will maintain in last_cell the previous state of the Cells.

  private int width, height;

  private int[] count = new int[Cell.N_STATES];  // how many in each cell state

  private final static int IDLE = 0, RUNNING = 2, FROZEN = 3, RESET = 4;

  //  IDLE     <==>  "reset"  "FREEZE"    all green           not running
  //  RUNNING  <==>  "reset"  "FREEZE"    some infected/dead  running
  //  FROZEN   <==>  "RESET"  "UNFREEZE"  some infected/dead  not running
  //  RESET    <==>  "reset"  "UNFREEZE"  all green           not running
    
  private int state = IDLE;

  private final static int RESET_EVENT = 0, FREEZE_EVENT = 1;
  private final static int RENDER_RATE = 2, INFECT_RATE = 3, CONVERT_RATE = 4, RECOVER_RATE = 5;
  private final static int MOUSE = 6, MOUSE_MOTION = 7, SKIP = 8;

  private Guard[] guard;
                           
  private boolean[] preCondition;
  
  private Spray spray;

  //     private methods
  //     -----------------

  private ColorModel createColorModel () {
    return new IndexColorModel (2, 3, reds, greens, blues);
  }

  private final Rand random = new Rand ();

  private void initialisePixels () {
    for (int ij = 0; ij < pixels.length; ij++) {
      pixels[ij] = Cell.GREEN;
    }
    for (int j = 0; j < height; j++) {
      for (int i = 0; i < width; i++) {
        cell[j][i] = Cell.GREEN;
      }
    }
    count[Cell.GREEN] = height*width;
    count[Cell.INFECTED] = 0;
    count[Cell.DEAD] = 0;
    infectedConfigure.write (String.valueOf (count[Cell.INFECTED]));
    deadConfigure.write (String.valueOf (count[Cell.DEAD]));
  }

  private void pixelise () {
    int i0 = 0;
    for (int i = 0; i < cell.length; i++) {
      System.arraycopy (cell[i], 0, pixels, i0, width);
      i0 = i0 + width;
    }
  }

  private void byteMatrixCopy (final byte[][] from, final byte[][] to) {
    // assume: from and to are equally sized ...
    for (int i = 0; i < from.length; i++) {
      System.arraycopy (from[i], 0, to[i], 0, from[i].length);  // fast copy
    }
  }

  private void infect (int i, int j) {     // possibly infect Cell[i][j]
    i = (i < 0) ? i + height : (i >= height) ? i - height : i;
    j = (j < 0) ? j + width : (j >= width) ? j - width : j;
    if (last_cell[i][j] == Cell.GREEN) {
      if (random.bits7 () < infectRate) {
        count[cell[i][j]]--;
        cell[i][j] = Cell.INFECTED;
        count[Cell.INFECTED]++;
      }
    }
  }

  // the following method has been replaced by the Evolve process
  // (instances of which are run in parallel)

  private void evolve () {      // evolves the forest forward forward one cycle
    for (int i = 0; i < height; i++) {
      final byte[] last_row_i = last_cell[i];
      final byte[] row_i = cell[i];
      for (int j = 0; j < width; j++) {
        switch (last_row_i[j]) {
          // case Cell.GREEN:
          // break;
          case Cell.INFECTED:
            infect (i + 1, j);
            infect (i - 1, j);
            infect (i, j + 1);
            infect (i, j - 1);
            if ((i % 2) == 0) {
              infect (i + 1, j + 1);
              infect (i - 1, j - 1);
            } else {
              infect (i - 1, j + 1);
              infect (i + 1, j - 1);
            }
            if (random.bits7 () < convertRate) {
              row_i[j] = Cell.DEAD;
              count[Cell.INFECTED]--;
              count[Cell.DEAD]++;
            }
          break;
          case Cell.DEAD:
            if (random.bits7 () < recoverRate) {
              if (random.bits16 () < reinfectRate) {
                row_i[j] = Cell.INFECTED;
                count[Cell.DEAD]--;
                count[Cell.INFECTED]++;
              } else {
                row_i[j] = Cell.GREEN;
                count[Cell.DEAD]--;
                count[Cell.GREEN]++;
              }
            }
          break;
        }
      }
    }
    pixelise ();
  }

  private void handle (final Point point, final byte newCellState,
                       final boolean spraying) {
    if (spraying) {
      System.out.println ("Spraying ..." + point);
      spray.zap (point, newCellState);
    } else {
      System.out.print ("Spotting ..." + point + " ... (");
      int i = point.y;
      int j = point.x;
      while (i < 0) i += height;            // mostly won't happen or
      while (i >= height) i -= height;      // will happen only once.
      while (j < 0) j += width;             //         ditto.
      while (j >= width) j -= width;        //         ditto.
      System.out.println (j + ", " + i + ")");
      // if ((0 <= i) && (i < height) && (0 <= j) && (j < width)) {
      byte[] cellRow = cell[i];
      final byte current = cellRow[j];
      if (current != Cell.INFECTED){
        pixels[(i*width) + j] = newCellState;
        count[current]--;
        cellRow[j] = newCellState;
        count[newCellState]++;
      }
      // }
    }
    final int notGreen = count[Cell.INFECTED] + count[Cell.DEAD];
    infectedConfigure.write (String.valueOf (count[Cell.INFECTED]));
    deadConfigure.write (String.valueOf (count[Cell.DEAD]));
    switch (state) {
      case IDLE:
        if (notGreen > 0) {
          preCondition[SKIP] = true;
          nFrames = 0;
          fpsUpdate = 1;
          firstFrameTime = tim.read ();
          state = RUNNING;
        }
      break;
      case RUNNING:
        if (notGreen == 0) {
          preCondition[SKIP] = false;
          state = IDLE;
        }
      break;
      case FROZEN:
        if (notGreen == 0) {
          resetConfigure.write (Boolean.FALSE);
          resetConfigure.write ("reset");
          preCondition[RESET_EVENT] = false;
          state = RESET;
        }
      break;
      case RESET:
        if (notGreen > 0) {
          while (resetEvent.pending ()) resetEvent.read ();
          resetConfigure.write (Boolean.TRUE);
          resetConfigure.write ("RESET");
          preCondition[RESET_EVENT] = true;
          state = FROZEN;
        }
      break;
    }
  }

  public void run () {

    fpsConfigure.write ("0");
    infectedConfigure.write ("0");
    deadConfigure.write ("0");
    
    renderRateLabelConfigure.write (String.valueOf (renderRate));
    infectRateLabelConfigure.write (String.valueOf (infectRate));
    convertRateLabelConfigure.write (String.valueOf (convertRate));
    recoverRateLabelConfigure.write (String.valueOf (recoverRate));
    
    infectRate = ((infectRate*128) + 64)/100;
    convertRate = ((convertRate*128) + 64)/100;
    recoverRate = ((recoverRate*128) + 64)/100;
    
    convertRate = 128 - convertRate;
    recoverRate = 128 - recoverRate;
    
    resetConfigure.write (Boolean.FALSE);
    resetConfigure.write ("reset");
    
    freezeConfigure.write (Boolean.TRUE);
    freezeConfigure.write ("FREEZE");
    
    renderRateBarConfigure.write (Boolean.TRUE);
    infectRateBarConfigure.write (Boolean.TRUE);
    convertRateBarConfigure.write (Boolean.TRUE);
    recoverRateBarConfigure.write (Boolean.TRUE);

    toGraphics.write (GraphicsProtocol.GET_DIMENSION);
    final Dimension graphicsDim = (Dimension) fromGraphics.read ();
    System.out.println ("Infection: graphics dimension = " + graphicsDim);

    width = graphicsDim.width;
    height = graphicsDim.height;

    pixels = new byte[width*height];

    cell = new byte[height][width];
    last_cell = new byte[height][width];

    final ColorModel model = createColorModel ();

    final MemoryImageSource mis =
      new MemoryImageSource (width, height, model, pixels, 0, width);
    mis.setAnimated (true);
    mis.setFullBufferUpdates (true);

    toGraphics.write (new GraphicsProtocol.MakeMISImage (mis));
    final Image image = (Image) fromGraphics.read ();

    final DisplayList display = new DisplayList ();
    toGraphics.write (new GraphicsProtocol.SetPaintable (display));
    fromGraphics.read ();

    final GraphicsCommand[] drawImage = {new GraphicsCommand.DrawImage (image, 0, 0)};
    display.set (drawImage);

    final Thread me = Thread.currentThread ();
    System.out.println ("Infection priority = " + me.getPriority ());
    me.setPriority (Thread.MIN_PRIORITY);
    System.out.println ("Infection priority = " + me.getPriority ());

    guard = new Guard[] {resetEvent, freezeEvent,
                         renderRateBarEvent, infectRateBarEvent, convertRateBarEvent,
                         recoverRateBarEvent, fromMouse, fromMouseMotion, new Skip ()};
                           
    preCondition = new boolean[] {false, true, true, true, true, true, true, false, false};

    boolean spraying = false;
    boolean controlled = false;
    boolean mousePressed = false;
    byte newCellState = Cell.GREEN;

    spray = new Spray (sprayRadius, cell, pixels, count);

    final Alternative alt = new Alternative (guard);

    initialisePixels ();
    mis.newPixels ();

    int cycle = renderEvery;

    Evolve[] evolvers = new Evolve[N_EVOLVERS];
    final int nRows = height/N_EVOLVERS;
    long seed = System.currentTimeMillis ();
    for (int i = 0; i < evolvers.length; i++) {
      evolvers[i] = new Evolve (i*nRows, nRows, cell, last_cell, pixels, seed + (i*1000));
      evolvers[i].infectRate = infectRate;
      evolvers[i].recoverRate = recoverRate;
      evolvers[i].reinfectRate = reinfectRate;
      evolvers[i].convertRate = convertRate;
    }

    CSProcess parEvolve = new Parallel (evolvers);

    while (true) {
      switch (alt.fairSelect (preCondition)) {
        case RESET_EVENT:
          System.out.println ("Infection: reset event ...");
          resetEvent.read ();
          // assert : state == FROZEN
          resetConfigure.write (Boolean.FALSE);
          resetConfigure.write ("reset");
          preCondition[RESET_EVENT] = false;
          state = RESET;
          System.out.println ("Infection: reset");
          initialisePixels ();
          mis.newPixels ();
        break;
        case FREEZE_EVENT:
          freezeEvent.read ();
          switch (state) {
            case IDLE:
              System.out.println ("Infection: freeze");
              freezeConfigure.write ("UNFREEZE");
              state = RESET;
            break;
            case RUNNING:
              System.out.println ("Infection: freeze");
              freezeConfigure.write ("UNFREEZE");
              while (resetEvent.pending ()) resetEvent.read ();
              resetConfigure.write (Boolean.TRUE);
              resetConfigure.write ("RESET");
              preCondition[RESET_EVENT] = true;
              preCondition[SKIP] = false;
              state = FROZEN;
            break;
            case FROZEN:
              System.out.println ("Infection: unfreeze");
              freezeConfigure.write ("FREEZE");
              resetConfigure.write (Boolean.FALSE);
              resetConfigure.write ("reset");
              preCondition[RESET_EVENT] = false;
              preCondition[SKIP] = true;
              nFrames = 0;
              fpsUpdate = 1;
              firstFrameTime = tim.read ();
              state = RUNNING;
            break;
            case RESET:
              System.out.println ("Infection: unfreeze");
              freezeConfigure.write ("FREEZE");
              state = IDLE;
            break;
          }
        break;
        case RENDER_RATE:
          renderRate = 100 - renderRateBarEvent.read ();
          renderEvery = (renderRate == 0) ? Integer.MAX_VALUE : 100/renderRate;
          cycle = renderEvery;
          renderRateLabelConfigure.write (String.valueOf (renderRate));
        break;
        case INFECT_RATE:
          infectRate = 100 - infectRateBarEvent.read ();
          infectRateLabelConfigure.write (String.valueOf (infectRate));
          infectRate = ((infectRate*128) + 64)/100;
          for (int i = 0; i < evolvers.length; i++) {
            evolvers[i].infectRate = infectRate;
          }
        break;
        case CONVERT_RATE:
          convertRate = 100 - convertRateBarEvent.read ();
          convertRateLabelConfigure.write (String.valueOf (convertRate));
          convertRate = ((convertRate*128) + 64)/100;
          convertRate = 128 - convertRate;
          for (int i = 0; i < evolvers.length; i++) {
            evolvers[i].convertRate = convertRate;
          }
        break;
        case RECOVER_RATE:
          recoverRate = 100 - recoverRateBarEvent.read ();
          recoverRateLabelConfigure.write (String.valueOf (recoverRate));
          recoverRate = ((recoverRate*128) + 64)/100;
          recoverRate = 128 - recoverRate;
          for (int i = 0; i < evolvers.length; i++) {
            evolvers[i].recoverRate = recoverRate;
          }
        break;
        case MOUSE:
          final MouseEvent event = (MouseEvent) fromMouse.read ();
          switch (event.getID ()) {
            case MouseEvent.MOUSE_PRESSED:
              if (controlled) {
                controlled = false;
                preCondition[MOUSE_MOTION] = false;
                if (spraying) {
                  spray.setMask ();
                  spraying = false;
                }
              } else {
                mousePressed = true;
                while (fromMouseMotion.pending ()) fromMouseMotion.read ();
                preCondition[MOUSE_MOTION] = true;
                int modifiers = event.getModifiers ();
                if ((modifiers & InputEvent.BUTTON1_MASK) != 0) {
                  newCellState = Cell.INFECTED;
                } else if ((modifiers & InputEvent.BUTTON2_MASK) != 0) {
                  newCellState = Cell.GREEN;
                } else {
                  newCellState = Cell.DEAD;
                }
                // System.out.println ("MOUSE_PRESSED modifiers = " + modifiers);
                // System.out.println ("SHIFT_MASK = " + InputEvent.SHIFT_MASK);
                // System.out.println ("CTRL_MASK = " + InputEvent.CTRL_MASK);
                // System.out.println ("META_MASK = " + InputEvent.META_MASK);
                // System.out.println ("ALT_MASK = " + InputEvent.ALT_MASK);
                // System.out.println ("BUTTON1_MASK = " + InputEvent.BUTTON1_MASK);
                // System.out.println ("BUTTON2_MASK = " + InputEvent.BUTTON2_MASK);
                // System.out.println ("BUTTON3_MASK = " + InputEvent.BUTTON3_MASK);
                spraying = ((modifiers & InputEvent.SHIFT_MASK) != 0);
                if (newCellState != Cell.GREEN) {  // CTRL_MASK doesn't work on BUTTON2 (JDK1.1/2/3)
                  controlled = ((modifiers & InputEvent.CTRL_MASK) != 0);
                }
                handle (event.getPoint (), newCellState, spraying);
                mis.newPixels ();
              }
            break;
            case MouseEvent.MOUSE_RELEASED:
              mousePressed = false;
              if (! controlled) {
                preCondition[MOUSE_MOTION] = false;
                if (spraying) {
                  spray.setMask ();
                  spraying = false;
                }
              }
            break;
          }
        break;
        case MOUSE_MOTION:
          final MouseEvent motion = (MouseEvent) fromMouseMotion.read ();
          switch (motion.getID ()) {
            case MouseEvent.MOUSE_MOVED:
              if (controlled) {
                handle (motion.getPoint (), newCellState, spraying);
                mis.newPixels ();
              }
            case MouseEvent.MOUSE_DRAGGED:
              if (mousePressed) {
                handle (motion.getPoint (), newCellState, spraying);
                mis.newPixels ();
              }
            break;
          }
        break;
        case SKIP:
          // assert : state == RUNNING
          byteMatrixCopy (cell, last_cell);
          // evolve ();          // sequential version (instead of next 6 lines)
          parEvolve.run ();
          for (int i = 0; i < evolvers.length; i++) {
            for (int j = 0; j < count.length; j++) {
              count[j] += evolvers[i].count[j];
            }
          }
          cycle--;
          if (cycle == 0) {
            mis.newPixels ();
            cycle = renderEvery;
          }
          nFrames++;
          if (nFrames == fpsUpdate) {
	    final long thisFrameTime = tim.read ();
            final int period = (int) (thisFrameTime - firstFrameTime);
            int framesPerTenSeconds = (period == 0) ? 0 : (nFrames*10000) / period;
            fpsConfigure.write (framesPerTenSeconds/10 + "." + framesPerTenSeconds%10);
            fpsUpdate = framesPerTenSeconds/20;
            if (fpsUpdate == 0) fpsUpdate = 1;
	    firstFrameTime = thisFrameTime;
	    nFrames = 0;
            infectedConfigure.write (String.valueOf (count[Cell.INFECTED]));
            deadConfigure.write (String.valueOf (count[Cell.DEAD]));
          }

          final int notGreen = count[Cell.INFECTED] + count[Cell.DEAD];
          if (notGreen == 0) {
            preCondition[SKIP] = false;   // no infection and no dead cells => stop computing!
            state = IDLE;
          }
        break;
      }  
    }

  }

}
