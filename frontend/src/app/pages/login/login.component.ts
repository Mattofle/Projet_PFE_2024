// login.component.ts
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import './login.component.css'
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MyApiService } from '../../services/api.service';
import { AuthService } from '../../services/auth.services';

@Component({
  selector: 'app-login',
  imports: [
    ReactiveFormsModule,
    CommonModule,
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  loginForm: FormGroup;
  missingCredentials = false;
  errorMsg = '';
  

  constructor(
    private formBuilder: FormBuilder,
    private api: MyApiService,
    private router: Router,
    private authService: AuthService
  ) {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  async onSubmit() {
    if (this.loginForm.valid) {
      const username = this.loginForm.get('username')?.value;
      const password = this.loginForm.get('password')?.value;  

      try {
        const token = await this.api.login({
          login: username,
          password: password,
        });

        // Utilisez le service d'authentification pour gérer la connexion
        this.authService.login(token); // Passez `true` si l'utilisateur est admin

        this.router.navigate(['']);
        this.loginForm.reset();
        this.missingCredentials = false;
        this.errorMsg = '';
        
      } catch (error) {
        if (error instanceof Error) {
          console.error('Erreur lors de la connexion :', error.message);
          this.errorMsg = error.message;
        } else {
          console.error('Erreur inconnue :', error);
        }
      }
      this.loginForm.reset();
      
    } else {
      this.missingCredentials = true;
    }
  }
}