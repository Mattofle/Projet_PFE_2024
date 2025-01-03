import { Component, OnInit } from '@angular/core';
import { ReportService } from '../../services/report.service';
import { CommonModule } from '@angular/common';
import { MatChipsModule } from '@angular/material/chips';
import { MatTableModule } from '@angular/material/table';
import { FormsModule } from '@angular/forms';
import { CommitmentsComponent } from './commitments/commitments.component';

interface Module {
  moduleId: number;
  companyId: number;
  date_form: string;
  score: number;
  score_engagement: number;
  scoremax: number;
}

interface SubModule {
  module: Module;
  title: string;
  subModules: SubModule[];
}

interface ReportResponse {
  module: Module;
  title: string;
  subModules: SubModule[];
}

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css'],
  imports: [
    MatChipsModule,
    MatTableModule,
    CommonModule,
    FormsModule,
    CommitmentsComponent,
  ], // Ajouter FormsModule ici
})
export class ReportComponent implements OnInit {
  reportODD: ReportResponse | undefined;
  reportESG: ReportResponse | undefined;
  finalScore: number = 0;

  showReportODD: boolean = false;
  showReportESG: boolean = false;

  constructor(private api: ReportService) {}

  ngOnInit(): void {
    this.loadReport();
    this.toggleReportESG()
  }

  async loadReport(): Promise<void> {
    try {
      this.reportODD = await this.api.getReport('ODD');
    } catch (error) {
      this.reportODD = undefined;
    }
    try {
      this.reportESG = await this.api.getReport('ESG');
    } catch (error) {
      this.reportESG = undefined;
    }


    if (this.reportESG) {
      if (this.reportESG.module.date_form !== null) {
        let scoreESG = 0;

        this.reportESG.subModules.forEach((subModule) => {
          const score = subModule.module.score;
          const score_engagement = subModule.module.score_engagement;
          const scoremax = subModule.module.scoremax;

          const scoreCalcule = (score / scoremax) * 30;
          subModule.module.score = parseFloat(scoreCalcule.toFixed(2));
          scoreESG += scoreCalcule;

          const score_engagementCalcule = (score_engagement / scoremax) * 30;
          subModule.module.score_engagement = parseFloat(
            score_engagementCalcule.toFixed(2)
          );
          subModule.module.scoremax = 30;
          scoreESG += score_engagementCalcule;
        });
        this.reportESG.module.score = parseFloat(scoreESG.toFixed(2));
      } else {
        this.reportESG = undefined;
      }
    }

    if (this.reportODD) {
      if (this.reportODD.module.date_form !== null) {
        this.reportODD.subModules.forEach((subModule) => {
          subModule.module.score = parseFloat(
            subModule.module.score.toFixed(2)
          );
        });
        this.reportODD.module.score = parseFloat(
          this.reportODD.module.score.toFixed(2)
        );
      } else {
        this.reportODD = undefined;
      }
    }

    let scoreODD = 0;
    if (this.reportODD) {
      scoreODD = this.reportODD.module.score;
      scoreODD = scoreODD * 0.3;
    }

    let scoreESG = 0;
    if (this.reportESG) {
      scoreESG = this.reportESG.module.score;
      scoreESG = scoreESG / 9;
      scoreESG = scoreESG * 7;
    }

    let scoreFinal = scoreODD + scoreESG;
    this.finalScore = parseFloat(scoreFinal.toFixed(2));
  }

  toggleReportESG(): void {
    this.showReportESG = !this.showReportESG;
    if (this.showReportESG) {
      this.showReportODD = false; // Hide ODD if ESG is active
    }
  }
  
  toggleReportODD(): void {
    this.showReportODD = !this.showReportODD;
    if (this.showReportODD) {
      this.showReportESG = false; // Hide ESG if ODD is active
    }
  }
  

  goToCommitments(): void {
  }

  generatePdf(): void {
    // Check if jsonData exists before calling the service
    if (this.reportODD && this.reportESG) {
      this.api.generatePdfFromJson(this.reportODD, this.reportESG);
    } else {
      console.error('No data provided for PDF generation');
    }
  }

  
}
