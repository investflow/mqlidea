//--------------------------------------------------------------------
// tradingexpert.mq4
// Предназначен для использования в качестве примера в учебнике MQL4.
//--------------------------------------------------------------------
#property copyright "Copyright © Book, 2007"
#property link      "http://AutoGraf.dp.ua"
//--------------------------------------------------------------- 1 --
                                   // Численные значения для М15
extern double StopLoss   =200;     // SL для открываемого ордера
extern double TakeProfit =39;      // ТР для открываемого ордера
extern int    Period_MA_1=11;      // Период МА 1
extern int    Period_MA_2=31;      // Период МА 2
extern double Rastvor    =28.0;    // Расстояние между МА
extern double Lots       =0.1;     // Жестко заданное колич. лотов
extern double Prots      =0.07;    // Процент свободных средств

bool Work=true;                    // Эксперт будет работать.
string Symb;                       // Название финанс. инструмента
//--------------------------------------------------------------- 2 --
int start()
  {
   int
   Total,                           // Количество ордеров в окне
   Tip=-1,                          // Тип выбран. ордера (B=0,S=1)
   Ticket;                          // Номер ордера
   double
   MA_1_t,                          // Значен. МА_1 текущее
   MA_2_t,                          // Значен. МА_2 текущее
   Lot,                             // Колич. лотов в выбран.ордере
   Lts,                             // Колич. лотов в открыв.ордере
   Min_Lot,                         // Минимальное количество лотов
   Step,                            // Шаг изменения размера лота
   Free,                            // Текущие свободные средства
   One_Lot,                         // Стоимость одного лота
   Price,                           // Цена выбранного ордера
   SL,                              // SL выбранного ордера
   TP;                              // TP выбранного ордера
   bool
   Ans  =false,                     // Ответ сервера после закрытия
   Cls_B=false,                     // Критерий для закрытия  Buy
   Cls_S=false,                     // Критерий для закрытия  Sell
   Opn_B=false,                     // Критерий для открытия  Buy
   Opn_S=false;                     // Критерий для открытия  Sell
//--------------------------------------------------------------- 3 --
   // Предварит.обработка
   if(Bars < Period_MA_2)                       // Недостаточно баров
     {
      Alert("Недостаточно баров в окне. Эксперт не работает.");
      return;                                   // Выход из start()
     }
   if(Work==false)                              // Критическая ошибка
     {
      Alert("Критическая ошибка. Эксперт не работает.");
      return;                                   // Выход из start()
     }
//--------------------------------------------------------------- 4 --
   // Учёт ордеров
   Symb=Symbol();                               // Название фин.инстр.
   Total=0;                                     // Количество ордеров
   for(int i=1; i<=OrdersTotal(); i++)          // Цикл перебора ордер
     {
      if (OrderSelect(i-1,SELECT_BY_POS)==true) // Если есть следующий
        {                                       // Анализ ордеров:
         if (OrderSymbol()!=Symb)continue;      // Не наш фин. инструм
         if (OrderType()>1)                     // Попался отложенный
           {
            Alert("Обнаружен отложенный ордер. Эксперт не работает.");
            return;                             // Выход из start()
           }
         Total++;                               // Счётчик рыночн. орд
         if (Total>1)                           // Не более одного орд
           {
            Alert("Несколько рыночных ордеров. Эксперт не работает.");
            return;                             // Выход из start()
           }
         Ticket=OrderTicket();                  // Номер выбранн. орд.
         Tip   =OrderType();                    // Тип выбранного орд.
         Price =OrderOpenPrice();               // Цена выбранн. орд.
         SL    =OrderStopLoss();                // SL выбранного орд.
         TP    =OrderTakeProfit();              // TP выбранного орд.
         Lot   =OrderLots();                    // Количество лотов
        }
     }
//--------------------------------------------------------------- 5 --
   // Торговые критерии
   MA_1_t=iMA(NULL,0,Period_MA_1,0,MODE_LWMA,PRICE_TYPICAL,0); // МА_1
   MA_2_t=iMA(NULL,0,Period_MA_2,0,MODE_LWMA,PRICE_TYPICAL,0); // МА_2

   if (MA_1_t > MA_2_t + Rastvor*Point)         // Если разница между
     {                                          // ..МА 1 и 2 большая
      Opn_B=true;                               // Критерий откр. Buy
      Cls_S=true;                               // Критерий закр. Sell
     }
   if (MA_1_t < MA_2_t - Rastvor*Point)         // Если разница между
     {                                          // ..МА 1 и 2 большая
      Opn_S=true;                               // Критерий откр. Sell
      Cls_B=true;                               // Критерий закр. Buy
     }
//--------------------------------------------------------------- 6 --
   // Закрытие ордеров
   while(true)                                  // Цикл закрытия орд.
     {
      if (Tip==0 && Cls_B==true)                // Открыт ордер Buy..
        {                                       //и есть критерий закр
         Alert("Попытка закрыть Buy ",Ticket,". Ожидание ответа..");
         RefreshRates();                        // Обновление данных
         Ans=OrderClose(Ticket,Lot,Bid,2);      // Закрытие Buy
         if (Ans==true)                         // Получилось :)
           {
            Alert ("Закрыт ордер Buy ",Ticket);
            break;                              // Выход из цикла закр
           }
         if (Fun_Error(GetLastError())==1)      // Обработка ошибок
            continue;                           // Повторная попытка
         return;                                // Выход из start()
        }

      if (Tip==1 && Cls_S==true)                // Открыт ордер Sell..
        {                                       // и есть критерий закр
         Alert("Попытка закрыть Sell ",Ticket,". Ожидание ответа..");
         RefreshRates();                        // Обновление данных
         Ans=OrderClose(Ticket,Lot,Ask,2);      // Закрытие Sell
         if (Ans==true)                         // Получилось :)
           {
            Alert ("Закрыт ордер Sell ",Ticket);
            break;                              // Выход из цикла закр
           }
         if (Fun_Error(GetLastError())==1)      // Обработка ошибок
            continue;                           // Повторная попытка
         return;                                // Выход из start()
        }
      break;                                    // Выход из while
     }
//--------------------------------------------------------------- 7 --
   // Стоимость ордеров
   RefreshRates();                              // Обновление данных
   Min_Lot=MarketInfo(Symb,MODE_MINLOT);        // Миним. колич. лотов
   Free   =AccountFreeMargin();                 // Свободн средства
   One_Lot=MarketInfo(Symb,MODE_MARGINREQUIRED);// Стоимость 1 лота
   Step   =MarketInfo(Symb,MODE_LOTSTEP);       // Шаг изменен размера

   if (Lots > 0)                                // Если заданы лоты,то
      Lts =Lots;                                // с ними и работаем
   else                                         // % свободных средств
      Lts=MathFloor(Free*Prots/One_Lot/Step)*Step;// Для открытия

   if(Lts < Min_Lot) Lts=Min_Lot;               // Не меньше минимальн
   if (Lts*One_Lot > Free)                      // Лот дороже свободн.
     {
      Alert(" Не хватает денег на ", Lts," лотов");
      return;                                   // Выход из start()
     }
//--------------------------------------------------------------- 8 --
   // Открытие ордеров
   while(true)                                  // Цикл закрытия орд.
     {
      if (Total==0 && Opn_B==true)              // Открытых орд. нет +
        {                                       // критерий откр. Buy
         RefreshRates();                        // Обновление данных
         SL=Bid - New_Stop(StopLoss)*Point;     // Вычисление SL откр.
         TP=Bid + New_Stop(TakeProfit)*Point;   // Вычисление TP откр.
         Alert("Попытка открыть Buy. Ожидание ответа..");
         Ticket=OrderSend(Symb,OP_BUY,Lts,Ask,2,SL,TP);//Открытие Buy
         if (Ticket > 0)                        // Получилось :)
           {
            Alert ("Открыт ордер Buy ",Ticket);
            return;                             // Выход из start()
           }
         if (Fun_Error(GetLastError())==1)      // Обработка ошибок
            continue;                           // Повторная попытка
         return;                                // Выход из start()
        }
      if (Total==0 && Opn_S==true)              // Открытых орд. нет +
        {                                       // критерий откр. Sell
         RefreshRates();                        // Обновление данных
         SL=Ask + New_Stop(StopLoss)*Point;     // Вычисление SL откр.
         TP=Ask - New_Stop(TakeProfit)*Point;   // Вычисление TP откр.
         Alert("Попытка открыть Sell. Ожидание ответа..");
         Ticket=OrderSend(Symb,OP_SELL,Lts,Bid,2,SL,TP);//Открытие Sel
         if (Ticket > 0)                        // Получилось :)
           {
            Alert ("Открыт ордер Sell ",Ticket);
            return;                             // Выход из start()
           }
         if (Fun_Error(GetLastError())==1)      // Обработка ошибок
            continue;                           // Повторная попытка
         return;                                // Выход из start()
        }
      break;                                    // Выход из while
     }
//--------------------------------------------------------------- 9 --
   return;                                      // Выход из start()
  }
