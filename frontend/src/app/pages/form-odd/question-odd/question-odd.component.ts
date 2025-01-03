import { CommonModule } from '@angular/common';
import {
  Component,
  Input,
  Output,
  EventEmitter,
  OnChanges,
  SimpleChanges,
  OnInit,
  ChangeDetectorRef,
  input,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { QuestionService } from '../../../services/question.service';

@Component({
  selector: 'app-question-odd',
  templateUrl: './question-odd.component.html',
  styleUrls: ['./question-odd.component.css'],
  imports: [CommonModule, FormsModule],
})
export class QuestionOddComponent implements OnChanges {
  constructor(
    private service: QuestionService,
    private cdr: ChangeDetectorRef
  ) {}

  @Input() question: string = '';
  @Input() answers: { answer_id: string; text: string }[] = []; // Answers contain answer_id and text
  @Input() type: string = ''; // Define question type (ONE, MULTIPLE, OPEN)
  @Input() questionNumber: number = 0;
  @Input() moduleIndex: number | undefined;
  @Input() userAnswer: string | string[] = ''; // Pre-selected answer(s) (only answer_id(s))
  @Input() questionId: number | undefined;
  @Input() questionComment: string | undefined;
  @Input() companyId: number | undefined

  @Output() answerSelected = new EventEmitter<{
    questionNumber: number;
    questionId: number | undefined;
    answer: string | string[];
  }>();
  // Emit the selected answer(s) along with question index and id
  isAddingComment: boolean = false; // Tracks if the user is adding a comment
  newComment: string = ''; // Temporary storage for new comment
  selectedAnswers: string[] = []; // For MULTIPLE selection (stores answer_id(s))
  openAnswer: string = ''; // For OPEN question type

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['userAnswer']) {
      this.initializeAnswer(changes['userAnswer'].currentValue);
    }
  }

  private initializeAnswer(userAnswer: string | string[]): void {
    if (this.type === 'MULTIPLE' && Array.isArray(userAnswer)) {
      // For MULTIPLE choice questions, initialize selected answers from userAnswer array
      this.selectedAnswers = userAnswer;
    } else if (this.type === 'ONE' && typeof userAnswer === 'string') {
      // For ONE choice questions, initialize the selected answer from userAnswer
      this.selectedAnswers = [userAnswer];
    } else if (this.type === 'OPEN' && typeof userAnswer === 'string') {
      // For OPEN questions, initialize the openAnswer from userAnswer
      this.openAnswer = userAnswer;
    } else if (this.type === 'OPEN' && userAnswer === '') {
      // Ensure empty string doesn't break the initialization
      this.openAnswer = '';
    }
  }

  // Check if an answer is selected (for both ONE and MULTIPLE choice)
  isAnswerSelected(answer: { answer_id: string; text: string }): boolean {
    if (this.type === 'MULTIPLE') {
      return this.selectedAnswers.includes(answer.answer_id);
    }

    return (
      this.selectedAnswers.length > 0 &&
      this.selectedAnswers[0] === answer.answer_id
    );
  }

  // Handle the selection of an answer
  selectAnswer(
    answer: { answer_id: string },
    question_id: number | undefined
  ): void {
    if (this.type === 'ONE') {
      if (this.selectedAnswers.includes(answer.answer_id)) {
        this.selectedAnswers = []; // Clear the selection
        this.service.deleteAnswer(parseInt(answer.answer_id, 10), question_id, this.companyId); // Delete the answer
      } else {
     
        this.selectedAnswers = [answer.answer_id];
        this.userAnswer = answer.answer_id;
        this.emitAnswer(); // Emit after updating the selection
      }
      this.cdr.detectChanges(); // Force the UI to update
    } else if (this.type === 'MULTIPLE') {
      const index = this.selectedAnswers.indexOf(answer.answer_id);

      if (index === -1) {
        this.selectedAnswers.push(answer.answer_id);
        this.emitAnswer();
      } else {
        this.selectedAnswers.splice(index, 1);
        this.service.deleteAnswer(parseInt(answer.answer_id, 10), question_id, this.companyId);
      }
    }
  }

  // Emit the selected answer(s) for OPEN type questions
  emitOpenAnswer(): void {
    this.openAnswer = this.openAnswer.trimEnd(); // Remove trailing newline or spaces
    this.emitAnswer();
  }

  // Emit the selected answer(s) back to the parent component
  private emitAnswer(): void {
    const answerToEmit =
      this.type === 'MULTIPLE'
        ? this.selectedAnswers // Emit an array of selected answer_ids for MULTIPLE questions
        : this.type === 'OPEN'
        ? this.openAnswer // Emit open-ended answer for OPEN questions
        : this.selectedAnswers[0]; // Emit the selected answer_id for ONE choice questions

    this.answerSelected.emit({
      questionNumber: this.questionNumber, // Include the question index
      questionId: this.questionId, // Include the question ID
      answer: answerToEmit, // Emit the selected answer(s)
    });
  }
}
