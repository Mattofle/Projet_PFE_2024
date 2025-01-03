import { Component, inject } from '@angular/core';
import {RouterOutlet } from '@angular/router';
import { MyApiService } from './services/api.service';
import { NavbarComponent } from './components/navbar/navbar.component';
import { FooterComponent } from './components/footer/footer.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavbarComponent, FooterComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {  

  title = 'frontend';
}