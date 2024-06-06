import {Component, EventEmitter, OnInit, Output} from "@angular/core";
import {ExchangeRatePortalService} from "../../service/ExchangeRatePortal.service";
import {CurrencyModel} from "../../models/currency.model";
import {FormsModule} from "@angular/forms";
import {DropdownModule} from "primeng/dropdown";
import {InputTextModule} from "primeng/inputtext";
import {ChartModule} from "primeng/chart";
import {ExchangeModel} from "../../models/exchange.model";
import {HistoryDateStartModel} from "../../models/historyDateStart.model";
import {NgIf} from "@angular/common";
import {DividerModule} from "primeng/divider";
import {ProgressSpinnerModule} from "primeng/progressspinner";
import {CheckboxModule} from "primeng/checkbox";
import {TooltipModule} from "primeng/tooltip";

@Component({
  selector: 'currency-selector',
  templateUrl: './currencySelector.component.html',
  styleUrls: ['./currencySelector.component.css'],
  imports: [
    DropdownModule,
    FormsModule,
    InputTextModule,
    ChartModule,
    NgIf,
    DividerModule,
    ProgressSpinnerModule,
    CheckboxModule,
    TooltipModule
  ],
  standalone: true
})
export class CurrencySelectorComponent implements OnInit {

  currencies: CurrencyModel[] = [];
  selectedCurrency: CurrencyModel | null = null;
  @Output() currencySelected = new EventEmitter<string>();
  @Output() rateSelected = new EventEmitter<number>();
  filteredCurrencies: CurrencyModel[] = [];

  historyData: ExchangeModel[] = [];
  data: any;
  options: any;
  isHistoryError: boolean = false;
  isChartLoading: boolean = false;

  fromDateChoices: HistoryDateStartModel[] = [];
  selectedFromDate: HistoryDateStartModel | null = null;

  selectedCurrencyRate: ExchangeModel | null = null;

  forceFromLb: boolean = false;

  constructor(private exchangeService: ExchangeRatePortalService) {
  }

  ngOnInit(): void {
    this.getCurrencies();

    this.initOptions();
    this.initHistoryStart();
  }

  onCurrencySelected(): void {
    if (this.selectedCurrency !== null) {
      this.isHistoryError = false;
      this.isChartLoading = true;

      if (this.selectedFromDate == null) {
        this.selectedFromDate = this.fromDateChoices[0];
      }

      this.exchangeService.getExchangeRateHistoryFor(this.selectedCurrency.code, this.selectedFromDate.from, this.forceFromLb)
        .subscribe((history: ExchangeModel[]) => {

          this.isHistoryError = history.length === 0;

          this.isChartLoading = false;
          this.historyData = history;
          this.createData(history);
        })

      this.exchangeService.getExchangeRateFor(this.selectedCurrency.code)
        .subscribe((rate: ExchangeModel) => {
          this.selectedCurrencyRate = rate;

          this.rateSelected.emit(this.selectedCurrencyRate.rate)
        })

      this.currencySelected.emit(this.selectedCurrency.code);
    }
  }

  createData(history: ExchangeModel[]) {
    let numbers = history.map(item => {
      return item.rate;
    });

    let dates = history.map(item => {
      return item.date;
    })

    this.data = {
      labels: dates,
      datasets: [
        {
          label: 'Currency data for: ' + this.selectedCurrency?.code,
          data: numbers,
          fill: false,
          borderColor: "#4caf50",
          tension: 0.4
        }
      ]
    };
  }

  getCurrencies(): void {
    this.exchangeService.getAllCurrencies().subscribe(
      (currencies: CurrencyModel[]) => {
        this.currencies = this.applyDisplayName(currencies);
        this.filteredCurrencies = this.currencies;
      }
    );
  }

  applyDisplayName(currencies: CurrencyModel[]): CurrencyModel[] {
    return currencies.map(curr => {
      return {name: curr.name, code: curr.code, displayName: curr.name + " " + curr.code}
    });
  }

  dropDownFilter(event: any) {
    this.filteredCurrencies = this.currencies.filter(currency =>
      currency.name.toLowerCase().includes(event.filter.toLowerCase()) ||
      currency.code.toLowerCase().includes(event.filter.toLowerCase())
    );
  }

  private initOptions() {
    const documentStyle = getComputedStyle(document.documentElement);

    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

    this.options = {
      maintainAspectRatio: false,
      aspectRatio: 1,
      plugins: {
        legend: {
          labels: {
            color: textColor
          }
        }
      },
      scales: {
        x: {
          ticks: {
            color: textColorSecondary
          },
          grid: {
            color: surfaceBorder,
            drawBorder: true
          }
        },
        y: {
          ticks: {
            color: textColorSecondary
          },
          grid: {
            color: surfaceBorder,
            drawBorder: true
          }
        }
      }
    };
  }


  private initHistoryStart(): void {
    const currentDate = new Date();

    const twoYearsAgo = new Date(currentDate.getFullYear() - 2, currentDate.getMonth(), 1);
    const oneYearAgo = new Date(currentDate.getFullYear() - 1, currentDate.getMonth(), 1);
    const sixMonthsAgo = new Date(currentDate.getFullYear(), currentDate.getMonth() - 6, 1);
    const threeMonthsAgo = new Date(currentDate.getFullYear(), currentDate.getMonth() - 3, 1);
    const oneMonthAgo = new Date(currentDate.getFullYear(), currentDate.getMonth() - 1, 1);

    const twoYearsAgoString = `${twoYearsAgo.getFullYear()}-${(twoYearsAgo.getMonth() + 1).toString().padStart(2, '0')}-01`;
    const oneYearAgoString = `${oneYearAgo.getFullYear()}-${(oneYearAgo.getMonth() + 1).toString().padStart(2, '0')}-01`;
    const sixMonthsAgoString = `${sixMonthsAgo.getFullYear()}-${(sixMonthsAgo.getMonth() + 1).toString().padStart(2, '0')}-01`;
    const threeMonthsAgoString = `${threeMonthsAgo.getFullYear()}-${(threeMonthsAgo.getMonth() + 1).toString().padStart(2, '0')}-01`;
    const oneMonthAgoString = `${oneMonthAgo.getFullYear()}-${(oneMonthAgo.getMonth() + 1).toString().padStart(2, '0')}-01`;

    this.fromDateChoices = [
      { from: twoYearsAgoString, displayName: "2 years"},
      { from: oneYearAgoString, displayName: "1 year"},
      { from: sixMonthsAgoString, displayName: "6 months"},
      { from: threeMonthsAgoString, displayName: "3 months"},
      { from: oneMonthAgoString, displayName: "1 month"}
    ]
  }
}
