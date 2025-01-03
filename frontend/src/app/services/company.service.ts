import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})

export class CompanyService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private router: Router) {}

  async getCompanies(): Promise<
    {
      company_id: number;
      name: string;
      address: string;
      phoneNumber: string;
      hasInstalation: boolean;
      ownsInstalation: boolean;
      workers: number;
      hasProduct: boolean;
    }[]
  > {
    try {
      const response = await fetch(`${this.baseUrl}/companies`);
      if (!response.ok) {
        throw new Error('Failed to fetch companies');
      }
      const reponse = await response.json();
      return reponse;
    } catch (error) {
      console.error('Error fetching companies:', error);
      throw error;
    }
  }

  async getModuleCompany(companyId: number, moduleTitle: String): Promise<
    {
      moduleId: number;
      companyId: number;
      date_form: Date;
      score: number;
      score_engagement: number;
      scoremax: number;
      is_validated: boolean;
    }
    > {
    try {
      
      const response = await fetch(`${this.baseUrl}/moduleCompany/${companyId}/${moduleTitle}`);
      if (!response.ok) {
        throw new Error('Failed to fetch module company');
      }
      const reponse = await response.json();
      
      return reponse;
    } catch (error) {
      console.error('Error fetching module company:', error);
      throw error;
    }    
  }
  
  async validateModuleCompany(companyId: number, moduleId: number): Promise<void> {
    try {
      const token = `${localStorage.getItem('userToken')}`
      const response = await fetch(`${this.baseUrl}/moduleCompany/${companyId}/validate/${moduleId}`, {
        method: 'PUT',
        headers: {
          Authorization: token,
        },
      });
      if (!response.ok) {
        throw new Error('Failed to validate module company');
      }

      this.router.navigate(['/companies/' + companyId]);
    } catch (error) {
      console.error('Error validating module company:', error);
      throw error;
    }
  }

  async getModuleCompanyOrNull(companyId: number, moduleTitle: string): Promise<
  | {
      moduleId: number;
      companyId: number;
      date_form: Date;
      score: number;
      score_engagement: number;
      scoremax: number;
      is_validated: boolean;
    }
  | null
> {
  try {
    const response = await fetch(`${this.baseUrl}/moduleCompany/${companyId}/${moduleTitle}`);
    if (!response.ok) {
      if (response.status === 404) {
        return null;
      }
      throw new Error('Failed to fetch module company');
    }

    const text = await response.text();
    if (!text) {
      return null;
    }

    const reponse = JSON.parse(text);
    return reponse;
  } catch (error) {
    console.error('Error fetching module company:', error);
    return null; // Return null explicitly on error
  }
}

  
}


