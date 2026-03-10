import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'ritmo',
    standalone: true
})
export class RitmoPipe implements PipeTransform {
    transform(value: number | string | undefined): string {
        if (!value) return '0:00';

        // Si el valor es una cadena, intentamos convertirlo a número
        const numValue = typeof value === 'string' ? parseFloat(value) : value;

        if (isNaN(numValue)) return '0:00';

        const mins = Math.floor(numValue);
        const secs = Math.round((numValue - mins) * 60);
        return `${mins}:${secs < 10 ? '0' : ''}${secs}`;
    }
}
