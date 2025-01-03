import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.services';
import { Company } from '../../models/company.model';
import {
  ReactiveFormsModule,
  FormGroup,
  FormBuilder,
  Validators,
} from '@angular/forms';

@Component({
  selector: 'app-profile',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
})
export class ProfileComponent implements OnInit {
  user: { userId: number; login: string; isAdmin: boolean } | undefined;

  isLoggedIn: boolean = false;
  isAdmin: boolean = false;

  login: string = '';
  company: Company | undefined;

  showPasswordForm: boolean = false;
  passwordForm: FormGroup;
  companyForm: FormGroup;

  sure: boolean = false;

  constructor(
    private userService: UserService,
    private router: Router,
    private authService: AuthService,
    private formBuilder: FormBuilder
  ) {
    this.passwordForm = this.formBuilder.group({
      newPassword: ['', Validators.required],
      oldPassword: ['', Validators.required], 
    });
    this.companyForm = this.formBuilder.group({
      name: [this.company?.name || ''],
      address: [this.company?.address || ''],
      phoneNumber: [this.company?.phoneNumber || ''],
      hasInstallation: [this.company?.hasInstallation || false],
      ownsInstallation: [this.company?.ownsInstallation || false],
      hasProduct: [this.company?.hasProduct || false],
      workers: [this.company?.workers || 0]
    });
  }

  async ngOnInit() {
    this.authService.isLoggedIn$.subscribe(
      (loggedIn) => (this.isLoggedIn = loggedIn)
    );
    if (!this.isLoggedIn) {
      this.router.navigate(['/login']);
    } else {
      this.user = await this.userService.getUser();
      this.company = await this.userService.getCompany(this.user.userId);
      this.laodCompany();
      if (this.company) {
        this.companyForm.patchValue({
          name: this.company.name,
          address: this.company.address,
          phoneNumber: this.company.phoneNumber,
          hasInstallation: this.company.hasInstallation,
          ownsInstallation: this.company.ownsInstallation,
          hasProduct: this.company.hasProduct,
          workers: this.company.workers
        });
      }
      
    }
  }

  togglePasswordForm() {
    this.showPasswordForm = !this.showPasswordForm;
  }

  async modifyPassword() {
    const oldPassword = this.passwordForm.get('oldPassword')?.value;
    const newPassword = this.passwordForm.get('newPassword')?.value;
    await this.userService.modifyPassword(oldPassword, newPassword);
    this.passwordForm.reset();
    this.showPasswordForm = false;
  }

  updateInfos() {
    if (this.companyForm.valid) {

      const updatedCompany = this.companyForm.value;
      updatedCompany.company_id = this.company?.company_id;

      try{

        this.userService.updateCompany(updatedCompany);

      }catch(error){
        if (error instanceof Error) {
          console.error('Error Message :', error.message);
        } else {
          console.error('Erreur inconnue :', error);
        }
      }
      this.laodCompany();
    }else{
    }
  }


  async laodCompany() {
    this.authService.isLoggedIn$.subscribe(loggedIn => this.isLoggedIn = loggedIn);
    this.authService.isAdmin$.subscribe(admin => this.isAdmin = admin);
    this.company = await this.userService.getCompany((await this.userService.getUser()).userId);
    if (this.company) {
      this.companyForm.patchValue({
        name: this.company.name,
        address: this.company.address,
        phoneNumber: this.company.phoneNumber,
        hasInstallation: this.company.hasInstallation,
        ownsInstallation: this.company.ownsInstallation,
        hasProduct: this.company.hasProduct,
        workers: this.company.workers
      });
    }
  }

  toggleSure() {
    this.sure = !this.sure;
  }

  async deleteAccount() {
    await this.userService.deleteAccount();
    this.authService.logout();
    this.router.navigate(['']);
  }

}
