package org.example.exchangerateportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExchangeRatePortalApplication {

//    Write a web application with such functionality:
//            1. Central bank exchange rates page. Exchange rates from the Bank of Lithuania are displayed here: http://www.lb.lt/webservices/FxRates/
//            2. After selecting a specific currency, its exchange rate history is displayed (chart or table).
//            3. Currency calculator. The amount is entered, the currency is selected, the program displays the amount in foreign currency and the rate at which it was calculated.
//            4.* Exchange rates must be automatically obtained every day (eg using quartz).
//            5.* Use the H2 database for data storage.

    // TODO BE
    //    1. Exchange Rate History for Currency
    //    2. Saving history
    //    3. querying history
    //    4. updating DB so when new day becomes then we add new entry to history.
    public static void main(String[] args) {
        SpringApplication.run(ExchangeRatePortalApplication.class, args);
    }

}
