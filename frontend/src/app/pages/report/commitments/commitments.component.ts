import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReportService } from '../../../services/report.service';

@Component({
  selector: 'app-commitments',
  imports: [
    CommonModule,
   
  ],
  templateUrl: './commitments.component.html',
  styleUrl: './commitments.component.css'
})
export class CommitmentsComponent implements OnInit {

  commitments: Record<string, Array<Record<string, string | null>>> = {};

  constructor(private api : ReportService) {}

  ngOnInit(): void {
    this.loadCommitments();
  }

  async loadCommitments(): Promise<void> {
    try {
      this.commitments = await this.api.getCommitments();
    } catch (error) {
      console.error('Error loading commitments:', error);
    }
  }

  getKeys(obj: Record<string, Array<Record<string, string | null>>>): string[] {
    return Object.keys(obj);
  }
  

}
