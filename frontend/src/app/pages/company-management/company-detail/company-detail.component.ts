import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { CompanyService } from '../../../services/company.service';
import { RouterModule } from '@angular/router';
import { Module } from '../../../models/module.model';

@Component({
  selector: 'company-detail',
  templateUrl: './company-detail.component.html',
  styleUrls: ['./company-detail.component.css'],
  imports: [CommonModule, RouterModule]
})
export class CompanyDetailComponent implements OnInit {
  company:
    | {
        company_id: number;
        name: string;
        address: string;
        phoneNumber: string;
        hasInstalation: boolean;
        ownsInstalation: boolean;
        workers: number;
        hasProduct: boolean;
      }
    | undefined;

  esgModuleStatus: string | undefined;
  oddModuleStatus: string | undefined;

  constructor(
    private route: ActivatedRoute,
    private companyService: CompanyService
  ) {}

  ngOnInit(): void {
    const companyId = this.route.snapshot.paramMap.get('id');
    if (companyId) {
      this.loadCompany(Number(companyId)); // Convert the string to a number
      this.loadModulesCompany(Number(companyId));
    }
  }

  async loadCompany(id: number): Promise<void> {
    try {
      const companies = await this.companyService.getCompanies();
      this.company = companies.find((company) => company.company_id === id);
    } catch (error) {
      console.error('Error loading company details:', error);
    }
  }

  async loadModulesCompany(id: number): Promise<void> {
    try {
      const companies = await this.companyService.getCompanies();
      this.company = companies.find((company) => company.company_id === id);
      if (this.company) {
        
        try {
          const esgModule = await this.companyService.getModuleCompany(this.company.company_id, 'ESG');
          console.log('ESG', esgModule);
          this.esgModuleStatus = this.getModuleStatus(esgModule);
          console.log('ESG STATUS', this.esgModuleStatus);
        } catch (error) {
          console.error('Error loading ESG module:', error);
          this.esgModuleStatus = 'Remis'; // Statut en cas d'erreur
        }
  
        try {
          const oddModule = await this.companyService.getModuleCompany(this.company.company_id, 'ODD');
          console.log('ODD', oddModule);
          this.oddModuleStatus = this.getModuleStatus(oddModule);
          console.log('ODD STATUS', this.oddModuleStatus);
        } catch (error) {
          console.error('Error loading ODD module:', error);
          this.oddModuleStatus = 'Remis'; // Statut en cas d'erreur
        }
        
      }
    } catch (error) {
      console.error('Error loading modules:', error);
    }
  }

  getModuleStatus(module: Module): string {
    if (module.date_form === null){
      return 'En cours';
    } else if (module.date_form !== null){
      if(module.is_validated){
        return 'Validé';
      }
      return 'Complété';
    } else {
      return 'Remis';
    }
  }
}

export default CompanyDetailComponent;

