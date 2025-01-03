import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { provideHttpClient } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { RecursiveModuleEsgComponent } from './pages/form-esg/app-module-esg/recursive-module-esg.component';
import { RecursiveModuleOddComponent } from './pages/form-odd/app-module-odd/recursive-module-odd.component';
import { QuestionEsgComponent } from './pages/form-esg/question-esg/question-esg.component';
import { QuestionOddComponent } from './pages/form-odd/question-odd/question-odd.component';



@NgModule({
  declarations: [
    AppComponent,
    RecursiveModuleEsgComponent,
    RecursiveModuleOddComponent,
    QuestionEsgComponent,
    QuestionOddComponent,
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    CommonModule,
    BrowserModule, // adding it in the imports
    FormsModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot({
      timeOut: 3000, // Durée du pop-up
      positionClass: 'toast-top-right', // Position
      preventDuplicates: true, // Évite les duplications
    }),
  ],
  providers: [provideHttpClient()],
  bootstrap: [AppComponent],
})
export class AppModule {}
