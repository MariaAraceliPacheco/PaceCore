import { Component, ElementRef, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { WorkoutService } from '../../../services/workout.service';
import { TipoActividad } from '../../../../models/tipo-actividad.model';
import { Estadisticas } from '../../../../models/estadisticas.model';
import { AuthService } from '../../../services/auth.service';
import { EstadisticasSemanalesDTO } from '../../../../models/estadisticasSemanalesDTO.model';
import { CommonModule } from '@angular/common';
import { FormatSecondsPipe } from '../../../pipes/format-seconds.pipe';
import { UsuarioService } from '../../../services/usuario.service';
import { Chart, registerables } from 'chart.js';
import { ZonasService } from '../../../services/zonas.service';
import { PorcentajesZonasDTO } from '../../../../models/PorcentajesZonasDTO.model';
import { ActividadConteoDTO } from '../../../../models/actividadConteoDTO.model';
import { RitmoPipe } from '../../../pipes/ritmo.pipe';

Chart.register(...registerables);

@Component({
  selector: 'app-statistics',
  standalone: true,
  imports: [FormsModule, CommonModule, FormatSecondsPipe, RitmoPipe],
  templateUrl: './statistics.component.html',
  styleUrl: './statistics.component.css'
})
export class StatisticsComponent implements OnInit, AfterViewInit {
  @ViewChild('volumeChart') volumeChartRef!: ElementRef<HTMLCanvasElement>;
  @ViewChild('chartZonas') chartZonasRef!: ElementRef<HTMLCanvasElement>;
  @ViewChild('chartConteoActividades') chartConteoActividadesRef!: ElementRef<HTMLCanvasElement>;
  chart: Chart | undefined;
  chartZonas: Chart | undefined;
  chartConteoActividades: Chart | undefined;

  estadisticasSemanales: EstadisticasSemanalesDTO[] = [];
  tiposActividad: TipoActividad[] = [];
  selectedWeekStart: Date;
  selectedWeekStartZonas: Date;
  selectedMonthStart: Date;
  selectedActivityIdZones: string = 'all';

  recuentoTipoActividadesUsuario: ActividadConteoDTO[] = [];
  recuentoTipoActividadesUsuarioAnterior: ActividadConteoDTO[] = [];

  porcentajeZonas: PorcentajesZonasDTO = {
    zona1: 0,
    zona2: 0,
    zona3: 0,
    zona4: 0,
    zona5: 0
  };

  selectedActivityId: string = 'all';
  activityTypes: TipoActividad[] = [];

  estadisticas: Estadisticas = {
    distanciaTotalKm: 0,
    desnivelTotal: 0,
    caloriasTotales: 0,
    ritmoMedioRunning: 0,
    velocidadMediaBici: 0,
    tiempoTotal: 0,
    marca5k: '',
    marca10k: '',
    marca21k: '',
    marca42k: ''
  };


  personalBests = [
    { distance: '5K', time: this.estadisticas.marca5k },
    { distance: '10K', time: this.estadisticas.marca10k },
    { distance: '21K', time: this.estadisticas.marca21k },
    { distance: '42K', time: this.estadisticas.marca42k },
  ];


  constructor(private workoutService: WorkoutService, private authService: AuthService, private usuarioService: UsuarioService, private zonasService: ZonasService) {
    // Initialize to current week's Monday
    this.selectedWeekStart = this.getWeekStart(new Date());
    this.selectedWeekStartZonas = this.getWeekStart(new Date());
    this.selectedMonthStart = this.getMonthStart(new Date());
  }

  ngOnInit(): void {
    // this.getPorcentajesZonas();
    this.workoutService.actividades$.subscribe({
      next: (actividades) => {
        this.activityTypes = actividades;
      },
      error: (err) => console.error('Error en la suscripción de actividades:', err)
    });

    this.authService.user$.subscribe(user => {
      if (user) {
        this.workoutService.getEstadisticas(user.usuario.id).subscribe({
          next: (estadisticas) => {
            this.estadisticas = estadisticas;
          },
          error: (err) => console.error('Error en la suscripción de estadisticas:', err)
        });
      }
    });

    this.authService.user$.subscribe(user => {
      if (user) {
        this.usuarioService.getEstadisticasSemanales(user.usuario.id).subscribe({
          next: (estadisticasSemanales) => {
            this.estadisticasSemanales = estadisticasSemanales;
            const nombresUnicos = new Set();
            this.tiposActividad = this.estadisticasSemanales
              .map(e => e.tipoActividad)
              .filter(act => {
                if (nombresUnicos.has(act.nombre)) {
                  return false;
                } else {
                  nombresUnicos.add(act.nombre);
                  return true;
                }
              });
            this.updateChart();
          },
          error: (err) => console.error('Error en la suscripción de estadisticas semanales:', err)
        });
      }
    });

    this.loadZonasData();
    this.loadMonthlyConteoData();
  }

  ngAfterViewInit() {
    this.initChart();
    this.updateChart();
    this.updateChartZonas();
  }

  initChart() {
    // Destruir charts existentes si los hay para evitar el error "Canvas is already in use"
    if (this.chart) {
      this.chart.destroy();
    }
    if (this.chartZonas) {
      this.chartZonas.destroy();
    }
    if (this.chartConteoActividades) {
      this.chartConteoActividades.destroy();
    }

    const ctx = this.volumeChartRef.nativeElement.getContext('2d');
    if (ctx) {
      const gradient = ctx.createLinearGradient(0, 0, 0, 400);
      gradient.addColorStop(0, 'rgba(252, 76, 2, 0.2)');
      gradient.addColorStop(1, 'rgba(252, 76, 2, 0)');

      this.chart = new Chart(ctx, {
        type: 'line',
        data: {
          labels: ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'],
          datasets: [{
            label: 'Kilómetros',
            data: [0, 0, 0, 0, 0, 0, 0],
            borderColor: '#fc4c02',
            backgroundColor: gradient,
            fill: true,
            tension: 0,
            pointRadius: 3,
            pointBackgroundColor: 'white',
            pointBorderColor: '#fc4c02',
            pointBorderWidth: 2,
            pointHoverRadius: 6,
            pointHoverBackgroundColor: '#fc4c02',
            pointHoverBorderColor: 'white',
            pointHoverBorderWidth: 2
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: { display: false },
            tooltip: {
              backgroundColor: 'rgba(0, 0, 0, 0.8)',
              padding: 10,
              titleFont: { size: 14, weight: 'bold' },
              bodyFont: { size: 13 },
              displayColors: false,
              callbacks: {
                label: (context) => ` ${context.parsed.y} km`
              }
            }
          },
          scales: {
            x: {
              grid: { display: false },
              ticks: { color: '#666', font: { weight: 600 } }
            },
            y: {
              beginAtZero: true,
              suggestedMax: 10,
              grid: { color: '#f0f0f0' },
              ticks: {
                color: '#999',
                callback: (value) => `${value} km`
              }
            }
          }
        }
      });
    }

    // Configuración del chart de las zonas
    const ctx2 = this.chartZonasRef.nativeElement.getContext('2d');
    if (ctx2) {
      this.chartZonas = new Chart(ctx2, {
        type: 'doughnut',
        data: {
          labels: ['Zona 1', 'Zona 2', 'Zona 3', 'Zona 4', 'Zona 5'],
          datasets: [{
            data: [0, 0, 0, 0, 0],
            backgroundColor: [
              '#328bdfff',
              '#30d31aff',
              '#ffd900ff',
              '#ff7e56ff',
              '#ff0000ff'
            ],
            hoverOffset: 4
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              position: 'right',
              labels: {
                padding: 20,
                font: { size: 12, weight: 600 }
              }
            }
          }
        }
      });
    }

    //configuracion del chart del recuento de tipo de actividad del usuario
    const ctx3 = this.chartConteoActividadesRef.nativeElement.getContext('2d');

    if (ctx3) {
      this.chartConteoActividades = new Chart(ctx3, {
        type: 'radar',
        data: {
          labels: [],
          datasets: [
            {
              label: 'Mes Actual',
              data: [],
              fill: true,
              backgroundColor: 'rgba(252, 76, 2, 0.2)',
              borderColor: '#fc4c02',
              pointBackgroundColor: '#fc4c02',
              pointBorderColor: '#fff',
              pointHoverBackgroundColor: '#fff',
              pointHoverBorderColor: '#fc4c02'
            },
            {
              label: 'Mes Anterior',
              data: [],
              fill: true,
              backgroundColor: 'rgba(55, 65, 81, 0.2)',
              borderColor: '#374151',
              pointBackgroundColor: '#374151',
              pointBorderColor: '#fff',
              pointHoverBackgroundColor: '#fff',
              pointHoverBorderColor: '#374151'
            }
          ]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          scales: {
            r: {
              beginAtZero: true,
              ticks: { stepSize: 1 }
            }
          },
          plugins: {
            legend: {
              position: 'top',
              labels: { font: { weight: 600 } }
            }
          }
        }
      });
    }
  }

  updateChart() {
    if (this.chart) {
      const volume = this.weeklyVolume.map(v => v.km);

      //inyecta los datos en el chart.js
      //volume es un array de 7 elementos con los km de cada dia de la semana
      //a la hora de insertarselo en el chart.js, el orden debe ser el mismo que el de los labels
      this.chart.data.datasets[0].data = volume;
      this.chart.update();
    }
    this.updateChartConteo();
  }

  updateChartConteo() {
    if (!this.chartConteoActividades) return;

    // Combinar etiquetas de ambos meses para asegurar que todos los deportes aparezcan
    const allLabels = Array.from(new Set([
      ...this.recuentoTipoActividadesUsuario.map(e => e.nombreActividad),
      ...this.recuentoTipoActividadesUsuarioAnterior.map(e => e.nombreActividad)
    ]));

    const currentData = allLabels.map(label => {
      const match = this.recuentoTipoActividadesUsuario.find(e => e.nombreActividad === label);
      return match ? match.cantidad : 0;
    });

    const previousData = allLabels.map(label => {
      const match = this.recuentoTipoActividadesUsuarioAnterior.find(e => e.nombreActividad === label);
      return match ? match.cantidad : 0;
    });

    this.chartConteoActividades.data.labels = allLabels;
    this.chartConteoActividades.data.datasets[0].data = currentData;
    this.chartConteoActividades.data.datasets[1].data = previousData;
    this.chartConteoActividades.update();
  }

  updateChartZonas() {
    if (this.chartZonas && this.porcentajeZonas) {
      const data = [
        this.porcentajeZonas.zona1,
        this.porcentajeZonas.zona2,
        this.porcentajeZonas.zona3,
        this.porcentajeZonas.zona4,
        this.porcentajeZonas.zona5
      ];
      this.chartZonas.data.datasets[0].data = data;
      this.chartZonas.update();
    }
  }

  get availableActivityTypes() {
    const typesMap = new Map();
    this.estadisticasSemanales.forEach(w => {
      typesMap.set(w.tipoActividad.id, w.tipoActividad.nombre);
    });
    return Array.from(typesMap.entries()).map(([id, name]) => ({ id, name }));
  }

  get filteredWorkouts() {
    const weekEnd = new Date(this.selectedWeekStart);
    weekEnd.setDate(weekEnd.getDate() + 7);

    let filtered = this.estadisticasSemanales.filter(w => {
      const workoutDate = new Date(w.fecha);
      return workoutDate >= this.selectedWeekStart && workoutDate < weekEnd;
    });

    if (this.selectedActivityId !== 'all') {
      filtered = filtered.filter(w => w.tipoActividad.id.toString() === this.selectedActivityId);
    }

    return filtered;
  }

  setActivityFilterZones(id: string) {
    this.selectedActivityIdZones = id;
    this.loadZonasData();
  }

  get weeklyFcMedia(): number {
    const workoutsWithFC = this.filteredWorkouts.filter(w => w.fcMedia > 0);
    if (workoutsWithFC.length === 0) return 0;

    const sum = workoutsWithFC.reduce((acc, w) => acc + w.fcMedia, 0);
    return Math.round(sum / workoutsWithFC.length);
  }

  get weeklyFcMaxima(): number {
    const max = this.filteredWorkouts.reduce((max, w) => Math.max(max, w.fcMaxima), 0);
    return max;
  }

  get totalWeeklyKm() {
    return this.filteredWorkouts.reduce((sum, w) => sum + w.distancia, 0).toFixed(1);
  }

  get weeklyVolume() {
    const dayNames = ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'];
    const volume = dayNames.map(day => ({ day, km: 0 }));

    this.filteredWorkouts.forEach(workout => {
      const date = new Date(workout.fecha);
      let dayIndex = date.getDay() - 1;
      if (dayIndex === -1) dayIndex = 6;

      if (dayIndex >= 0 && dayIndex < 7) {
        volume[dayIndex].km += workout.distancia;
      }
    });

    return volume;
  }


  setActivityFilter(value: string) {
    this.selectedActivityId = value;
    this.updateChart();
  }

  // Week navigation methods
  getWeekStart(date: Date): Date {
    const d = new Date(date);
    const day = d.getDay();
    const diff = d.getDate() - day + (day === 0 ? -6 : 1); // Adjust to Monday
    d.setDate(diff);
    d.setHours(0, 0, 0, 0);
    return d;
  }

  previousWeek(): void {
    const newDate = new Date(this.selectedWeekStart);
    newDate.setDate(newDate.getDate() - 7);
    this.selectedWeekStart = newDate;
    this.updateChart();
  }

  //CONFIGURACION DEL DIV DE LA FECHA DE LAS ZONAS
  previousWeekZonas(): void {
    const newDate = new Date(this.selectedWeekStartZonas);
    newDate.setDate(newDate.getDate() - 7);
    this.selectedWeekStartZonas = newDate;
    this.loadZonasData();
  }

  nextWeekZonas(): void {
    const newDate = new Date(this.selectedWeekStartZonas);
    newDate.setDate(newDate.getDate() + 7);
    this.selectedWeekStartZonas = newDate;
    this.loadZonasData();
  }

  loadZonasData(): void {
    const activityId = this.selectedActivityIdZones === 'all' ? undefined : parseInt(this.selectedActivityIdZones);
    const inicio = new Date(this.selectedWeekStartZonas);
    // Ampliamos el rango para evitar problemas de zona horaria (UTC vs Local)
    inicio.setDate(inicio.getDate() - 1);

    const fin = new Date(this.selectedWeekStartZonas);
    fin.setDate(fin.getDate() + 8); // 7 días de la semana + 1 de margen

    //se llama al metodo para obtener las zonas filtradas
    this.getPorcentajesZonas(activityId, inicio, fin);
  }

  getWeekStartZonas(date: Date): Date {
    const d = new Date(date);
    const day = d.getDay();
    const diff = d.getDate() - day + (day === 0 ? -6 : 1); // Adjust to Monday
    d.setDate(diff);
    d.setHours(0, 0, 0, 0);
    return d;
  }

  get weekLabelZonas(): string {
    const weekEnd = new Date(this.selectedWeekStartZonas);
    weekEnd.setDate(weekEnd.getDate() + 6);

    const formatDate = (date: Date) => {
      const day = date.getDate();
      const month = date.toLocaleDateString('es-ES', { month: 'short' });
      return `${day} ${month}`;
    };

    return `${formatDate(this.selectedWeekStartZonas)} - ${formatDate(weekEnd)}`;
  }

  isCurrentWeekZonas(): boolean {
    const today = new Date();
    const currentWeekStartZonas = this.getWeekStart(today);
    return this.selectedWeekStartZonas.getTime() === currentWeekStartZonas.getTime();
  }


  nextWeek(): void {
    const newDate = new Date(this.selectedWeekStart);
    newDate.setDate(newDate.getDate() + 7);
    this.selectedWeekStart = newDate;
    this.updateChart();
  }

  get weekLabel(): string {
    const weekEnd = new Date(this.selectedWeekStart);
    weekEnd.setDate(weekEnd.getDate() + 6);

    const formatDate = (date: Date) => {
      const day = date.getDate();
      const month = date.toLocaleDateString('es-ES', { month: 'short' });
      return `${day} ${month}`;
    };

    return `${formatDate(this.selectedWeekStart)} - ${formatDate(weekEnd)}`;
  }



  get isCurrentWeek(): boolean {
    const today = new Date();
    const currentWeekStart = this.getWeekStart(today);
    return this.selectedWeekStart.getTime() === currentWeekStart.getTime();
  }

  getPorcentajesZonas(idTipoActividad?: number, inicio?: Date, fin?: Date) {
    this.authService.user$.subscribe(user => {
      if (user) {
        this.zonasService.getPorcentajesZonas(user.usuario.id, idTipoActividad, inicio, fin).subscribe({
          next: (porcentajesZonas) => {
            this.porcentajeZonas = porcentajesZonas;
            this.updateChartZonas();
          },
          error: (err) => console.error('Error en la suscripción de porcentajes de zonas:', err)
        });
      }
    });
  }

  loadMonthlyConteoData(): void {
    const inicioActual = this.selectedMonthStart;
    const finActual = new Date(inicioActual);
    finActual.setMonth(finActual.getMonth() + 1);

    const inicioAnterior = new Date(inicioActual);
    inicioAnterior.setMonth(inicioAnterior.getMonth() - 1);
    const finAnterior = new Date(inicioActual);

    this.authService.user$.subscribe(user => {
      if (user) {
        // Cargar mes actual
        this.zonasService.getConteoActividades(user.usuario.id, inicioActual, finActual).subscribe(data => {
          this.recuentoTipoActividadesUsuario = data;
          this.updateChartConteo();
        });

        // Cargar mes anterior
        this.zonasService.getConteoActividades(user.usuario.id, inicioAnterior, finAnterior).subscribe(data => {
          this.recuentoTipoActividadesUsuarioAnterior = data;
          this.updateChartConteo();
        });
      }
    });
  }

  previousMonth(): void {
    const newDate = new Date(this.selectedMonthStart);
    newDate.setMonth(newDate.getMonth() - 1);
    this.selectedMonthStart = newDate;
    this.loadMonthlyConteoData();
  }

  nextMonth(): void {
    const newDate = new Date(this.selectedMonthStart);
    newDate.setMonth(newDate.getMonth() + 1);
    this.selectedMonthStart = newDate;
    this.loadMonthlyConteoData();
  }

  get monthLabel(): string {
    return this.selectedMonthStart.toLocaleDateString('es-ES', { month: 'long', year: 'numeric' });
  }

  get isCurrentMonth(): boolean {
    const today = new Date();
    const currentMonthStart = this.getMonthStart(today);
    return this.selectedMonthStart.getTime() === currentMonthStart.getTime();
  }

  getMonthStart(date: Date): Date {
    const d = new Date(date);
    d.setDate(1);
    d.setHours(0, 0, 0, 0);
    return d;
  }

}
