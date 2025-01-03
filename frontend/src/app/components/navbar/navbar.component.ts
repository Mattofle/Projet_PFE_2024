import { Component, OnInit, HostListener } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.services';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  isLoggedIn: boolean = false;
  isAdmin: boolean = false;
  isNavbarVisible: boolean = true; // Initially visible
  isScrolled: boolean = false; // Tracks if the user has scrolled down

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.authService.isLoggedIn$.subscribe(loggedIn => (this.isLoggedIn = loggedIn));
    this.authService.isAdmin$.subscribe(admin => (this.isAdmin = admin));
    this.authService.checkAuthStatus();
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  @HostListener('window:scroll', [])
  onScroll(): void {
    const scrollPosition = window.scrollY || document.documentElement.scrollTop;
    const screenWidth = window.innerWidth; // Get screen width
  
    // Define responsiveness breakpoints
    const mobileBreakpoint = 6000; // Example: 768px for mobile screens
  
    // Adjust navbar visibility behavior based on scroll position and screen size
  
      // On mobile devices, always keep the navbar visible when at the top
      this.isNavbarVisible = scrollPosition <= 30;
    
  
    this.isScrolled = scrollPosition > 30; // Track if the page is scrolled
  }
  
  @HostListener('document:mousemove', ['$event'])
  onMouseMove(event: MouseEvent): void {
    if (this.isScrolled && event.clientY <= 50) {
      // Show navbar if hovered near the top when scrolled
      this.isNavbarVisible = true;
    } else if (this.isScrolled) {
      // Hide navbar when away from the hover zone
      this.isNavbarVisible = false;
    }
  }
}
