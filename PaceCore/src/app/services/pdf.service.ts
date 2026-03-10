import { Injectable } from '@angular/core';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import { EntrenamientoSugeridoDTO } from '../../models/recomendaciones/entrenamientoSugeridoDTO.model';

@Injectable({
    providedIn: 'root'
})
export class PdfService {

    constructor() { }

    exportarEntrenamiento(recomendacion: EntrenamientoSugeridoDTO): void {
        const doc = new jsPDF();
        const primaryColor: [number, number, number] = [252, 76, 2]; // PaceCore Orange
        const darkColor: [number, number, number] = [31, 41, 55]; // Dark Gray

        // 1. Header & Branding
        doc.setFillColor(primaryColor[0], primaryColor[1], primaryColor[2]);
        doc.rect(0, 0, 210, 40, 'F');

        doc.setTextColor(255, 255, 255);
        doc.setFontSize(24);
        doc.setFont('helvetica', 'bold');
        doc.text('PaceCore', 15, 20);

        doc.setFontSize(10);
        doc.setFont('helvetica', 'normal');
        doc.text('Tu asistente personal de entrenamiento', 15, 30);

        doc.setFontSize(12);
        doc.text(new Date().toLocaleDateString(), 170, 20);

        // 2. Title & Description
        doc.setTextColor(darkColor[0], darkColor[1], darkColor[2]);
        doc.setFontSize(20);
        doc.setFont('helvetica', 'bold');
        doc.text(recomendacion.titulo, 15, 55);

        doc.setFontSize(11);
        doc.setFont('helvetica', 'normal');
        const splitDescription = doc.splitTextToSize(recomendacion.descripcion, 180);
        doc.text(splitDescription, 15, 65);

        // 3. Workout Blocks Table
        const tableStartY = 75 + (splitDescription.length * 5);

        const tableData = recomendacion.bloques.map((b, index) => [
            `Z${b.zonaObjetivo}`,
            b.fase.charAt(0).toUpperCase() + b.fase.slice(1),
            b.descripcion,
            this.formatPace(b.ritmoObjetivo),
            this.formatDuration(b.duracionSegundos)
        ]);

        autoTable(doc, {
            startY: tableStartY,
            head: [['Zona', 'Fase', 'Descripción', 'Ritmo', 'Duración']],
            body: tableData,
            theme: 'striped',
            headStyles: {
                fillColor: primaryColor,
                textColor: [255, 255, 255],
                fontStyle: 'bold',
                halign: 'center'
            },
            columnStyles: {
                0: { cellWidth: 20, halign: 'center', fontStyle: 'bold' },
                1: { cellWidth: 30 },
                2: { cellWidth: 'auto' },
                3: { cellWidth: 25, halign: 'center' },
                4: { cellWidth: 25, halign: 'center' }
            },
            styles: {
                fontSize: 10,
                cellPadding: 5
            },
            didParseCell: (data) => {
                if (data.section === 'body' && data.column.index === 0) {
                    const zoneText = Array.isArray(data.cell.text) ? data.cell.text[0] : (data.cell.text as string);
                    const zone = parseInt(zoneText.substring(1));
                    const zoneColors: { [key: number]: [number, number, number] } = {
                        1: [50, 139, 223],
                        2: [48, 211, 26],
                        3: [255, 217, 0],
                        4: [255, 126, 86],
                        5: [255, 0, 0]
                    };
                    const color = zoneColors[zone] || primaryColor;
                    data.cell.styles.textColor = color;
                }
            }
        });

        // 4. Footer
        const finalY = (doc as any).lastAutoTable.finalY + 20;
        doc.setFontSize(10);
        doc.setTextColor(150, 150, 150);
        doc.text('Entrena con inteligencia. Sigue superándote.', 105, finalY, { align: 'center' });
        doc.text('www.pacecore.com', 105, finalY + 7, { align: 'center' });

        // 5. Save PDF
        doc.save(`Entrenamiento_${recomendacion.titulo.replace(/\s+/g, '_')}.pdf`);
    }

    private formatDuration(seconds: number): string {
        const hours = Math.floor(seconds / 3600);
        const minutes = Math.floor((seconds % 3600) / 60);
        const secs = seconds % 60;

        if (hours > 0) {
            return `${hours}:${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
        }
        return `${minutes}:${secs.toString().padStart(2, '0')}`;
    }

    private formatPace(ritmo: number | string | undefined): string {
        if (!ritmo) return '-';

        // Si ya tiene formato de tiempo (contiene ':'), lo devolvemos tal cual
        if (typeof ritmo === 'string' && ritmo.includes(':')) return ritmo;

        const numValue = typeof ritmo === 'string' ? parseFloat(ritmo) : ritmo;
        if (isNaN(numValue)) return '-';

        const mins = Math.floor(numValue);
        const secs = Math.round((numValue - mins) * 60);
        return `${mins}:${secs < 10 ? '0' : ''}${secs}`;
    }
}
