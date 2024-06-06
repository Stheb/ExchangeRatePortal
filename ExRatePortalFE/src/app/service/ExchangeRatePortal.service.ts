import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {ExchangeModel} from "../models/exchange.model";
import {catchError, Observable, of, retry} from "rxjs";
import {CurrencyModel} from "../models/currency.model";
import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class ExchangeRatePortalService {

  private portal_api_url = "http://localhost:8080";

  constructor(private httpClient: HttpClient) {
  }

  public getTodayRates(): Observable<ExchangeModel[]> {
    return this.httpClient.get<ExchangeModel[]>(this.portal_api_url + "/exchangeRateToday")
      .pipe(
        retry(2),
        catchError((err: HttpErrorResponse) => {
          return of(this.handleError<ExchangeModel[]>(err, []));
        })
      );
  }

  public getExchangeRateFor(code: string): Observable<ExchangeModel> {
    return this.httpClient.get<ExchangeModel>(this.portal_api_url + "/exchangeRateFor/" + code)
      .pipe(
        retry(2),
        catchError((err: HttpErrorResponse) => {
          return of(this.handleError<ExchangeModel>(err, {rate: 0, code: "", date: ""}));
        })
      );
  }

  public getExchangeRateHistoryFor(code: string, startDate: string, forceFromLbLt: boolean): Observable<ExchangeModel[]> {
    return this.httpClient.get<ExchangeModel[]>(this.portal_api_url + "/exchangeRateHistory/" + code + "/" + startDate + "/" + forceFromLbLt)
      .pipe(
        retry(2),
        catchError((err: HttpErrorResponse) => {
          return of(this.handleError<ExchangeModel[]>(err, []));
        })
      );
  }

  public getAllCurrencies(): Observable<CurrencyModel[]> {
    return this.httpClient.get<CurrencyModel[]>(this.portal_api_url + "/currencies")
      .pipe(
        retry(2),
        catchError((err: HttpErrorResponse) => {
          return of(this.handleError<CurrencyModel[]>(err, []));
        })
      );
  }

  private handleError<T>(err: HttpErrorResponse, defaultObject: T): T {
    console.log(err);
    return defaultObject;
  }
}
