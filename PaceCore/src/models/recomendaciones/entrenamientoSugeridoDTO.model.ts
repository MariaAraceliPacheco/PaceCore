import { BloqueEntrenamiento } from "./bloqueEntrenamiento.model";

export interface EntrenamientoSugeridoDTO {
    id: number;
    titulo: string;
    descripcion: string;
    fechaGeneracion: Date;
    estado: string;
    bloques: BloqueEntrenamiento[];
}