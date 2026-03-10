import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ExplorarDetalleComponent } from './explorar-detalle/explorar-detalle.component';

@Component({
  selector: 'app-explorar',
  standalone: true,
  imports: [CommonModule, MatDialogModule],
  templateUrl: './explorar.component.html',
  styleUrl: './explorar.component.css'
})
export class ExplorarComponent {
  cards = [
    {
      id: 'zonas',
      titulo: 'Zonas de Entrenamiento',
      descripcion: 'Aprende qué son las zonas de frecuencia cardíaca y cómo usarlas para optimizar tu rendimiento.',
      imagen: 'assets/images/explorar/training_zones_hero.png',
      subtitulo: 'Entiende la intensidad de tus sesiones',
      contenido: [
        {
          titulo: '¿Qué son las Zonas?',
          texto: 'Las zonas de entrenamiento son rangos de intensidad basados en tu frecuencia cardíaca máxima (FCM). Entrenar en la zona adecuada es la diferencia entre simplemente "moverse" y realmente mejorar.'
        },
        {
          titulo: 'Beneficios de cada Zona',
          texto: 'No todo el entrenamiento debe ser "a tope". Cada zona tiene una función fisiológica específica:',
          items: [
            { label: 'Z1', titulo: 'Recuperación Activa', color: '#6b7280', descripcion: 'Ideal para calentar, enfriar o recuperarse de días intensos. Mejora la salud básica.' },
            { label: 'Z2', titulo: 'Base Aeróbica', color: '#30d31a', descripcion: 'La zona más importante para quemar grasas y construir resistencia de larga duración.' },
            { label: 'Z3', titulo: 'Aeróbico Intensivo', color: '#ffd900', descripcion: 'Mejora la capacidad cardiovascular general y la eficiencia del ritmo de crucero.' },
            { label: 'Z4', titulo: 'Umbral Anaeróbico', color: '#ff7e56', descripcion: 'Aquí es donde se gana velocidad. Entrenamiento duro que mejora la tolerancia al lactato.' },
            { label: 'Z5', titulo: 'Potencia Máxima', color: '#ff0000', descripcion: 'Esfuerzo máximo de corta duración. Solo para intervalos de alta intensidad (HIIT).' }
          ]
        },
        {
          titulo: '¿Por qué es importante?',
          texto: 'Si entrenas siempre "fuerte" pero sin control (Z3-Z4 constante), te estancarás pronto. Alternar días de Z2 (base) con días específicos de Z4 o Z5 permite que tu cuerpo se adapte y mejore sin caer en el sobreentrenamiento.'
        }
      ]
    },
    {
      id: 'ritmo',
      titulo: 'Ritmo: min/km vs km/h',
      descripcion: 'Descubre por qué los corredores usan min/km en lugar de km/h y cómo interpretar tu ritmo.',
      imagen: 'assets/images/explorar/pace_hero.png',
      subtitulo: 'La métrica clave para corredores',
      contenido: [
        {
          titulo: '¿Qué es el Ritmo (Pace)?',
          texto: 'El ritmo es el tiempo que tardas en recorrer un kilómetro, expresado en minutos y segundos (min/km). Por ejemplo, un ritmo de 5:30 min/km significa que tardas 5 minutos y 30 segundos en correr cada kilómetro.'
        },
        {
          titulo: '¿Por qué min/km y no km/h?',
          texto: 'Aunque el ciclismo usa km/h (velocidad), el running prefiere min/km (ritmo) por varias razones prácticas:',
          items: [
            { label: '🎯', titulo: 'Planificación de Carrera', color: '#f3f3f3ff', descripcion: 'Es más fácil calcular tu tiempo final. Si corres 10km a 5:00 min/km, sabes que tardarás 50 minutos.' },
            { label: '📊', titulo: 'Control de Intensidad', color: '#f3f3f3ff', descripcion: 'Los cambios de ritmo son más perceptibles. Pasar de 5:00 a 4:50 min/km es un cambio notable, mientras que de 12 a 12.2 km/h parece menor.' },
            { label: '🏃', titulo: 'Estándar Universal', color: '#f3f3f3ff', descripcion: 'Todas las carreras populares (5K, 10K, media maratón, maratón) se planifican en min/km, no en km/h.' }
          ]
        },
        {
          titulo: 'Conversión Rápida',
          texto: 'Si necesitas convertir entre ambas métricas, aquí tienes algunos ejemplos comunes:',
          items: [
            { label: '4:00', titulo: 'min/km = 15 km/h', color: '#fc4c02', descripcion: 'Ritmo muy rápido, típico de corredores avanzados en 10K.' },
            { label: '5:00', titulo: 'min/km = 12 km/h', color: '#fc4c02', descripcion: 'Ritmo sólido para carreras de media distancia.' },
            { label: '6:00', titulo: 'min/km = 10 km/h', color: '#fc4c02', descripcion: 'Ritmo cómodo para entrenamientos de base aeróbica (Z2).' },
            { label: '7:00', titulo: 'min/km = 8.6 km/h', color: '#fc4c02', descripcion: 'Ritmo suave, ideal para rodajes de recuperación.' }
          ]
        },
        {
          titulo: '¿Cómo usar el Ritmo en tus Entrenamientos?',
          texto: 'Cada zona de entrenamiento tiene un rango de ritmo óptimo. Por ejemplo, si tu Z2 (base aeróbica) está entre 140-150 ppm, tu ritmo debería estar entre 6:00-6:30 min/km. Mantener el ritmo correcto te ayuda a entrenar en la zona adecuada sin depender constantemente del pulsómetro.'
        }
      ]
    },
    {
      id: 'frecuencia-cardiaca',
      titulo: 'Frecuencia Cardíaca: Tu Mejor Guía',
      descripcion: 'Aprende a interpretar tus pulsaciones y cómo usarlas para entrenar de forma inteligente.',
      imagen: 'assets/images/explorar/heart_rate_hero.png',
      subtitulo: 'El indicador más fiable de intensidad',
      contenido: [
        {
          titulo: '¿Por qué entrenar con pulsómetro?',
          texto: 'La frecuencia cardíaca (FC) es el indicador más objetivo de la intensidad del ejercicio. A diferencia del ritmo o la sensación subjetiva, las pulsaciones reflejan exactamente el esfuerzo real de tu cuerpo en ese momento.'
        },
        {
          titulo: 'FC Máxima y FC en Reposo',
          texto: 'Estos dos valores son fundamentales para calcular tus zonas de entrenamiento:',
          items: [
            { label: '💓', titulo: 'FC Máxima (FCM)', color: '#fc4c02', descripcion: 'El máximo de pulsaciones que tu corazón puede alcanzar. Se puede estimar con 220 - edad, pero es más preciso medirla en una prueba de esfuerzo.' },
            { label: '🛌', titulo: 'FC en Reposo (FCR)', color: '#30d31a', descripcion: 'Tus pulsaciones al despertar, antes de levantarte. Un valor bajo (50-60 ppm) indica buena forma física.' }
          ]
        },
        {
          titulo: 'Variabilidad de la Frecuencia Cardíaca (HRV)',
          texto: 'La HRV mide la variación entre latidos consecutivos. Una HRV alta indica buena recuperación y capacidad de adaptación al estrés. Si tu HRV baja significativamente, puede ser señal de sobreentrenamiento o fatiga acumulada.'
        },
        {
          titulo: 'Consejos Prácticos',
          texto: 'No te obsesiones con alcanzar exactamente la zona objetivo en cada segundo. Las pulsaciones tardan en estabilizarse (especialmente al inicio). Usa las zonas como guía, no como límites estrictos. En días de calor o cansancio, es normal que las pulsaciones sean más altas para el mismo ritmo.'
        }
      ]
    },
    {
      id: 'sobreentrenamiento',
      titulo: 'Evitar el Sobreentrenamiento',
      descripcion: 'Identifica las señales de alarma y aprende a descansar para seguir progresando.',
      imagen: 'assets/images/explorar/overtraining_hero.png',
      subtitulo: 'Más no siempre es mejor',
      contenido: [
        {
          titulo: '¿Qué es el Sobreentrenamiento?',
          texto: 'El sobreentrenamiento ocurre cuando el volumen o intensidad del ejercicio supera la capacidad de recuperación del cuerpo. En lugar de mejorar, el rendimiento empeora y aumenta el riesgo de lesiones.'
        },
        {
          titulo: 'Señales de Alarma',
          texto: 'Presta atención a estos síntomas que indican que necesitas reducir la carga:',
          items: [
            { label: '😴', titulo: 'Fatiga Persistente', color: '#ff0000', descripcion: 'Cansancio que no desaparece con el descanso nocturno. Sensación de "piernas pesadas" constante.' },
            { label: '💔', titulo: 'FC Elevada en Reposo', color: '#ff7e56', descripcion: 'Si tu FC en reposo sube 5-10 ppm respecto a tu media habitual, es señal de estrés fisiológico.' },
            { label: '😞', titulo: 'Pérdida de Motivación', color: '#ffd900', descripcion: 'Falta de ganas de entrenar, irritabilidad, problemas de sueño o cambios de humor.' },
            { label: '📉', titulo: 'Rendimiento Estancado', color: '#30d31a', descripcion: 'Ritmos que antes eran cómodos ahora requieren más esfuerzo. Incapacidad de completar entrenamientos habituales.' }
          ]
        },
        {
          titulo: 'Cómo Prevenirlo',
          texto: 'La clave está en el equilibrio entre carga y recuperación:',
          items: [
            { label: '📅', titulo: 'Planifica Descansos', color: '#f3f3f3ff', descripcion: 'Incluye al menos 1-2 días de descanso completo o actividad muy suave cada semana.' },
            { label: '💤', titulo: 'Prioriza el Sueño', color: '#f3f3f3ff', descripcion: '7-9 horas de sueño de calidad son esenciales para la recuperación muscular y hormonal.' },
            { label: '🍎', titulo: 'Nutrición Adecuada', color: '#f3f3f3ff', descripcion: 'No entrenes en déficit calórico extremo. Tu cuerpo necesita energía para adaptarse al entrenamiento.' }
          ]
        },
        {
          titulo: '¿Qué hacer si ya estás sobreentrenado?',
          texto: 'Reduce el volumen e intensidad drásticamente durante 1-2 semanas. Prioriza el sueño, la hidratación y la alimentación. Si los síntomas persisten más de 2 semanas, consulta con un médico deportivo.'
        }
      ]
    },
    {
      id: 'periodizacion',
      titulo: 'Periodización del Entrenamiento',
      descripcion: 'Organiza tus entrenamientos en ciclos para maximizar el rendimiento y evitar estancamientos.',
      imagen: 'assets/images/explorar/periodization_hero.png',
      subtitulo: 'Planifica para mejorar continuamente',
      contenido: [
        {
          titulo: '¿Qué es la Periodización?',
          texto: 'La periodización es la planificación estratégica del entrenamiento en ciclos (semanas, meses) con diferentes objetivos. En lugar de entrenar siempre igual, alternas fases de construcción de base, intensidad y recuperación.'
        },
        {
          titulo: 'Fases de un Ciclo Básico',
          texto: 'Un ciclo típico de 12 semanas para preparar una carrera podría estructurarse así:',
          items: [
            { label: '1-4', titulo: 'Base Aeróbica', color: '#30d31a', descripcion: 'Volumen alto, intensidad baja (80% en Z2). Construyes resistencia de larga duración.' },
            { label: '5-8', titulo: 'Construcción', color: '#ffd900', descripcion: 'Introduces entrenamientos de Z3-Z4 (tempo runs, intervalos largos). Volumen moderado.' },
            { label: '9-11', titulo: 'Intensidad', color: '#ff7e56', descripcion: 'Entrenamientos específicos de Z4-Z5 (intervalos cortos, series). Volumen reducido, calidad alta.' },
            { label: '12', titulo: 'Descarga (Taper)', color: '#328bdf', descripcion: 'Reducción drástica del volumen (50-70%) manteniendo algo de intensidad. Llegas fresco a la carrera.' }
          ]
        },
        {
          titulo: 'Principio de Sobrecarga Progresiva',
          texto: 'Para mejorar, debes aumentar gradualmente la carga (volumen o intensidad), pero no ambas a la vez. La regla del 10% sugiere no aumentar el kilometraje semanal más de un 10% respecto a la semana anterior.'
        },
        {
          titulo: 'Semanas de Recuperación',
          texto: 'Cada 3-4 semanas de carga creciente, incluye una semana de recuperación con 30-40% menos volumen. Esto permite que el cuerpo asimile las adaptaciones y evita el sobreentrenamiento. Es en estas semanas cuando realmente mejoras, no durante las semanas duras.'
        }
      ]
    }
  ];

  constructor(private dialog: MatDialog) { }

  openDetail(card: any) {
    this.dialog.open(ExplorarDetalleComponent, {
      data: card,
      maxWidth: '800px',
      width: '90%',
      panelClass: 'premium-dialog'
    });
  }
}
