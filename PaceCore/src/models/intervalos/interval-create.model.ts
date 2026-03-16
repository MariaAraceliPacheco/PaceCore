export interface IntervalCreate {
    tipo_actividad_id: number;
    distancia: number;
    duracion: string;
    desnivel?: number;
    fcMedia?: number;
    fcMaxima?: number;
    zona_alcanzada?: number;
}
