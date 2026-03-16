import { IntervalCreate } from "../intervalos/interval-create.model";

export interface EntrenoUpdate {
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
