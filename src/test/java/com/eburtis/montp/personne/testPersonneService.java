package com.eburtis.montp.personne;

import com.eburtis.montp.Application.DepartementVo;
import com.eburtis.montp.Application.PersonneVo;
import com.eburtis.montp.Domain.Departement;
import com.eburtis.montp.Domain.Personne;
import com.eburtis.montp.Repository.DepartementRepository;
import com.eburtis.montp.Repository.PersonneRepository;
import com.eburtis.montp.Service.PersonneService;
import com.eburtis.montp.utils.DepartementMB;
import com.eburtis.montp.utils.DepartementVoMB;
import com.eburtis.montp.utils.PersonneMB;
import com.eburtis.montp.utils.PersonneVoMB;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.data.util.Lazy.empty;

@ExtendWith(MockitoExtension.class)
class TestPersonneService {
    @Mock
    private PersonneRepository personneRepository;
    @Mock
    private DepartementRepository departementRepository;
    @InjectMocks
    private PersonneService personneService;

    Long personneId = 1L;
    Long departementId = 2L;
    Departement departement = new DepartementMB()
            .setId(departementId)
            .setCode("DAB")
            .setDesignation("Dabou")
            .bluid();
    DepartementVo departementVo = new DepartementVoMB()
            .setId(departementId)
            .setCode("DAB")
            .setDesignation("Dabou")
            .bluid();
    Personne seydou = new PersonneMB()
            .setId(personneId)
            .setNom("Diarrassouba")
            .setPrenoms("Seydou")
            .setAge(22)
            .setDepartement(departement)
            .build();
    PersonneVo seydouVo = new PersonneVoMB()
            .setId(personneId)
            .setNom("Diarrassouba")
            .setPrenoms("Seydou")
            .setAge(22)
            .setDepartement(departementVo)
            .build();
    Personne bamba = new PersonneMB()
            .setId(2L)
            .setNom("Bamba")
            .setPrenoms("Salimata")
            .setAge(24)
            .setDepartement(departement)
            .build();

    @Test
    @DisplayName("test unitaire de la methode listePersonnes du service personne")
    void testListePersonnes(){
        // GIVEN
        // Création d'une liste de personnes
        List<Personne> personnes = new ArrayList<>();
        personnes.add(seydou);
        personnes.add(bamba);

        // Configuration de la méthode "findAll" du repository pour qu'elle renvoie la liste de personnes créée ci-dessus
        when(personneRepository.findAll()).thenReturn(personnes);

        // WHEN
        // Appel de la méthode "listePersonnes" du service
        List<PersonneVo> personnesVo = personneService.listePersonnes();

        // THEN
        // Vérification que la méthode "findAll" du repository a bien été appelée
        verify(personneRepository).findAll();
        // Vérification que la taille de la liste de personnes retournée par le service est égale à la taille de la liste de personnes créée ci-dessus
        assertEquals(personnes.size(), personnesVo.size());
        // Vérification que la liste de personnes retournée par le service n'est pas nulle
        assertNotNull(personnesVo);
    }

    @Test
    @DisplayName("test unitaire de la methode obtenirPersonne de personneService")
    void testObtenirPersonne(){
        // GIVEN
        // Configuration de la méthode "findById" du repository pour qu'elle renvoie la personne "yedagne" lorsque l'identifiant "personneId" est passé en paramètre
        when(personneRepository.findById(personneId)).thenReturn(Optional.of(seydou));

        // WHEN
        // Appel de la méthode "obtenirPersonne" du service en passant l'identifiant de la personne "yedagne" en paramètre
        Optional<PersonneVo> personneVo = personneService.obtenirPersonne(personneId);

        // THEN
        // Vérification que la méthode "findById" du repository a bien été appelée avec l'identifiant "personneId"
        verify(personneRepository).findById(personneId);
        // Vérification que la personne retournée par la méthode "obtenirPersonne" du service n'est pas nulle
        assertNotNull(personneVo);
        // Vérification que la personne retournée par la méthode "obtenirPersonne" du service est bien présente (c'est-à-dire qu'elle existe)
        assertTrue(personneVo.isPresent());
    }

    @Test
    @DisplayName("test unitaire de la methode de creation d'une personne de la couche personneService")
    void testCreerPersonne(){
        // GIVEN
        // Configuration de la méthode "findById" du repository pour qu'elle renvoie le département "departement" lorsque l'identifiant "departementId" est passé en paramètre
        when(departementRepository.findById(departementId)).thenReturn(Optional.of(departement));
        // Configuration de la méthode "save" du repository pour qu'elle renvoie la personne "yedagne" lorsqu'elle est appelée avec n'importe quelle instance de la classe "Personne" en paramètre
        when(personneRepository.save(Mockito.any(Personne.class))).thenReturn(seydou);

        // WHEN
        // Appel de la méthode "creerPersonne" du service en passant l'objet "yedagneVo" en paramètre
        PersonneVo savePersonne = personneService.creerPersonne(seydouVo);

        // THEN
        // Vérification que la personne retournée par la méthode "creerPersonne" du service n'est pas nulle
        assertNotNull(savePersonne);
        // Vérification que le nom de la personne retournée correspond bien au nom de la personne passée en paramètre
        assertEquals(savePersonne.getNom(), seydouVo.getNom());
    }

    @Test
    @DisplayName("test unitaire de la methode modifierPersonne de la couche service Personne")
    void testModifierPersonne(){
        // GIVEN
        // Configuration de la méthode "findById" du repository pour qu'elle renvoie la personne "seydou" lorsque l'identifiant "personneId" est passé en paramètre
        when(personneRepository.findById(personneId)).thenReturn(Optional.of(seydou));
        // Configuration de la méthode "findById" du repository pour qu'elle renvoie le département "departement" lorsque l'identifiant "departementId" est passé en paramètre
        when(departementRepository.findById(departementId)).thenReturn(Optional.of(departement));
        // Configuration de la méthode "save" du repository pour qu'elle renvoie la personne "seydou" lorsqu'elle est appelée avec n'importe quelle instance de la classe "Personne" en paramètre
        when(personneRepository.save(Mockito.any(Personne.class))).thenReturn(seydou);

        // WHEN
        // Appel de la méthode "modifierPersonne" du service en passant l'identifiant "personneId" et l'objet "seydouVo" en paramètres
        PersonneVo updatePersonne = personneService.modifierPersonne(personneId,seydouVo);

        // THEN
        // Vérification que la personne retournée par la méthode "modifierPersonne" du service n'est pas nulle
        assertNotNull(updatePersonne);


    }
    @Test
    @DisplayName("test unitaire de la methode de suppression d'une personne de la couche personneservice")
    void testSupprimerPersonne(){
        // GIVEN
        // Configuration de la méthode "deleteById" du repository pour qu'elle ne fasse rien lorsqu'elle est appelée avec l'identifiant "personneId" en paramètre
        doNothing().when(personneRepository).deleteById(personneId);

        // WHEN
        // Appel de la méthode "supprimerPersonne" du service en passant l'identifiant "personneId" en paramètre
        personneService.supprimerPersonne(personneId);

        // THEN
        // Vérification que la méthode "deleteById" du repository a été appelée avec l'identifiant "personneId" en paramètre
        verify(personneRepository).deleteById(personneId);
    }

}
