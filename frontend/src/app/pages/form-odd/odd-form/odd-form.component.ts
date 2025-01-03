import { CommonModule } from '@angular/common';

import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { RecursiveModuleOddComponent } from '../app-module-odd/recursive-module-odd.component';
import { QuestionService } from '../../../services/question.service';
import { UserService } from '../../../services/user.service';
import { CompanyService } from '../../../services/company.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-odd-form',
  templateUrl: './odd-form.component.html',
  styleUrls: ['./odd-form.component.css'],
  imports: [RecursiveModuleOddComponent, CommonModule],
  encapsulation: ViewEncapsulation.None,
})
export class OddFormComponent implements OnInit {
  parentModules: any[] = [];
  isLoading = true;
  modulesDisplayer: any[] = [];
  isSidebarCollapsed = false;
  currentModuleIndex: number = 0; // Index of the currently displayed module
  isAdmin = false;
  companyId: number = 0;

  constructor(
    private questionService: QuestionService,
    private userService: UserService,
    private companyService: CompanyService,
    private route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.loadQuestionsAndAnswers();
  }

  async loadQuestionsAndAnswers(): Promise<void> {
    try {
      this.isLoading = true;
      let user = await this.userService.getUser();
      if(user.isAdmin === true){
        this.isAdmin = true;
        const userId = this.route.snapshot.paramMap.get('id');
        this.companyId = Number(userId);
        user = await this.userService.getUserById(Number(userId));
      }

      this.parentModules = await this.questionService.getQuestionsWithAnswers(
        'ODD',
        user.userId
      );
      
      this.modulesDisplayer = this.parentModules;

      
    } catch (error) {
      console.error('Error loading questions:', error);
    } finally {
      this.isLoading = false;
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
      await this.questionService.answerQuestion(answer, questionId, token,user.userId);
    } else {
      console.error('User token is missing');
      // Handle the case when token is missing (e.g., redirect to login or show error)
    }

    const parentModule = this.modulesDisplayer[parentIndex];
    if (parentModule) {
      const childModule = parentModule.children[childIndex];
      if (childModule) {
        const question = childModule.questions[questionId];
        if (question) {
          question.userAnswer = answer; // Update the selected answer
          
        }
      }
    }
  }

  showNextModule(): void {
    if (this.currentModuleIndex < this.modulesDisplayer.length - 1) {
      this.currentModuleIndex++;
      this.updateModuleDisplay();
    } else if(this.isAdmin){
      this.validateForm();
    } else {
      this.endForm();
    }
  }

  private async endForm(): Promise<void> {
    const user = await this.userService.getUser();
    await this.questionService.submitFormODD(user.userId);
  }

  showPreviousModule(): void {
    if (this.currentModuleIndex > 0) {
      this.currentModuleIndex--;
      this.updateModuleDisplay();
    }
  }

  updateModuleDisplay(): void {
    this.initializeModules(); // Update answers when switching modules
  }

  private async initializeModules(): Promise<void> {
    try {
      window.scrollTo({
        top: 0,
        behavior: 'auto',
      });
      this.isLoading = true;
      const user = await this.userService.getUser();
      this.parentModules = await this.questionService.getQuestionsWithAnswers(
        'ODD',
        user.userId
      );
    } catch (error) {
      console.error('Error loading modules:', error);
    } finally {
      this.isLoading = false;

      // Scroll to the top with a smooth scrolling behavior
    }
  }

  // Scrolls to the top of the page with a 10% offset
  scrollToTopWithOffset(): void {
    const offset = window.innerHeight * 0.1; // 10% of the viewport height
    window.scrollTo({
      top: 0 + offset, // Scroll to the top with an offset of 10% of the page height
      behavior: 'smooth', // Smooth scroll animation
    });
  }

  toggleSidebar() {
    this.isSidebarCollapsed = !this.isSidebarCollapsed; // Toggle the collapsed state
  }

  scrollToModule(moduleId: number): void {
    
    this.currentModuleIndex = moduleId - 3;
    this.scrollToTopWithOffset();
  }

  async validateForm(): Promise<void> {
    const userId = this.route.snapshot.paramMap.get('id');
    
    const user = await this.userService.getUserById(Number(userId));
    const module = await this.companyService.getModuleCompany(user.userId, 'ODD');
    await this.companyService.validateModuleCompany(user.userId, module.moduleId);
  }
}
