export interface ZonasResponseDTO {
    id: number;
    usuario_id: number;
    nombre_zona: string;
    descripcion?: string;
    fc_minima: number;
    fc_maxima: number;
    numero_zona: number;
}