import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecursiveModuleOddComponent } from './recursive-module-odd.component';

describe('RecursiveModuleComponent', () => {
  let component: RecursiveModuleOddComponent;
  let fixture: ComponentFixture<RecursiveModuleOddComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecursiveModuleOddComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecursiveModuleOddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
