package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ClinicServiceImplTest {

    @Mock
    private PetRepository petRepository;
    @Mock
    private VetRepository vetRepository;
    @Mock
    private OwnerRepository ownerRepository;
    @Mock
    private VisitRepository visitRepository;

    @InjectMocks
    private ClinicServiceImpl clinicService;

    @Test
    void testNotfoundPetTypes() {
        //given:
        //when:
        Collection<PetType> petTypes = this.clinicService.findPetTypes();
        //then:
        assertThat(petTypes).isEmpty();
    }

    @Test
    void testFoundPetTypes() {
        //given:
        given(this.petRepository.findPetTypes()).willReturn(List.of(new PetType(), new PetType()));
        //when:
        Collection<PetType> petTypes = this.clinicService.findPetTypes();
        //then:
        assertThat(petTypes).hasSize(2);
    }
}