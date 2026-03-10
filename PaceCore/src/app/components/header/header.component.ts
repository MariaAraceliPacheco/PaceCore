import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ViewportScroller } from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  constructor(private scroller: ViewportScroller) {}

  scrollToAbout() {
    // Smooth scroll to 'about' section
    document.getElementById('about')?.scrollIntoView({ behavior: 'smooth' });
  }
}
