//==========================================================================================================================
#property copyright "BETSAFE"
#property link  "http://beam.to/betsafe"   // "WEBSITE"
//==========================================================================================================================
extern double LotExponent = 1.37; // умножение лотов в серии по експоненте для вывода в безубыток. первый лот 0.1, серия: 0.15, 0.26, 0.43 ...
extern double Lots = 0.01;        // теперь можно и микролоты 0.01 при этом если стоит 0.1 то следующий лот в серии будет 0.16
extern int lotdecimal = 2;        // 2 - микролоты 0.01, 1 - мини лоты 0.1, 0 - нормальные лоты 1.0
extern double TakeProfit = 10.0;  // тейк профит
extern double PipStep = 30.0;     // шаг колена
extern double slip = 30.0;        // проскальзывание
extern int MaxTrades = 10;        // максимально количество одновременно открытых ордеров
extern int MagicNumber = 12345;   // магик
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Индикатор МА настройки ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
extern int    MA_TF = 1;          // ТФ с которого берет значение индикатор
extern double MaPeriod=60;        // Период индикатора МА можно настраивать
extern double MaMode=1;           // 0-Простое скользящее среднее, 1-Экспоненциальное скользящее среднее, 2-Сглаженное скользящее среднее, 3-Линейно-взвешенное скользящее среднее
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
bool UseEquityStop = FALSE;       // использовать риск в процентах
double TotalEquityRisk = 20.0;    // риск в процентах от депозита
bool UseTrailingStop = FALSE;     // использовать трейлинг стоп
bool UseTimeOut = FALSE;          // использовать анулирование ордеров по времени
double MaxTradeOpenHours = 48.0;  // через колько часов анулировать висячие ордера
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
double Stoploss = 5000.0;         // Эти три параметра не работают
double TrailStart = 100.0;
double TrailStop = 100.0;
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
double PriceTarget, StartEquity, BuyTarget, SellTarget ;
double AveragePrice, SellLimit, BuyLimit ;
double LastBuyPrice, LastSellPrice, Spread;
bool flag;
string EAName = "Quick_Scalp 1.0";
int timeprev = 0, expiration;
int NumOfTrades = 0;
double iLots;
int cnt = 0, total;
double Stopper = 0.0;
bool TradeNow = FALSE, LongTrade = FALSE, ShortTrade = FALSE;
int ticket;
bool NewOrdersPlaced = FALSE;
double AccountEquityHighAmt, PrevEquity;
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
int init() {
   Spread = MarketInfo(Symbol(), MODE_SPREAD) * Point;
   return (0);
}

