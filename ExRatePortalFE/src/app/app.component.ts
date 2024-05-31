import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {ExchangeRateScrollerComponent} from "./component/rateScroller/exchangeRateScroller.component";
import {NgOptimizedImage} from "@angular/common";
import {CurrencySelectorComponent} from "./component/currencySelector/currencySelector.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ExchangeRateScrollerComponent, NgOptimizedImage, CurrencySelectorComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'ExRatePortalFE';
}
