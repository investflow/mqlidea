//--------------------------------------------------------------------
// userindicator.mq4
// The code should be used for educational purpose only.
//--------------------------------------------------------------------
#property indicator_chart_window    // Indicator is drawn in the main window
#property indicator_buffers 2       // Number of buffers
#property indicator_color1 Blue     // Color of the 1st line
#property indicator_color2 Red      // Color of the 2nd line

double Buf_0[],Buf_1[];             // Declaring arrays (for indicator buffers)
//--------------------------------------------------------------------
int init()                          // Special function init()
  {
   SetIndexBuffer(0,Buf_0);         // Assigning an array to a buffer
   SetIndexStyle (0,DRAW_LINE,STYLE_SOLID,2);// Line style
   SetIndexBuffer(1,Buf_1);         // Assigning an array to a buffer
   SetIndexStyle (1,DRAW_LINE,STYLE_DOT,1);// Line style
   return;                          // Exit the special funct. init()
  }
//--------------------------------------------------------------------
int start()                         // Special function start()
  {
   int i,                           // Bar index
       Counted_bars;                // Number of counted bars
//--------------------------------------------------------------------
   Counted_bars=IndicatorCounted(); // Number of counted bars
   i=Bars-Counted_bars-1;           // Index of the first uncounted
   while(i>=0)                      // Loop for uncounted bars
     {
      Buf_0[i]=High[i];             // Value of 0 buffer on i bar
      Buf_1[i]=Low[i];              // Value of 1st buffer on i bar
      i--;                          // Calculating index of the next bar
     }
//--------------------------------------------------------------------
   return;                          // Exit the special funct. start()
  }
//--------------------------------------------------------------------
