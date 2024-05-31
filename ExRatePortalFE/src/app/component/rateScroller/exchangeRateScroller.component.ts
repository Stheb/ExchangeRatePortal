import {Component, OnInit} from "@angular/core";
import {ExchangeRatePortalService} from "../../service/ExchangeRatePortal.service";
import {ExchangeModel} from "../../models/exchange.model";
import {NgForOf, NgIf} from "@angular/common";
import {MatCard, MatCardContent, MatCardHeader} from "@angular/material/card";
import {MatIcon, MatIconModule} from "@angular/material/icon";

@Component({
  selector: 'rate-scroller',
  templateUrl: './exchangeRateScroller.component.html',
  styleUrls: ['./exchangeRateScroller.component.css'],
  imports: [
    NgForOf,
    NgIf,
    MatCard,
    MatCardContent,
    MatCardHeader,
    MatIcon,
    MatIconModule
  ],
  standalone: true
})
export class ExchangeRateScrollerComponent implements OnInit {

  rates: ExchangeModel[] = [];

  constructor(private exchangeService: ExchangeRatePortalService) {}

  ngOnInit(): void {
    this.getTodayRates();
  }

  getTodayRates(): void {
    this.exchangeService.getTodayRates().subscribe(
      (rates: ExchangeModel[]) => this.rates = rates
    );
  }

}
