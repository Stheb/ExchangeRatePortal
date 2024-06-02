import {Component, Input, OnChanges, OnInit, SimpleChanges} from "@angular/core";
import {IconFieldModule} from "primeng/iconfield";
import {InputIconModule} from "primeng/inputicon";
import {InputTextModule} from "primeng/inputtext";
import {InputNumberModule} from "primeng/inputnumber";
import {FormsModule} from "@angular/forms";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'currency-calculator',
  templateUrl: './currencyCalculator.component.html',
  styleUrls: ['./currencyCalculator.component.css'],
  imports: [
    IconFieldModule,
    InputIconModule,
    InputTextModule,
    InputNumberModule,
    FormsModule,
    MatIcon
  ],
  standalone: true
})
export class CurrencyCalculatorComponent implements OnInit, OnChanges {

  inputNumber: number = 0;
  result: number = 0;
  @Input() currentSelectedCurrency: string = "";
  @Input() currentSelectedRate: number = 1;

  ngOnInit(): void {
    this.currentSelectedCurrency = "EUR";
    this.currentSelectedRate = 1;
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.result = 0;
    this.inputNumber = 0;
  }

  updateResult(): void {
    setTimeout(() => {
      this.result = this.inputNumber * this.currentSelectedRate;
    }, 0)
  }

  // Without setTimeouts it fails to update input fields for both...
  updateInput(): void {
    setTimeout(() => {
      this.inputNumber = this.result / this.currentSelectedRate;
    }, 0)
  }

  handleKeyDown(event: KeyboardEvent, field: string): void {
    if (event.key === 'Backspace' || event.key === 'Delete') {
      setTimeout(() => {
        if (field === 'input') {
          this.updateResult();
        } else if (field === 'result') {
          this.updateInput();
        }
      }, 0);
    }
  }
}
