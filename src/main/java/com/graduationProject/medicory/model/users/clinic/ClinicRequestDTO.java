package com.graduationProject.medicory.model.users.clinic;

import com.graduationProject.medicory.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClinicRequestDTO {
    private String name;
    private String googleMapsLink;
    private String address;
    private String ownerName;
    private String specialization;
    private String email;
    private Role role;
    private boolean isEnabled;
    private List<String> userPhoneNumbers;
}