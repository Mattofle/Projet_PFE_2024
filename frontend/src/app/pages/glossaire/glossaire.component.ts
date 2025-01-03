import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { glossaireService } from '../../services/glossaire.service'; 
import { CommonModule } from '@angular/common';
import { MatChipsModule } from '@angular/material/chips';
import { MatTableModule } from '@angular/material/table'; 
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-glossaire',
  templateUrl: './glossaire.component.html',
  styleUrls: ['./glossaire.component.css'],
  standalone: true, // Si vous utilisez Angular standalone component
  imports: [MatChipsModule, MatTableModule, CommonModule, FormsModule] // Ajouter FormsModule ici
})
export class GlossaireComponent implements OnInit {
  @Input() search: string = '';

  glossaires: {glossaireId: number, word: string, definition: string, comment: string, info: string}[] = [];
  filteredGlossaires: {glossaireId: number, word: string, definition: string, comment: string, info: string}[] = [];

  constructor(private api: glossaireService) {}

  ngOnInit(): void {
    this.loadGlossaire();
  }

  redirectTo(url: string): void {
    window.open(url, '_blank');
  }

  async loadGlossaire(): Promise<void> {
    try {
      this.glossaires = await this.api.getGlossaire();
      this.filteredGlossaires = this.glossaires; // Initialisez filteredGlossaires ici
    } catch (error) {
      console.error('Error loading glossaire:', error);
    }
  }

  onSearchChange(): void {
    this.filteredGlossaires = this.searchGlossaire(this.search);
  }

  searchGlossaire(search: string): {glossaireId: number, word: string, definition: string, comment: string, info: string}[] {
    if (!search) {
      return this.glossaires;
    }
    search = search.toLowerCase();
    return this.glossaires.filter(glossaire => 
      glossaire.word.toLowerCase().includes(search) || 
      glossaire.definition.toLowerCase().includes(search) ||
      glossaire.comment?.toLowerCase().includes(search) || 
      glossaire.info?.toLowerCase().includes(search)
    );
  }
}