int deinit() {
   return (0);
}
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
int start() {
   double PrevCl;
   double CurrCl;
   if (UseTrailingStop) TrailingAlls(TrailStart, TrailStop, AveragePrice);
   if (UseTimeOut) {
      if (TimeCurrent() >= expiration) {
         CloseThisSymbolAll();
         Print("Closed All due to TimeOut");
      }
   }
   if (timeprev == Time[0]) return (0);
   timeprev = Time[0];
   double CurrentPairProfit = CalculateProfit();
   if (UseEquityStop) {
      if (CurrentPairProfit < 0.0 && MathAbs(CurrentPairProfit) > TotalEquityRisk / 100.0 * AccountEquityHigh()) {
         CloseThisSymbolAll();
         Print("Closed All due to Stop Out");
         NewOrdersPlaced = FALSE;
      }
   }
   total = CountTrades();
   if (total == 0) flag = FALSE;
   for (cnt = OrdersTotal() - 1; cnt >= 0; cnt--) {
      OrderSelect(cnt, SELECT_BY_POS, MODE_TRADES);
      if (OrderSymbol() != Symbol() || OrderMagicNumber() != MagicNumber) continue;
      if (OrderSymbol() == Symbol() && OrderMagicNumber() == MagicNumber) {
         if (OrderType() == OP_BUY) {
            LongTrade = TRUE;
            ShortTrade = FALSE;
            break;
         }
      }
      if (OrderSymbol() == Symbol() && OrderMagicNumber() == MagicNumber) {
         if (OrderType() == OP_SELL) {
            LongTrade = FALSE;
            ShortTrade = TRUE;
            break;
         }
      }
   }
   if (total > 0 && total <= MaxTrades) {
      RefreshRates();
      LastBuyPrice = FindLastBuyPrice();
      LastSellPrice = FindLastSellPrice();
      if (LongTrade && LastBuyPrice - Ask >= PipStep * Point) TradeNow = TRUE;
      if (ShortTrade && Bid - LastSellPrice >= PipStep * Point) TradeNow = TRUE;
   }
   if (total < 1) {
      ShortTrade = FALSE;
      LongTrade = FALSE;
      TradeNow = TRUE;
      StartEquity = AccountEquity();
   }
   if (TradeNow) {
      LastBuyPrice = FindLastBuyPrice();
      LastSellPrice = FindLastSellPrice();
      if (ShortTrade) {
         NumOfTrades = total;
         iLots = NormalizeDouble(Lots * MathPow(LotExponent, NumOfTrades), lotdecimal);
         RefreshRates();
         ticket = OpenPendingOrder(1, iLots, Bid, slip, Ask, 0, 0, EAName + "-" + NumOfTrades, MagicNumber, 0, HotPink);
         if (ticket < 0) {
            Print("Error: ", GetLastError());
            return (0);
         }
         LastSellPrice = FindLastSellPrice();
         TradeNow = FALSE;
         NewOrdersPlaced = TRUE;
      } else {
         if (LongTrade) {
            NumOfTrades = total;
            iLots = NormalizeDouble(Lots * MathPow(LotExponent, NumOfTrades), lotdecimal);
            ticket = OpenPendingOrder(0, iLots, Ask, slip, Bid, 0, 0, EAName + "-" + NumOfTrades, MagicNumber, 0, Lime);
            if (ticket < 0) {
               Print("Error: ", GetLastError());
               return (0);
            }
            LastBuyPrice = FindLastBuyPrice();
            TradeNow = FALSE;
            NewOrdersPlaced = TRUE;
         }
      }
   }
   if (TradeNow && total < 1) {
      PrevCl = iClose(Symbol(), 0, 2); // цена закрытия 2 бара, 0 - означает текущий ТФ на котором висит советник.
      CurrCl = iClose(Symbol(), 0, 1); // цена закрытия 1 бара
      SellLimit = Bid;
      BuyLimit = Ask;
      if (!ShortTrade && !LongTrade) {
         NumOfTrades = total;
         iLots = NormalizeDouble(Lots * MathPow(LotExponent, NumOfTrades), lotdecimal);
         if (PrevCl > CurrCl) { 

//HHHHHHHH~~~~~~~~~~~~~ Индюк МА ~~~~~~~~~~HHHHHHHHH~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~         
            
               if (iMA(NULL,MA_TF,MaPeriod,0,MaMode,PRICE_CLOSE,1) > Bid) { // если цена ниже цены Селл
            
               ticket = OpenPendingOrder(1, iLots, SellLimit, slip, SellLimit, 0, 0, EAName + "-" + NumOfTrades, MagicNumber, 0, HotPink);
               if (ticket < 0) {
                  Print("Error: ", GetLastError());
                  return (0);
               }
               LastBuyPrice = FindLastBuyPrice();
               NewOrdersPlaced = TRUE;
            }
         } else {

//HHHHHHHH~~~~~~~~~~~~~ Индюк MA ~~~~~~~~~HHHHHHHHHH~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            
               if (iMA(NULL,MA_TF,MaPeriod,0,MaMode,PRICE_CLOSE,1) < Ask) { // если МА выше цены Бай

               ticket = OpenPendingOrder(0, iLots, BuyLimit, slip, BuyLimit, 0, 0, EAName + "-" + NumOfTrades, MagicNumber, 0, Lime);
               if (ticket < 0) {
                  Print("Error: ", GetLastError());
                  return (0);
               }
               LastSellPrice = FindLastSellPrice();
               NewOrdersPlaced = TRUE;
            }
         }
         if (ticket > 0) expiration = TimeCurrent() + 60.0 * (60.0 * MaxTradeOpenHours);
         TradeNow = FALSE;
      }
   }
   total = CountTrades();
   AveragePrice = 0;
   double Count = 0;
   for (cnt = OrdersTotal() - 1; cnt >= 0; cnt--) {
      OrderSelect(cnt, SELECT_BY_POS, MODE_TRADES);
      if (OrderSymbol() != Symbol() || OrderMagicNumber() != MagicNumber) continue;
      if (OrderSymbol() == Symbol() && OrderMagicNumber() == MagicNumber) {
         if (OrderType() == OP_BUY || OrderType() == OP_SELL) {
            AveragePrice += OrderOpenPrice() * OrderLots();
            Count += OrderLots();
         }
      }
   }
   if (total > 0) AveragePrice = NormalizeDouble(AveragePrice / Count, Digits);
   if (NewOrdersPlaced) {
      for (cnt = OrdersTotal() - 1; cnt >= 0; cnt--) {
         OrderSelect(cnt, SELECT_BY_POS, MODE_TRADES);
         if (OrderSymbol() != Symbol() || OrderMagicNumber() != MagicNumber) continue;
         if (OrderSymbol() == Symbol() && OrderMagicNumber() == MagicNumber) {
            if (OrderType() == OP_BUY) {
               PriceTarget = AveragePrice + TakeProfit * Point;
               BuyTarget = PriceTarget;
               Stopper = AveragePrice - Stoploss * Point;
               flag = TRUE;
            }
         }
         if (OrderSymbol() == Symbol() && OrderMagicNumber() == MagicNumber) {
            if (OrderType() == OP_SELL) {
               PriceTarget = AveragePrice - TakeProfit * Point;
               SellTarget = PriceTarget;
               Stopper = AveragePrice + Stoploss * Point;
               flag = TRUE;
            }
         }
      }
   }
   if (NewOrdersPlaced) {
      if (flag == TRUE) {
         for (cnt = OrdersTotal() - 1; cnt >= 0; cnt--) {
            OrderSelect(cnt, SELECT_BY_POS, MODE_TRADES);
            if (OrderSymbol() != Symbol() || OrderMagicNumber() != MagicNumber) continue;
            if (OrderSymbol() == Symbol() && OrderMagicNumber() == MagicNumber) OrderModify(OrderTicket(), AveragePrice, OrderStopLoss(), PriceTarget, 0, Yellow);
            NewOrdersPlaced = FALSE;
         }
      }
   }
   return (0);
}
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

