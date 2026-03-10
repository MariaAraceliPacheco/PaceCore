import { Intervalo } from "./intervalo.model";
import { TipoActividad } from "./tipo-actividad.model";

export interface Entreno {
    id: number;
    titulo: string;
    descripcion: string | null;
    distancia_entreno: number;
    tiempo_total_entreno: any; // Es un objeto {hour, minute, second...}
    fecha: string;
    desnivel_entreno: number | null;
    tipo_actividad_entreno: TipoActividad;
    intervalos: Intervalo[];
    id_usuario: number;
    fcMedia?: number;
    fcMaxima?: number;
    zona_alcanzada?: number;
    ritmoMedio?: number;
}
