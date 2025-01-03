import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { AccountManagementComponent } from './pages/accountManagement/accountManagement.component';
import { FormsComponent } from './pages/forms/forms.component';
import { CompanyManagementComponent } from './pages/company-management/company-management.component';
import { CompanyDetailComponent } from './pages/company-management/company-detail/company-detail.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { GlossaireComponent } from './pages/glossaire/glossaire.component';
import { ReportComponent } from './pages/report/report.component';
import { AuthGuard } from './guards/auth.guard';
import { RoleGuard } from './guards/isAdmin.guard';
import { OddFormComponent } from './pages/form-odd/odd-form/odd-form.component';
import { EsgFormComponent } from './pages/form-esg/esg-form/esg-form.component';
import { notAuthGuard } from './guards/notAuth.guard';
import { NotAdminGuard } from './guards/notAdmin.guard';
import { AccessFormGuardAdmin } from './guards/accesFormAdmin.guard';
import { AccessFormGuardUser } from './guards/accesFormUser.guard';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent, canActivate: [notAuthGuard] },
  {
    path: 'glossaire',
    component: GlossaireComponent,
    canActivate: [AuthGuard],
  },
  { path: 'forms', component: FormsComponent, canActivate: [AuthGuard] },
  { path: 'esg', component: EsgFormComponent, canActivate:[AuthGuard, NotAdminGuard, AccessFormGuardUser], data: { module: 'ESG' } },
  { path: 'odd', component: OddFormComponent, canActivate: [AuthGuard, NotAdminGuard, AccessFormGuardUser], data: { module: 'ODD' } },
  {path: 'esg/:id', component: EsgFormComponent, canActivate: [RoleGuard, AccessFormGuardAdmin], data: { module: 'ESG' }},
  {path: 'odd/:id', component: OddFormComponent, canActivate: [RoleGuard, AccessFormGuardAdmin], data: { module: 'ODD' }},
  {
    path: 'account-management',
    component: AccountManagementComponent,
    canActivate: [RoleGuard],
  },
  {
    path: 'companies',
    component: CompanyManagementComponent,
    canActivate: [RoleGuard],
  },
  {
    path: 'companies/:id',
    component: CompanyDetailComponent,
    canActivate: [RoleGuard],
  },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard, NotAdminGuard] },
  { path: 'rapport', component: ReportComponent, canActivate: [AuthGuard] },
];
