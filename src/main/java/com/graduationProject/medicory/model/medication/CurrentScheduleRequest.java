package com.graduationProject.medicory.model.medication;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CurrentScheduleRequest {
//    private MedicationDTO updatesInMedication;
    private Long id;
    private String medicineName;
    private String dose;
    private int frequency;
    private String sideEffects;
    private String tips;
}
