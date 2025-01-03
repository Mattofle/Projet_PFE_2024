import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class glossaireService {
  private baseUrl = 'http://localhost:8080/api';

  constructor() {}

  async getGlossaire(): Promise<
    {
      glossaireId: number;
      word: string;
      definition: string;
      comment: string;
      info: string;
    }[]
  > {
    try {
      const response = await fetch(`${this.baseUrl}/glossaires`, {
        method: 'GET',
        headers: {
          Authorization: `${localStorage.getItem('userToken')}`,
        },
      });

      if (!response.ok) {
        throw new Error('Failed to fetch glossaire');
      }
      const data = await response.json();
      return data;
    } catch (error) {
      console.error('Error fetching glossaire:', error);
      throw error;
    }
  }



  
  


}
