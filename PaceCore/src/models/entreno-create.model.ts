import { IntervalCreate } from "./interval-create.model";

export interface EntrenoCreate {
    id_usuario?: number;
    titulo: string;
    fecha: string;
    distancia: number;
    tiempo_total: string;
    desnivel?: number;
    descripcion?: string;
    tipo_actividad_id: number;
    fcMedia?: number;
    fcMaxima?: number;
    intervalos?: IntervalCreate[];
}