//-------------------------------------------------------------- 10 --
int Fun_Error(int Error)                        // Ф-ия обработ ошибок
  {
   switch(Error)
     {                                          // Преодолимые ошибки
      case  4: Alert("Торговый сервер занят. Пробуем ещё раз..");
         Sleep(3000);                           // Простое решение
         return(1);                             // Выход из функции
      case 135:Alert("Цена изменилась. Пробуем ещё раз..");
         RefreshRates();                        // Обновим данные
         return(1);                             // Выход из функции
      case 136:Alert("Нет цен. Ждём новый тик..");
         while(RefreshRates()==false)           // До нового тика
            Sleep(1);                           // Задержка в цикле
         return(1);                             // Выход из функции
      case 137:Alert("Брокер занят. Пробуем ещё раз..");
         Sleep(3000);                           // Простое решение
         return(1);                             // Выход из функции
      case 146:Alert("Подсистема торговли занята. Пробуем ещё..");
         Sleep(500);                            // Простое решение
         return(1);                             // Выход из функции
         // Критические ошибки
      case  2: Alert("Общая ошибка.");
         return(0);                             // Выход из функции
      case  5: Alert("Старая версия терминала.");
         Work=false;                            // Больше не работать
         return(0);                             // Выход из функции
      case 64: Alert("Счет заблокирован.");
         Work=false;                            // Больше не работать
         return(0);                             // Выход из функции
      case 133:Alert("Торговля запрещена.");
         return(0);                             // Выход из функции
      case 134:Alert("Недостаточно денег для совершения операции.");
         return(0);                             // Выход из функции
      default: Alert("Возникла ошибка ",Error); // Другие варианты
         return(0);                             // Выход из функции
     }
  }
//-------------------------------------------------------------- 11 --
int New_Stop(int Parametr)                      // Проверка стоп-прик.
  {
   int Min_Dist=MarketInfo(Symb,MODE_STOPLEVEL);// Миним. дистанция
   if (Parametr < Min_Dist)                     // Если меньше допуст.
     {
      Parametr=Min_Dist;                        // Установим допуст.
      Alert("Увеличена дистанция стоп-приказа.");
     }
   return(Parametr);                            // Возврат значения
  }
//-------------------------------------------------------------- 12 --
