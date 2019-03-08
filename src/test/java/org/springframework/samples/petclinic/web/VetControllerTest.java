package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.ClinicService;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class VetControllerTest {

    @Mock
    private ClinicService clinicService;

    @InjectMocks
    private VetController controller;

    @Test
    void showVetList() {
        //given:
        given(this.clinicService.findVets()).willReturn(List.of(new Vet(), new Vet()));


        //when:
        String templateName = this.controller.showVetList(new HashMap<>());

        //then:
        assertThat(templateName).isEqualTo("vets/vetList");
        then(this.clinicService).should().findVets();
    }

    @Test
    void showResourcesVetList() {
    }
}