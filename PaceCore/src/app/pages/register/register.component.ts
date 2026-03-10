import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [RouterLink, CommonModule, FormsModule, MatSnackBarModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {


  constructor(
    private router: Router,
    private authService: AuthService,
    private snackBar: MatSnackBar
  ) { }

  step: number = 1;

  // Step 1 Data
  name: string = '';
  email: string = '';
  password: string = '';
  confirmPassword: string = '';

  // Step 2 Data
  weight: number | null = null;
  height: number | null = null;
  description: string = '';
  age: number | null = null;

  onSubmit(event: Event) {
    event.preventDefault();

    if (this.step === 1) {
      this.handleStep1();
    } else if (this.step === 2) {
      this.handleStep2();
      this.crearUsuario();
    }
  }

  handleStep1() {
    // Basic validation
    if (!this.name || !this.email || !this.password || !this.confirmPassword) {
      alert('Por favor completa todos los campos');
      return;
    }

    if (this.password !== this.confirmPassword) {
      alert('Las contraseñas no coinciden');
      return;
    }

    // Proceed to next step
    this.step = 2;
  }

  handleStep2() {
    // Basic validation
    if (!this.weight || !this.height) {
      alert('Por favor completa peso y altura');
      return;
    }

    const userData = {
      name: this.name,
      email: this.email,
      password: this.password,
      weight: this.weight,
      height: this.height,
      description: this.description,
      age: this.age
    };

    console.log('Final Registration Data:', userData);
   
  }

  crearUsuario() {
     this.authService.register(this.name, this.email, this.password, this.description, this.weight!, this.height!, this.age!).subscribe({
      next: () => {
        this.snackBar.open('¡Usuario creado correctamente!', 'Cerrar', {
          duration: 5000,
          horizontalPosition: 'center',
          verticalPosition: 'bottom',
        });
        this.router.navigate(['/login']);
      },
      error: (error) => {
        console.error('Registration failed:', error);
      }
    });
  }
}
