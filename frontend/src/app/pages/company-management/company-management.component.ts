import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router'; // Import Router
import { MatChipsModule } from '@angular/material/chips';
import { MatTableModule } from '@angular/material/table'; // Table Material
import { CompanyService } from '../../services/company.service';

@Component({
  selector: 'company-management',
  templateUrl: './company-management.component.html',
  styleUrls: ['./company-management.component.css'],
  imports: [MatChipsModule, MatTableModule, CommonModule],
})
export class CompanyManagementComponent implements OnInit {
  companies: {
    company_id: number;
      name: string;
      address: string;
      phoneNumber: string;
      hasInstalation: boolean;
      ownsInstalation: boolean;
      workers: number;
      hasProduct: boolean;
  }[] = [];

  constructor(private companyService: CompanyService, private router: Router) {}

  ngOnInit(): void {
    this.loadCompanies();
  }

  async loadCompanies(): Promise<void> {
    try {
      this.companies = await this.companyService.getCompanies();
    } catch (error) {
      console.error('Error loading companies:', error);
    }
  }

  viewCompany(id: number): void {
    this.router.navigate(['/companies', id]);
  }
}
