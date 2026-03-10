import { TipoActividad } from "./tipo-actividad.model";

export interface Intervalo {
    id: number;
    distancia: number;
    duracion: string;
    desnivel: number | null;
    tipo_actividad_id: TipoActividad;
    fcMedia?: number;
    fcMaxima?: number;
    zona_alcanzada?: number;
    ritmoMedio?: number;
}
