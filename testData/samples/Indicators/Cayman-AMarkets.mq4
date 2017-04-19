#property copyright "Andrey Sitaev"
#property link      "http://www.aforex.ru"
#property version   "1.00"
#property strict
#property indicator_separate_window
#property indicator_minimum 0
#property indicator_maximum 100
#property indicator_buffers 1
#property indicator_plots   1
// plot Cayman
#property indicator_label1  "Cayman"
#property indicator_type1   DRAW_LINE
#property indicator_color1  clrOliveDrab
#property indicator_style1  STYLE_SOLID
#property indicator_width1  1
// indicator buffers
double         CaymanBuffer[];

int            MaxBars = 1000;   
string         caymanSymbol = "";

int OnInit()
{
   FindCaymanSymbol();
   SetIndexBuffer(0, CaymanBuffer);
   return (INIT_SUCCEEDED);
}

int start()
{
   if (StringLen(caymanSymbol) == 0) return (0);

	int lastBarToCalc = IntPutInRange(Bars - IndicatorCounted() - 1, 0, INT_MAX);
	if (lastBarToCalc < 0)
		lastBarToCalc = 0;
	if (MaxBars > 0)
		lastBarToCalc = MathMin(lastBarToCalc, MaxBars - 1);
		
   MqlRates rates[];
   ArraySetAsSeries(rates, true);
   int copied = CopyRates(caymanSymbol, 0, 0, MaxBars, rates);
   if (copied == 0) 
   {
      Print("история Каймана не прочитана. Попробуйте открыть график " + caymanSymbol);
      return (0);
   }

   //PrintFormat("Found %d symbols of %s", copied, caymanSymbol);
   int caymanIndex = 0;
   for (int j = 0; j <= lastBarToCalc; j++)
   {
      datetime time = Time[j];
      double caymanValue = -1;
      for (; caymanIndex < copied; caymanIndex++)
      {
         if (rates[caymanIndex].time <= time)
         {
            caymanValue = rates[caymanIndex].close;
            break;
         }
      }
      if (caymanValue < 0) break;
      CaymanBuffer[j] = caymanValue;
   }
	return (0);
}

int IntPutInRange(int value, int from, int to)
{
	if (to >= from)
	{
		if (value > to) value = to;
		else if (value < from) value = from;
	}
	return (value);
}

void FindCaymanSymbol()
{
   string curSymbol = Symbol();
   // из символов типа EURUSDc NGASm ... USDJPYcent EURCHF_micro
   // оставить нормальное имя типа EURUSD
   int len = StringLen(curSymbol);
   for (int i = 0; i < len; i++)
   {
      ushort c = StringGetCharacter(curSymbol, i);
      if ((c < '0' || c > '9') && (c < 'A' || c > 'Z')) break;
      StringSetCharacter(caymanSymbol, StringLen(caymanSymbol), c);
   }
   if (StringLen(caymanSymbol) == 0)
   {
      Print("символ не поддерживается (" + curSymbol + ")");
      return;
   }

   // в начало символа прилепить CM
   string resultedSymbol = "CM";
   StringAdd(resultedSymbol, caymanSymbol);
   caymanSymbol = resultedSymbol;
   //Print("Cayman symbol: " + resultedSymbol);
   CheckCaymanSymbol();
}

void CheckCaymanSymbol()
{
   long smbVal;
   if (!SymbolInfoInteger(caymanSymbol, SYMBOL_SELECT, smbVal))
   {
      int error = GetLastError();
      if (error == 4301 || error == 4106) PrintFormat("для %s Кайман (%s) не определен", Symbol(), caymanSymbol);
      else if (error == 4302) PrintFormat("инструмент Каймана недоступен (%s)", caymanSymbol);
      else PrintFormat("ошибка получения инструмента Каймана (%s): %d", caymanSymbol, error);
      caymanSymbol = "";
   }
}
