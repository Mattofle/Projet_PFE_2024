import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router } from '@angular/router';
import { AuthService } from '../services/auth.services';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class NotAdminGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): Observable<boolean> {
    return this.authService.isAdmin$.pipe(
      map(isAdmin => {
        if (!isAdmin) {
          return true; // Autorise l'accès si l'utilisateur n'est PAS admin
        }
        this.router.navigate(['/account-management']); // Redirige les admins vers une autre page
        return false;
      })
    );
  }
}
