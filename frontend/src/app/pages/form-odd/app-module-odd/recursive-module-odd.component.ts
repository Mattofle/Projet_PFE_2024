import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { QuestionOddComponent } from '../question-odd/question-odd.component';


@Component({
  selector: 'app-recursive-module-odd',
  templateUrl: './recursive-module-odd.component.html',
  styleUrls: ['./recursive-module-odd.component.css'],
  imports: [CommonModule, QuestionOddComponent],
})
export class RecursiveModuleOddComponent implements OnInit {
  @Input() module: any; // Module (parent, child, or question module)
  @Input() parentIndex: number | undefined;
  @Input() childIndex: number | undefined;
  @Output() answerSelected = new EventEmitter<any>();
  @Input() questionId: number | undefined;
  @Input() companyId: number | undefined

  ngOnInit(): void {
    // Initialization logic, if required
  }

  /**
   * Get CSS class for the module container based on depth level.
   */
  getContainerClass(): string {
    const level = this.module.modulePath.split('.').length;
    return `container-lvl${level}`; // Adjust style based on module depth
  }

  /**
   * Get CSS class for the title based on depth level.
   */
  getTitleClass(): string {
    const level = this.module.modulePath.split('.').length;
    return `title-lvl${level}`;
  }

  /**
   * Emits selected answers to the parent component.
   */
  recordAnswer(questionId: number | undefined, answer: string): void {
    this.answerSelected.emit({
      questionId,
      answer,
    });
  }

  /**
   * Determines if the module is a leaf (contains questions).
   */
  isLeafModule(): boolean {
    return this.module.questions && this.module.questions.length > 0;
  }

  /**
   * Returns child modules for recursive rendering.
   */
  getChildModules(): any[] {
    return this.module.children || [];
  }

  /**
   * Handles answer selection events for questions.
   */
  handleAnswerSelection(event: any): void {
    const { questionId, answer } = event;

    if (!questionId) {
      console.error('Invalid questionId received');
      return;
    }

    // If the answer is an array, get the last item; otherwise, use the string directly
    const selectedAnswer = Array.isArray(answer)
      ? answer[answer.length - 1]
      : answer;

    this.recordAnswer(questionId, selectedAnswer); // Bubble up the answer selection
  }
}
