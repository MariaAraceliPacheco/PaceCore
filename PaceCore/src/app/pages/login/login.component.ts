import { Component } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [RouterLink, FormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  email = '';
  password = '';
  loginError = false;

  constructor(private router: Router, private authService: AuthService) { }

  //onSubmit es un metodo que se ejecuta cuando se envia el formulario
  //el metodo subscribe() se utiliza para suscribirse al observable que devuelve el metodo login()
  //el metodo subscribe() recibe un objeto con dos metodos: next() y error()
  //el metodo next() se ejecuta cuando el observable devuelve un valor
  //el metodo error() se ejecuta cuando el observable devuelve un error
  onSubmit(event: Event) {
    this.loginError = false; // Reset error state
    this.authService.login(this.email, this.password).subscribe({
      next: () => {
        this.router.navigate(['/dashboard']);
      },
      error: (error) => {
        this.loginError = true;
        console.error('Login failed:', error);
      }
    });
  }
}
