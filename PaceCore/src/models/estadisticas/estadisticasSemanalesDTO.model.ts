import { TipoActividad } from "../tipoActividad/tipo-actividad.model";

export interface EstadisticasSemanalesDTO {
 fecha: string;
 distancia: number;
 tipoActividad: TipoActividad;   
 fcMedia: number;
 fcMaxima: number;
}