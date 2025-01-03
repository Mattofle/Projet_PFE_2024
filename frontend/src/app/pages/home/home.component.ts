import { Component, HostListener, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.services';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  imports: [RouterModule]
})
export class HomeComponent implements OnInit {

  isScrolledFirstContainer: boolean = false;
  isScrolledSecondContainer: boolean = false;
  isScrolledThirdContainer: boolean = false;

  constructor(private authService: AuthService, private router: Router) { }

  onStart() {
  
    const isLoggedIn = this.authService.isLoggedInSubject.getValue();
    const isAdmin = this.authService.isAdminSubject.getValue();
  
  
    if (isLoggedIn && !isAdmin) {
      this.router.navigate(['/forms']);
    } else if (isAdmin) {
      this.router.navigate(['/companies']);
    } else {
      this.router.navigate(['/login']);
    }
  }
  

  ngOnInit(): void {
    this.authService.checkAuthStatus();
  }

  @HostListener('window:scroll', [])
  onWindowScroll() {


    const firstContainer = document.querySelector('.first-container');
    if (firstContainer) {
      const firstOffset = firstContainer.getBoundingClientRect().top;
      this.isScrolledFirstContainer = firstOffset < window.innerHeight ;
    }

    const secondContainer = document.querySelector('.second-container');
    if (secondContainer) {
      const secondOffset = secondContainer.getBoundingClientRect().top;
      this.isScrolledSecondContainer = secondOffset < window.innerHeight;
    }

    const thirdContainer = document.querySelector('.third-container');
    if (thirdContainer) {
      const thirdOffset = thirdContainer.getBoundingClientRect().top;
      this.isScrolledThirdContainer = thirdOffset < window.innerHeight ;
    }
  }
}
