import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.services';
import { CompanyService } from '../services/company.service';

@Injectable({
  providedIn: 'root',
})
export class AccessFormGuardAdmin implements CanActivate {
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

    const userId = route.params['id'];

    if (!userId) {
      console.error('user ID is required');
      return false;
    }

    const module = route.data['module']; // Par défaut "ODD", mais peut être modifié pour ESG

    try {
      // Récupérer le module spécifique pour cette entreprise
      const moduleData = await this.companyService.getModuleCompanyOrNull(userId, module);
      if(moduleData === null){
        this.router.navigate(['/companies']); // Rediriger si l'accès est refusé
        return false;
      }

      // Logique Admin : Peut accéder si le module a une date mais n'est pas validé
      const hasAccess = moduleData.date_form && !moduleData.is_validated;
      if (!hasAccess) {
        console.error('Admin cannot access: Module is either validated or incomplete');
        this.router.navigate(['/companies']); // Rediriger si l'accès est refusé
        return false;
      }

      return true; // Autoriser l'accès si les conditions sont remplies
    } catch (error) {
      console.error('Error fetching module:', error);
      this.router.navigate(['/']); // Rediriger en cas d'erreur
      return false;
    }
  }
}