int CountTrades() {
   int count = 0;
   for (int trade = OrdersTotal() - 1; trade >= 0; trade--) {
      OrderSelect(trade, SELECT_BY_POS, MODE_TRADES);
      if (OrderSymbol() != Symbol() || OrderMagicNumber() != MagicNumber) continue;
      if (OrderSymbol() == Symbol() && OrderMagicNumber() == MagicNumber)
         if (OrderType() == OP_SELL || OrderType() == OP_BUY) count++;
   }
   return (count);
}
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void CloseThisSymbolAll() {
   for (int trade = OrdersTotal() - 1; trade >= 0; trade--) {
      OrderSelect(trade, SELECT_BY_POS, MODE_TRADES);
      if (OrderSymbol() == Symbol()) {
         if (OrderSymbol() == Symbol() && OrderMagicNumber() == MagicNumber) {
            if (OrderType() == OP_BUY) OrderClose(OrderTicket(), OrderLots(), Bid, slip, Blue);
            if (OrderType() == OP_SELL) OrderClose(OrderTicket(), OrderLots(), Ask, slip, Red);
         }
         Sleep(1000);
      }
   }
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

int OpenPendingOrder(int pType, double pLots, double pPrice, int pSlippage, double ad_24, int ai_32, int ai_36, string a_comment_40, int a_magic_48, int a_datetime_52, color a_color_56) {
   int l_ticket_60 = 0;
   int l_error_64 = 0;
   int l_count_68 = 0;
   int li_72 = 100;
   switch (pType) {
   case 2:
      for (l_count_68 = 0; l_count_68 < li_72; l_count_68++) {
         l_ticket_60 = OrderSend(Symbol(), OP_BUYLIMIT, pLots, pPrice, pSlippage, StopLong(ad_24, ai_32), TakeLong(pPrice, ai_36), a_comment_40, a_magic_48, a_datetime_52, a_color_56);
         l_error_64 = GetLastError();
         if (l_error_64 == 0/* NO_ERROR */) break;
         if (!(l_error_64 == 4/* SERVER_BUSY */ || l_error_64 == 137/* BROKER_BUSY */ || l_error_64 == 146/* TRADE_CONTEXT_BUSY */ || l_error_64 == 136/* OFF_QUOTES */)) break;
         Sleep(1000);
      }
      break;
   case 4:
      for (l_count_68 = 0; l_count_68 < li_72; l_count_68++) {
         l_ticket_60 = OrderSend(Symbol(), OP_BUYSTOP, pLots, pPrice, pSlippage, StopLong(ad_24, ai_32), TakeLong(pPrice, ai_36), a_comment_40, a_magic_48, a_datetime_52, a_color_56);
         l_error_64 = GetLastError();
         if (l_error_64 == 0/* NO_ERROR */) break;
         if (!(l_error_64 == 4/* SERVER_BUSY */ || l_error_64 == 137/* BROKER_BUSY */ || l_error_64 == 146/* TRADE_CONTEXT_BUSY */ || l_error_64 == 136/* OFF_QUOTES */)) break;
         Sleep(5000);
      }
      break;
   case 0:
      for (l_count_68 = 0; l_count_68 < li_72; l_count_68++) {
         RefreshRates();
         l_ticket_60 = OrderSend(Symbol(), OP_BUY, pLots, Ask, pSlippage, StopLong(Bid, ai_32), TakeLong(Ask, ai_36), a_comment_40, a_magic_48, a_datetime_52, a_color_56);
         l_error_64 = GetLastError();
         if (l_error_64 == 0/* NO_ERROR */) break;
         if (!(l_error_64 == 4/* SERVER_BUSY */ || l_error_64 == 137/* BROKER_BUSY */ || l_error_64 == 146/* TRADE_CONTEXT_BUSY */ || l_error_64 == 136/* OFF_QUOTES */)) break;
         Sleep(5000);
      }
      break;
   case 3:
      for (l_count_68 = 0; l_count_68 < li_72; l_count_68++) {
         l_ticket_60 = OrderSend(Symbol(), OP_SELLLIMIT, pLots, pPrice, pSlippage, StopShort(ad_24, ai_32), TakeShort(pPrice, ai_36), a_comment_40, a_magic_48, a_datetime_52, a_color_56);
         l_error_64 = GetLastError();
         if (l_error_64 == 0/* NO_ERROR */) break;
         if (!(l_error_64 == 4/* SERVER_BUSY */ || l_error_64 == 137/* BROKER_BUSY */ || l_error_64 == 146/* TRADE_CONTEXT_BUSY */ || l_error_64 == 136/* OFF_QUOTES */)) break;
         Sleep(5000);
      }
      break;
   case 5:
      for (l_count_68 = 0; l_count_68 < li_72; l_count_68++) {
         l_ticket_60 = OrderSend(Symbol(), OP_SELLSTOP, pLots, pPrice, pSlippage, StopShort(ad_24, ai_32), TakeShort(pPrice, ai_36), a_comment_40, a_magic_48, a_datetime_52, a_color_56);
         l_error_64 = GetLastError();
         if (l_error_64 == 0/* NO_ERROR */) break;
         if (!(l_error_64 == 4/* SERVER_BUSY */ || l_error_64 == 137/* BROKER_BUSY */ || l_error_64 == 146/* TRADE_CONTEXT_BUSY */ || l_error_64 == 136/* OFF_QUOTES */)) break;
         Sleep(5000);
      }
      break;
   case 1:
      for (l_count_68 = 0; l_count_68 < li_72; l_count_68++) {
         l_ticket_60 = OrderSend(Symbol(), OP_SELL, pLots, Bid, pSlippage, StopShort(Ask, ai_32), TakeShort(Bid, ai_36), a_comment_40, a_magic_48, a_datetime_52, a_color_56);
         l_error_64 = GetLastError();
         if (l_error_64 == 0/* NO_ERROR */) break;
         if (!(l_error_64 == 4/* SERVER_BUSY */ || l_error_64 == 137/* BROKER_BUSY */ || l_error_64 == 146/* TRADE_CONTEXT_BUSY */ || l_error_64 == 136/* OFF_QUOTES */)) break;
         Sleep(5000);
      }
   }
   return (l_ticket_60);
}

double StopLong(double ad_0, int ai_8) {
   if (ai_8 == 0) return (0);
   else return (ad_0 - ai_8 * Point);
}

double StopShort(double ad_0, int ai_8) {
   if (ai_8 == 0) return (0);
   else return (ad_0 + ai_8 * Point);
}

double TakeLong(double ad_0, int ai_8) {
   if (ai_8 == 0) return (0);
   else return (ad_0 + ai_8 * Point);
}

double TakeShort(double ad_0, int ai_8) {
   if (ai_8 == 0) return (0);
   else return (ad_0 - ai_8 * Point);
}

double CalculateProfit() {
   double ld_ret_0 = 0;
   for (cnt = OrdersTotal() - 1; cnt >= 0; cnt--) {
      OrderSelect(cnt, SELECT_BY_POS, MODE_TRADES);
      if (OrderSymbol() != Symbol() || OrderMagicNumber() != MagicNumber) continue;
      if (OrderSymbol() == Symbol() && OrderMagicNumber() == MagicNumber)
         if (OrderType() == OP_BUY || OrderType() == OP_SELL) ld_ret_0 += OrderProfit();
   }
   return (ld_ret_0);
}

void TrailingAlls(int pType, int ai_4, double a_price_8) {
   int l_ticket_16;
   double l_ord_stoploss_20;
   double l_price_28;
   if (ai_4 != 0) {
      for (int l_pos_36 = OrdersTotal() - 1; l_pos_36 >= 0; l_pos_36--) {
         if (OrderSelect(l_pos_36, SELECT_BY_POS, MODE_TRADES)) {
            if (OrderSymbol() != Symbol() || OrderMagicNumber() != MagicNumber) continue;
            if (OrderSymbol() == Symbol() || OrderMagicNumber() == MagicNumber) {
               if (OrderType() == OP_BUY) {
                  l_ticket_16 = NormalizeDouble((Bid - a_price_8) / Point, 0);
                  if (l_ticket_16 < pType) continue;
                  l_ord_stoploss_20 = OrderStopLoss();
                  l_price_28 = Bid - ai_4 * Point;
                  if (l_ord_stoploss_20 == 0.0 || (l_ord_stoploss_20 != 0.0 && l_price_28 > l_ord_stoploss_20)) OrderModify(OrderTicket(), a_price_8, l_price_28, OrderTakeProfit(), 0, Aqua);
               }
               if (OrderType() == OP_SELL) {
                  l_ticket_16 = NormalizeDouble((a_price_8 - Ask) / Point, 0);
                  if (l_ticket_16 < pType) continue;
                  l_ord_stoploss_20 = OrderStopLoss();
                  l_price_28 = Ask + ai_4 * Point;
                  if (l_ord_stoploss_20 == 0.0 || (l_ord_stoploss_20 != 0.0 && l_price_28 < l_ord_stoploss_20)) OrderModify(OrderTicket(), a_price_8, l_price_28, OrderTakeProfit(), 0, Red);
               }
            }
            Sleep(1000);
         }
      }
   }
}

double AccountEquityHigh() {
   if (CountTrades() == 0) AccountEquityHighAmt = AccountEquity();
   if (AccountEquityHighAmt < PrevEquity) AccountEquityHighAmt = PrevEquity;
   else AccountEquityHighAmt = AccountEquity();
   PrevEquity = AccountEquity();
   return (AccountEquityHighAmt);
}

double FindLastBuyPrice() {
   double l_ord_open_price_8;
   int l_ticket_24;
   double ld_unused_0 = 0;
   int l_ticket_20 = 0;
   for (int l_pos_16 = OrdersTotal() - 1; l_pos_16 >= 0; l_pos_16--) {
      OrderSelect(l_pos_16, SELECT_BY_POS, MODE_TRADES);
      if (OrderSymbol() != Symbol() || OrderMagicNumber() != MagicNumber) continue;
      if (OrderSymbol() == Symbol() && OrderMagicNumber() == MagicNumber && OrderType() == OP_BUY) {
         l_ticket_24 = OrderTicket();
         if (l_ticket_24 > l_ticket_20) {
            l_ord_open_price_8 = OrderOpenPrice();
            ld_unused_0 = l_ord_open_price_8;
            l_ticket_20 = l_ticket_24;
         }
      }
   }
   return (l_ord_open_price_8);
}

double FindLastSellPrice() {
   double l_ord_open_price_8;
   int l_ticket_24;
   double ld_unused_0 = 0;
   int l_ticket_20 = 0;
   for (int l_pos_16 = OrdersTotal() - 1; l_pos_16 >= 0; l_pos_16--) {
      OrderSelect(l_pos_16, SELECT_BY_POS, MODE_TRADES);
      if (OrderSymbol() != Symbol() || OrderMagicNumber() != MagicNumber) continue;
      if (OrderSymbol() == Symbol() && OrderMagicNumber() == MagicNumber && OrderType() == OP_SELL) {
         l_ticket_24 = OrderTicket();
         if (l_ticket_24 > l_ticket_20) {
            l_ord_open_price_8 = OrderOpenPrice();
            ld_unused_0 = l_ord_open_price_8;
            l_ticket_20 = l_ticket_24;
         }
      }
   }
   return (l_ord_open_price_8);
}