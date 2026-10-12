import { Routes } from '@angular/router';
import { CitaListComponent } from './components/cita-list/cita-list.component';
import { CitaFormComponent } from './components/cita-form/cita-form.component';

export const routes: Routes = [
  { path: '', redirectTo: 'citas', pathMatch: 'full' },
  { path: 'citas', component: CitaListComponent },
  { path: 'nueva-cita', component: CitaFormComponent },
  { path: '**', redirectTo: 'citas' },
];
