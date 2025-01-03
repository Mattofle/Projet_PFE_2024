import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.services';
import { CompanyService } from '../services/company.service';

@Injectable({
  providedIn: 'root',
})
export class AccessFormGuardUser implements CanActivate {
  constructor(
    private authService: AuthService,
    private companyService: CompanyService,
    private router: Router
  ) {}

  async canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Promise<boolean> {

    const token = localStorage.getItem('userToken');
    if (!token) {
      this.router.navigate(['/login']);
      return false;
    }

    const userId = this.authService.getUserIdFromToken();

    if (!userId) {
      console.error('user ID is required');
      return false;
    }

    const module = route.data['module'] ; 

    try {
      // Récupérer le module spécifique pour cette entreprise
      const moduleData = await this.companyService.getModuleCompanyOrNull(userId, module);

      

    if(moduleData === null){
        return true;
    }
      // Logique Utilisateur : Peut accéder si le module a une date
      const hasAccess = moduleData.date_form == null;
      if (!hasAccess) {
        console.error('User cannot access: No modules have dates');
        this.router.navigate(['/forms']);
        return false;
      }

      return true; // Autoriser l'accès si les conditions sont remplies
    } catch (error) {
      console.error('Error fetching module:', error);
      this.router.navigate(['/forms']);
      return false;
    }
  }
}
