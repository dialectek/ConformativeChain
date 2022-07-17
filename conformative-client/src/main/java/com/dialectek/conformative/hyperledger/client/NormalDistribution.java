// Normal Distribution.

package com.dialectek.conformative.hyperledger.client;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
import java.util.Random;

public class NormalDistribution
{
   public static final int      CANVAS_WIDTH       = 300;
   public static final int      CANVAS_HEIGHT      = 200;
   public static final int      CANVAS_BORDER      = 30;
   public static final Color    GRAPH_LINE_COLOR  = Color.BLUE;   
   public static final Color    GRAPH_POINT_COLOR  = Color.RED;
   public static final int      GRAPH_POINT_WIDTH  = 4;
   public static final int      NUM_PLOT_INTERVALS = 20;
   public static final int      X_NUM_FREQUENCY    = 5;
   public static final int      Y_NUM_FREQUENCY    = 5;
   public static final int      NUM_SIGMAS         = 3;
   public static final double   DEFAULT_MEAN       = 10.0;
   public static final double   DEFAULT_SIGMA      = 2.0;
   private Canvas               canvas;
   private double               mean;
   private double               sigma;
   private Random               random;
   private Graphics2D graphics;
   
   // Constructors.
   public NormalDistribution(Canvas canvas, double mean, double sigma)
   {
      this.canvas = canvas;
      this.mean   = mean;
      this.sigma  = sigma;
      random      = new Random();
      graphics      = (Graphics2D)canvas.getGraphics();     
   }


   public NormalDistribution(Canvas canvas)
   {
      this.canvas = canvas;
      mean        = DEFAULT_MEAN;
      sigma       = DEFAULT_SIGMA;
      random      = new Random();
      graphics      = (Graphics2D)canvas.getGraphics();       
   }


   public double getMean()
   {
      return(mean);
   }


   public void setMean(double mean)
   {
      this.mean = mean;
   }


   public double getSigma()
   {
      return(sigma);
   }


   public void setSigma(double sigma)
   {
      this.sigma = sigma;
   }


   // Get next value from distribution.
   public double nextValue()
   {
      double result = (random.nextGaussian() * sigma) + mean;

      if (result < 0.0)
      {
         result = 0.0;
      }
      return(result);
   }


   // Get probability value for x.
   public double phi(double x)
   {
      double d = x - mean;

      return(Math.exp(-(d * d) / (2.0 * sigma * sigma)) / (sigma * Math.sqrt(2.0 * Math.PI)));
   }


   // Draw.
   public void draw()
   {
      int x0, y0, x1, y1, x2, y2;

      if (canvas == null) { return; }
      canvas.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
      
      double xLow = mean - ((double)NUM_SIGMAS * sigma);
      if (xLow < 0.0)
      {
         xLow = 0.0;
      }
      double xHigh     = mean + ((double)NUM_SIGMAS * sigma);
      double xInterval = (xHigh - xLow) / (double)NUM_PLOT_INTERVALS;
      double yHigh     = phi(mean);
      double yInterval = yHigh / (double)NUM_PLOT_INTERVALS;

      double xScale = ((double)CANVAS_WIDTH - 2 * CANVAS_BORDER) / NUM_PLOT_INTERVALS;
      double yScale = ((double)CANVAS_HEIGHT - 2 * CANVAS_BORDER) / NUM_PLOT_INTERVALS;

      double[] graphXcoords = new double[NUM_PLOT_INTERVALS + 1];
      int[] graphXpoints    = new int[NUM_PLOT_INTERVALS + 1];
      int[] graphYpoints    = new int[NUM_PLOT_INTERVALS + 1];
      
      FontMetrics fontMetrics = graphics.getFontMetrics();
      int charWidth = fontMetrics.charWidth('0');
      
      for (int i = 0; i <= NUM_PLOT_INTERVALS; i++)
      {
         double x = xLow + ((double)i * xInterval);
         graphXcoords[i] = x;
         double y = phi(x);
         x1 = (int)((double)i * xScale + CANVAS_BORDER);
         y1 = (int)(((yHigh - y) / yInterval) * yScale + CANVAS_BORDER);
         graphXpoints[i] = x1;
         graphYpoints[i] = y1;
      }

      // Draw x and y axes.
      graphics.setColor(GRAPH_LINE_COLOR);
      graphics.drawLine(CANVAS_BORDER, CANVAS_HEIGHT - CANVAS_BORDER, CANVAS_BORDER, CANVAS_BORDER);
      graphics.drawLine(CANVAS_BORDER, CANVAS_HEIGHT - CANVAS_BORDER, CANVAS_WIDTH - CANVAS_BORDER, CANVAS_HEIGHT - CANVAS_BORDER);
      
      // Draw intervals for y axis.
      DecimalFormat decimalFormat = new DecimalFormat(".##");
      int          borderGap34   = (3 * CANVAS_BORDER) / 4;
      x0 = CANVAS_BORDER;
      x1 = GRAPH_POINT_WIDTH + CANVAS_BORDER;
      
      int offset = (int)((float)charWidth / 2.0f);
      for (int i = 0; i <= NUM_PLOT_INTERVALS; i++)
      {
         y0 = CANVAS_HEIGHT - ((i * (CANVAS_HEIGHT - CANVAS_BORDER * 2)) / NUM_PLOT_INTERVALS + CANVAS_BORDER);
         y1 = y0;
         graphics.drawLine(x0, y0, x1, y1);
         if ((i % Y_NUM_FREQUENCY) == 0)
         {
            graphics.drawString(decimalFormat.format((double)i * yInterval), x0 - borderGap34, y0 + offset);
         }
      }

      // Draw intervals x axis
      int borderGap2 = CANVAS_BORDER / 2;
      y0 = CANVAS_HEIGHT - CANVAS_BORDER;
      y1 = y0 - GRAPH_POINT_WIDTH;
      for (int i = 0; i <= NUM_PLOT_INTERVALS; i++)
      {
         x0 = i * (CANVAS_WIDTH - CANVAS_BORDER * 2) / NUM_PLOT_INTERVALS + CANVAS_BORDER;
         x1 = x0;
         graphics.drawLine(x0, y0, x1, y1);
         if ((i % X_NUM_FREQUENCY) == 0)
         {
            String      n       = decimalFormat.format(graphXcoords[i]);
            int width = fontMetrics.stringWidth(n);
            graphics.drawString(n, x0 - (int)((float)width / 2.0f), y0 + borderGap2);
         }
      }

      // Draw lines.
      for (int i = 0; i < NUM_PLOT_INTERVALS; i++)
      {
         x1 = graphXpoints[i];
         y1 = graphYpoints[i];
         x2 = graphXpoints[i + 1];
         y2 = graphYpoints[i + 1];
         graphics.drawLine(x1, y1, x2, y2);
      }

      // Draw points.
      graphics.setColor(GRAPH_POINT_COLOR);
      for (int i = 0; i <= NUM_PLOT_INTERVALS; i++)
      {
         int x = graphXpoints[i];
         int y = graphYpoints[i];
         graphics.drawLine(x, y, x, y);
      }
   }
}
