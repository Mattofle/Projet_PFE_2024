import {Injectable} from "@angular/core";
import {Company} from '../models/company.model';

@Injectable({
  providedIn: 'root'
})

export class UserService {

  apiUrl = 'http://localhost:8080/api';

  async getUser(): Promise<{ userId: number; login: string; isAdmin: boolean }> {
    // console.log("getUserLogin")
    // console.log(localStorage.getItem('userToken'))
    const userLogin = await fetch(`${this.apiUrl}/users/token`, {
      method: 'GET',
      headers: {
        Authorization: `${localStorage.getItem('userToken')}`,
      },
    });
    const data = await userLogin.json()
    return data;
  }

  async getUserById(userId: number): Promise<{ userId: number; login: string; isAdmin: boolean }> {
    const userLogin = await fetch(`${this.apiUrl}/users/${userId}`, {
      method: 'GET',
      headers: {
        Authorization: `${localStorage.getItem('userToken')}`,
      },
    });
    const data = await userLogin.json()
    return data;
  }

  async getCompany(userId: number): Promise<Company> {
    const company = await fetch(`${this.apiUrl}/companies/${userId}`, {
      method: 'GET',
      headers: {
        Authorization: `${localStorage.getItem('userToken')}`,
      },
    });
    const companyJson = await company.json();
    const companyData: Company = {
      company_id: companyJson.company_id,
      name: companyJson.name,
      address: companyJson.address,
      phoneNumber: companyJson.phoneNumber,
      hasInstallation: companyJson.hasInstalation,
      ownsInstallation: companyJson.ownsInstalation,
      workers: companyJson.workers,
      hasProduct: companyJson.hasProduct,
    };
    return companyData;
  }
 
  async modifyPassword(oldPassword: string, newPassword: string): Promise<any> {
    try{
    const response = await fetch(`${this.apiUrl}/users/modifyPassword`, {
      method: 'PUT',
      headers: {
        Authorization: `${localStorage.getItem('userToken')}`,
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        oldPassword: oldPassword,
        newPassword: newPassword,
      }),
    });
    return response;

    }catch (error: any) {
      console.error('Erreur  ', error.message);
      throw error; 
    }
  }

  async updateCompany(company: Company) : Promise<Company> {
    const updatedCompany = {
      company_id: company.company_id,
      name: company.name,
      address: company.address,
      phoneNumber: company.phoneNumber,
      hasInstalation: company.hasInstallation,
      ownsInstalation: company.ownsInstallation,
      hasProduct: company.hasProduct,
      workers: company.workers,
    }
    if(updatedCompany.hasInstalation === false){
      updatedCompany.ownsInstalation = false;
    }
  
    const returnCompany = await fetch(`${this.apiUrl}/companies/${company.company_id}`, {
      method: 'PATCH',
      headers: {
        Authorization: `${localStorage.getItem('userToken')}`,
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(updatedCompany),
    });
    company = await returnCompany.json();
    return company;
  }

  async deleteAccount(): Promise<any> {
    try {
      const responseUser = await fetch(`${this.apiUrl}/users/token`, {
        method: 'GET',
        headers: {
          Authorization: `${localStorage.getItem('userToken')}`,
        },
      });
      if (!responseUser.ok) {
        throw new Error('Failed to fetch User');
      }
      const user = await responseUser.json();
      const response = await fetch(`${this.apiUrl}/companies/${user.userId}`, {
        method: 'DELETE',
        headers: {
          Authorization: `${localStorage.getItem('userToken')}`,
        },
      });
      return response;
    } catch (error) {
      console.error('Error fetching deleteCompany:', error);
      throw error;
    }
  }

}
