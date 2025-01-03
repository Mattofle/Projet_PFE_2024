import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import {jwtDecode} from "jwt-decode";

interface DecodedToken {
  userId: number;
  isAdmin: boolean;
  iss: string;
  // Ajoutez d'autres champs si nécessaire
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  public isLoggedInSubject = new BehaviorSubject<boolean>(false);
  public isAdminSubject = new BehaviorSubject<boolean>(false);

  isLoggedIn$ = this.isLoggedInSubject.asObservable();
  isAdmin$ = this.isAdminSubject.asObservable();

  constructor() {
    this.checkAuthStatus(); // Initialiser l'état d'authentification au démarrage
  }

  /**
   * Connecte un utilisateur en enregistrant son token et en mettant à jour son état.
   * @param token JWT de l'utilisateur.
   */
  login(token: string) {
    localStorage.setItem('userToken', token);
    this.updateAuthStatus(token);
  }

  /**
   * Déconnecte un utilisateur en supprimant son token et en réinitialisant son état.
   */
  logout() {
    localStorage.removeItem('userToken');
    this.isLoggedInSubject.next(false);
    this.isAdminSubject.next(false);
  }

  /**
   * Vérifie l'état d'authentification en validant la présence d'un token.
   */
  checkAuthStatus() {
    const token = localStorage.getItem('userToken');
    if (token) {
      this.updateAuthStatus(token);
    } else {
      this.isLoggedInSubject.next(false);
      this.isAdminSubject.next(false);
    }
  }

  /**
   * Décoder un token JWT pour extraire les informations utilisateur.
   * @param token JWT à décoder.
   */
  private decodeToken(token: string): DecodedToken {
    try {
      return jwtDecode<DecodedToken>(token);
    } catch (error) {
      console.error('Erreur lors du décodage du token:', error);
      throw new Error('Token invalide');
    }
  }

  /**
   * Récupère l'identifiant utilisateur depuis le token JWT.
   */
  getUserIdFromToken(): number | null {
    const token = localStorage.getItem('userToken');
    if (!token) {
      return null;
    }

    try {
      const decodedToken = this.decodeToken(token);
      return decodedToken.userId;
    } catch (error) {
      console.error('Erreur lors de la récupération de l\'userId:', error);
      return null;
    }
  }

  /**
   * Met à jour l'état d'authentification en fonction du token JWT.
   * @param token JWT valide.
   */
  private updateAuthStatus(token: string) {
    try {
      const decodedToken = this.decodeToken(token);
      this.isLoggedInSubject.next(true);
      this.isAdminSubject.next(decodedToken.isAdmin);
    } catch (error) {
      console.error('Erreur lors de la mise à jour de l\'état d\'authentification:', error);
      this.logout(); // En cas d'erreur, déconnectez l'utilisateur
    }
  }
}
