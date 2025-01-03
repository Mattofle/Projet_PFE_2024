import { Injectable } from '@angular/core';


@Injectable({
  providedIn: 'root'
  
})

export class MyApiService {

 apiUrl = 'http://localhost:8080/api'; // URL de votre API Spring

  /*constructor(private http: HttpClient) { }

  getData(): Observable<string> {
    //print ce que l'api retourne dans la console
    this.http.get<string>(this.apiUrl).subscribe(data => console.log(data));
    return this.http.get<string>(this.apiUrl);
  }*/

  
    async getData() : Promise<string> {
      const data = await fetch(this.apiUrl);
      const response = await data.text  () ?? ""
      return response
    }

    // HTTP Request to login
    async login(credentials : {login: string; password : string}) : Promise<string> {
      const response = await fetch(`${this.apiUrl}/authentication/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(credentials),
      });
    
      if (response.ok) {
        // connection ok
        const token = await response.text();
        return token; 
      } else {
        // connection failed
        const errorMessage = await response.text();
        throw new Error(errorMessage); 
      }
    }
}
