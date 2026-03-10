import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'activityIcon',
    standalone: true
})
export class ActivityIconPipe implements PipeTransform {

    transform(value: string | undefined): string {
        if (!value) return 'fa-solid fa-person-running';

        const val = value.toLowerCase();

        if (val.includes('trail')) {
            return 'fa-solid fa-mountain';
        } else if (val.includes('montaña')) {
            return 'fa-solid fa-mountain-sun';
        } else if (val.includes('bici') || val.includes('ciclismo')) {
            return 'fa-solid fa-bicycle';
        } else if (val.includes('correr') || val.includes('carrera')) {
            return 'fa-solid fa-person-running';
        } else if (val.includes('caminar')) {
            return 'fa-solid fa-person-walking';
        } else if (val.includes('trail') || val.includes('mountain')) {
            return 'fa-solid fa-mountain';
        }

        return 'fa-solid fa-heart-pulse';
    }

}
