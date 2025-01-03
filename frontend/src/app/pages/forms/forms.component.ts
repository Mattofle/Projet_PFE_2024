import { Component } from '@angular/core';
import { RouterModule} from '@angular/router';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user.service';
import { CompanyService } from '../../services/company.service';


@Component({
  selector: 'app-forms',
  templateUrl: './forms.component.html',
  styleUrls: ['./forms.component.css'],
  imports: [RouterModule, CommonModule]
})
export class FormsComponent {
  constructor(
    private companyService: CompanyService, 
    private userService: UserService,
    private router: RouterModule, 
    private commonModule: CommonModule
  ) { }

  esgStatus = '';
  oddStatus = '';

  ngOnInit(): void {
    this.loadFormsCompany();
  }

  async loadFormsCompany(): Promise<void> {
    try {
      const company = await this.userService.getUser();
      if (company) {
        const esgModule = await this.companyService.getModuleCompany(company.userId, 'ESG');
        if(esgModule.date_form !== null) {
          this.esgStatus = 'Completed';
        }
        const oddModule = await this.companyService.getModuleCompany(company.userId, 'ODD');
        if(oddModule.date_form !== null) {
          this.oddStatus = 'Completed';
        }
      }
    } catch (error) {
      console.error('Error loading modules:', error);
    }
  }
}
