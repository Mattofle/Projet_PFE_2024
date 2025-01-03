import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'

  })

export class AccountManagementService {

  apiUrl = 'http://localhost:8080/api'; // URL de votre API Spring

  async getAllAdmins(): Promise<any> {
    try {
      const token = localStorage.getItem('userToken');
      if (!token) {
        throw new Error('Pas de token');
      }
  
      const response = await fetch(`${this.apiUrl}/users/admin`, {
        method: 'GET', 
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `${token}`, 
        },
      });
  
      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`Erreur ${response.status}: ${errorMessage}`);
      }

      const data = await response.json();
      return data; 

    } catch (error: any) {
      console.error('Erreur lors de la récupération ', error.message);
      throw error; 
    }
  }

  async createUser(credentials : {login: string; password: string; is_admin: boolean}): Promise<any>{
    try{
      const token = localStorage.getItem('userToken');
      if (!token) {
        throw new Error('Pas de token');
      }
      const response = await fetch(`${this.apiUrl}/authentication/register/admin`, {
        method: 'POST', 
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `${token}`, 
        },
        body: JSON.stringify(credentials),
      });


      return response;

    }catch (error: any) {
      console.error('Erreur lors de la récupération ', error.message);
      throw error; 
    }
  }


  
  async createCompany(credentials : {name: string; address: string; phoneNumber: string; login: string; password: string; hasInstalation: boolean; workers: number; hasProduct: boolean; ownsInstalation: boolean}): Promise<any>{
    try{
      const token = localStorage.getItem('userToken');
      if (!token) {
        throw new Error('Pas de token');
      }
      const response = await fetch(`${this.apiUrl}/authentication/register/company`, {
        method: 'POST', 
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `${token}`, 
        },
        body: JSON.stringify(credentials),
      });

      return response;

    }catch (error: any) {
      console.error('Erreur lors de la récupération ', error.message);
      throw error; 
    }
  }

  async deleteUser(userId: number): Promise<void> {
    try {
      const token = localStorage.getItem('userToken');
      if (!token) {
        throw new Error('Pas de token');
      }
      const response = await fetch(`${this.apiUrl}/users/${userId}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `${token}`, 
        },
      });

      if (!response.ok) {
        throw new Error(`Failed to delete user with ID ${userId}`);
      }

    } catch (error) {
      console.error('Error deleting user:', error);
      throw error;
    }
  }
}