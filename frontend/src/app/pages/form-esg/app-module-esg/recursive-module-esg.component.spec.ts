import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecursiveModuleEsgComponent } from './recursive-module-esg.component';

describe('RecursiveModuleComponent', () => {
  let component: RecursiveModuleEsgComponent;
  let fixture: ComponentFixture<RecursiveModuleEsgComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecursiveModuleEsgComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecursiveModuleEsgComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
