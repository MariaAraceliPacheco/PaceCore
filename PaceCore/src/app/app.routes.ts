import { Routes } from '@angular/router';
import { AuthGuard } from './guards/auth.guard';
import { DashboardLayoutComponent } from './layouts/dashboard-layout/dashboard-layout.component';

export const routes: Routes = [
    {
        path: '',
        loadComponent: () => import('./pages/landing/landing.component').then(m => m.LandingComponent)
    },
    {
        path: 'login',
        loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent)
    },
    {
        path: 'register',
        loadComponent: () => import('./pages/register/register.component').then(m => m.RegisterComponent)
    },
    {
        path: 'dashboard',
        component: DashboardLayoutComponent,
        canActivate: [AuthGuard],
        children: [
            {
                path: '',
                loadComponent: () => import('./pages/dashboard/overview/overview.component').then(m => m.OverviewComponent)
            },
            {
                path: 'workouts',
                loadComponent: () => import('./pages/dashboard/workouts/workouts.component').then(m => m.WorkoutsComponent)
            },
            {
                path: 'statistics',
                loadComponent: () => import('./pages/dashboard/statistics/statistics.component').then(m => m.StatisticsComponent)
            },
            {
                path: 'profile',
                loadComponent: () => import('./pages/dashboard/profile/profile.component').then(m => m.ProfileComponent)
            },
            {
                path: 'explorar',
                loadComponent: () => import('./pages/dashboard/explorar/explorar.component').then(m => m.ExplorarComponent)
            }
        ]
    }
];
