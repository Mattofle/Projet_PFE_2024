import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuestionEsgComponent } from './question-esg.component';

describe('QuestionComponent', () => {
  let component: QuestionEsgComponent;
  let fixture: ComponentFixture<QuestionEsgComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuestionEsgComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(QuestionEsgComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
