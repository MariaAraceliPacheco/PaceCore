import { Pipe, PipeTransform } from '@angular/core';
/* un pipe permite transformar un valor directamente en el html de forma reutilizable */
@Pipe({
  name: 'formatSeconds',
  standalone: true
})
export class FormatSecondsPipe implements PipeTransform {

  transform(seconds: number | undefined): string {
    if (seconds === undefined || seconds === null) return '00:00:00';

    const hours = Math.floor(seconds / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);
    const secs = seconds % 60;

    // Usamos padStart para asegurar que siempre haya dos dígitos (ej: 05 en lugar de 5)
    const hDisplay = hours.toString().padStart(2, '0');
    const mDisplay = minutes.toString().padStart(2, '0');
    const sDisplay = secs.toString().padStart(2, '0');

    return `${hDisplay}:${mDisplay}:${sDisplay}`;

  }
}
