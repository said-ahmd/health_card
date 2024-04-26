package com.sameh.medicory.model.users.hospital;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospitalResponseDTO {
    private long hospitalId;
    private String hospitalName;

}
