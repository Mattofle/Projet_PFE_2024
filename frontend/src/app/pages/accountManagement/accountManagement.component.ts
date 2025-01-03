import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; // Import du CommonModule
import { FormsModule, FormGroup, FormBuilder, Validators, ReactiveFormsModule,} from '@angular/forms';
import {AccountManagementService } from '../../services/accountManagement.service';
import { ToastrService } from 'ngx-toastr'
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-accountManagement',
  imports: [CommonModule, FormsModule,ReactiveFormsModule],
  templateUrl: './accountManagement.component.html',
  styleUrl: './accountManagement.component.css',
})
export class AccountManagementComponent implements OnInit {

  user = {
    userId: 0,
    login: '',
    isAdmin: true
  }
  selectedCountryCode: string = '+32'; 
  countries = [
    { name: 'Belgique', dialCode: '+32', flag: '🇧🇪' },
    { name: 'France', dialCode: '+33', flag: '🇫🇷' },
    { name: 'États-Unis', dialCode: '+1', flag: '🇺🇸' },
  ];

  admins: any[] = [];
  newAdmin = {
    login: '',
    password: '',
    is_admin: true
  };

  newCompany = {
    name: '',
    street: '', 
    city: '',   
    postalCode: '', 
    address: '', 
    phoneNumber: '',
    login: '',
    password: '',
    hasInstalation: true,
    workers: 0,
    hasProduct: true,
    ownsInstalation: true,
  };

  showPasswordForm: boolean = false;
  passwordForm: FormGroup;

  constructor(private accountManagementService: AccountManagementService, private toastr: ToastrService, private userService: UserService,private formBuilder: FormBuilder)  {
    this.passwordForm = this.formBuilder.group({
      newPassword: ['', Validators.required],
      oldPassword: ['', Validators.required],
      confirmNewPassword: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadAdmins();
  this.userService.getUser().then((user) => {
    this.user.userId = user.userId;
    this.user.login = user.login;
    this.user.isAdmin = user.isAdmin;
  }).catch((error) => {
    console.error('Erreur lors de la récupération des informations utilisateur :', error);
  });
  }

  async loadAdmins() {
    try {
      this.admins = await this.accountManagementService.getAllAdmins();
     
    } catch (error: any) {
      console.error('Erreur lors du chargement des administrateurs:', error.message);
    }
  }

  async createUser() {
    try {
      const regexEmail = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
      if(!regexEmail.test(this.newAdmin.login)){
        this.toastr.error('Veuillez fournir une adresse mail correcte.', 'Erreur');
        return;
      }
      if(this.newAdmin.login === '' || this.newAdmin.password === ''){
        this.toastr.error('Veuillez remplir tous les champs.', 'Erreur');
        return;
      }
      const response = await this.accountManagementService.createUser(this.newAdmin);
      if (response.ok) {
      
        this.toastr.success('Administrateur créé avec succès !', 'Succès');
        await this.loadAdmins();
      } else {
        console.error('Erreur lors de la création :', response.statusText);
        this.toastr.error('Erreur lors de la création de l\'utilisateur.', 'Erreur');
      }
    } catch (error: any) {
      console.error('Erreur lors de la création de l\'utilisateur:', error.message);
    }
  }

  async createCompany() {
    try {
      const regexEmail = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
      if(!regexEmail.test(this.newCompany.login)){
        this.toastr.error('Veuillez fournir une adresse mail correcte.', 'Erreur');
        return;
      }
      const regexPhoneNumber = /^[0-9]{6,14}$/; 
      if (!regexPhoneNumber.test(this.newCompany.phoneNumber)) {
        this.toastr.error('Veuillez fournir un numéro de téléphone valide.', 'Erreur');
        return;
      }
      if(this.newCompany.name === '' || this.newCompany.street === '' || this.newCompany.postalCode === '' ||
        this.newCompany.city === '' || this.newCompany.login === '' || this.newCompany.password === '' ||
        this.newCompany.phoneNumber === ''
      ){
        this.toastr.error('Veuillez remplir tous les champs.', 'Erreur');
        return;
      }
      const fullPhoneNumber = `${this.selectedCountryCode}${this.newCompany.phoneNumber}`;
      this.newCompany.phoneNumber = fullPhoneNumber;

      this.newCompany.address = `${this.newCompany.street}, ${this.newCompany.city}, ${this.newCompany.postalCode}`;
      const { street, city, postalCode, ...companyDataToSend } = this.newCompany;

      const response = await this.accountManagementService.createCompany(companyDataToSend);
      if (response.ok) {
        this.toastr.success('Entreprise créée avec succès !', 'Succès');
       
      } else {
        
        console.error('Erreur lors de la création :', response.statusText);
      }
    } catch (error: any) {
      console.error('Erreur lors de la création de l\'entreprise:', error.message);
    }
  }

  async deleteAdmin(userId: number) {
    try {
      await this.accountManagementService.deleteUser(userId);
      this.admins = this.admins.filter(admin => admin.userId !== userId);
      this.toastr.success(`Administrateur supprimé avec succès.`, 'Succès');
      
    } catch (error) {
      console.error('Erreur lors de la suppression de l\'administrateur :', error);
    }
  }

  async modifyPassword() {
    try {
        const oldPassword = this.passwordForm.get('oldPassword')?.value;
        const newPassword = this.passwordForm.get('newPassword')?.value;
        const confirmNewPassword = this.passwordForm.get('confirmNewPassword')?.value;
        if(oldPassword === newPassword){
          this.toastr.error('Veuillez entrez un mot de passe différent de l\'ancien.', 'Erreur');
          return;
        }
        if(newPassword !== confirmNewPassword){
          this.toastr.error('Confirmer mot de passe et nouveau mot de passe sont différents! ', 'Erreur');
          return;
        }
        const response = await this.userService.modifyPassword(oldPassword, newPassword);
        if(response.ok){
          this.toastr.success('Mot de passe modifié !', 'Succès');
        }
        this.passwordForm.reset();
        this.showPasswordForm = false;
    }catch (error) {
      console.error('Erreur :', error);
    }
  }

  togglePasswordForm() {
  
    this.showPasswordForm = !this.showPasswordForm;
  }
}
