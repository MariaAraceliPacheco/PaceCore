export interface Usuario {
    id: number;
    nombre: string;
    descripcion?: string;
    email: string;
    password: string;
    rol: string;
    fechaCreacion: string;
    altura?: number;
    peso?: number;
    edad?: number;
}