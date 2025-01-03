import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { RecursiveModuleEsgComponent } from '../app-module-esg/recursive-module-esg.component';
import { CommonModule } from '@angular/common';
import { QuestionEsgComponent } from '../question-esg/question-esg.component';
import { UserService } from '../../../services/user.service';
import { QuestionService } from '../../../services/question.service';
import { CompanyService } from '../../../services/company.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-esg-form',
  templateUrl: './esg-form.component.html',
  styleUrls: ['./esg-form.component.css'],
  imports: [RecursiveModuleEsgComponent, CommonModule],
  encapsulation: ViewEncapsulation.None,
})
export class EsgFormComponent implements OnInit {
  parentModules: any[] = [];
  modulesDisplayer: any[] = [];
  currentModuleIndex: number = 0; // Tracks the current module
  isLoading = true;
  isSidebarCollapsed = false;
  isAdmin = false;
  companyId: number = 0;

  constructor(
    private questionService: QuestionService,
    private userService: UserService,
    private companyService: CompanyService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.initializeModules();
  }

  private async initializeModules(): Promise<void> {
    try {
      this.isLoading = true;

      let user = await this.userService.getUser();
      if(user.isAdmin){
        this.isAdmin = true;
        const userId = this.route.snapshot.paramMap.get('id');
        this.companyId = Number(userId);
       
        user = await this.userService.getUserById(Number(userId));
      }
      this.parentModules = await this.questionService.getQuestionsWithAnswers(
        'ESG',
        user.userId
      );
      this.loadModulesDisplayer();
      
    } catch (error) {
      console.error('Error loading modules:', error);
    } finally {
      this.isLoading = true; // Ensure the loader is visible
      this.isLoading = false; // Hide the loader after 10 seconds
      this.scrollToTopWithOffset(0.08); // Scroll to the top with an offset
    }
  }

  async handleAnswerSelection(event: {
    parentIndex: number;
    childIndex: number;
    questionId: number;
    answer: string;
  }): Promise<void> {
    const { parentIndex, childIndex, questionId, answer } = event;

    const token = localStorage.getItem('userToken');
    let user = await this.userService.getUser();
    if(user.isAdmin){
      const userId = this.route.snapshot.paramMap.get('id');
      user = await this.userService.getUserById(Number(userId));
    }
    if (token) {
      await this.questionService.answerQuestion(answer, questionId, token, user.userId);

      // Update answers after submission
    } else {
      console.error('User token is missing');
    }
  }

  loadModulesDisplayer(): void {
    this.modulesDisplayer = this.parentModules.flatMap(
      (module) => module.children || []
    );
  }

  showNextModule(): void {
    if (this.currentModuleIndex < this.modulesDisplayer.length - 1) {
      this.currentModuleIndex++;
      this.updateModuleDisplay();
    } else{
      if(this.isAdmin){
        this.validateForm();
      }
      this.endForm();
    }
  }

  private async endForm(): Promise<void> {
    const user = await this.userService.getUser();
    await this.questionService.submitFormESG(user.userId);
  }

  showPreviousModule(): void {
    if (this.currentModuleIndex > 0) {
      this.currentModuleIndex--;
      this.updateModuleDisplay();
    }
  }

  scrollToModule(moduleId: number): void {
    this.currentModuleIndex = moduleId - 22;
    this.updateModuleDisplay();
  }

  updateModuleDisplay(): void {
    this.initializeModules(); // Update answers when switching modules

  }

  scrollToTopWithOffset(pourcentage: number): void {
    const offset = window.innerHeight * pourcentage; // 10% offset
    window.scrollTo({
      top: 0 + offset,
      behavior: 'smooth',
    });
  }

  toggleSidebar(): void {
    this.isSidebarCollapsed = !this.isSidebarCollapsed;
  }

  async validateForm(): Promise<void> {
    const userId = this.route.snapshot.paramMap.get('id');
   
    const user = await this.userService.getUserById(Number(userId));
    const module = await this.companyService.getModuleCompany(user.userId, 'ESG');
    await this.companyService.validateModuleCompany(user.userId, module.moduleId);
  }
}
