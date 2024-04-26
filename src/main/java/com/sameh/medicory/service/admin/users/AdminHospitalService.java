package com.sameh.medicory.service.admin.users;

import com.sameh.medicory.model.users.hospital.HospitalDTO;
import com.sameh.medicory.model.users.hospital.HospitalResponseDTO;

import java.util.List;

public interface AdminHospitalService {

    HospitalResponseDTO findHospitalByUserCode(String userCode);
    HospitalResponseDTO findHospitalByEmail(String email);
    List<HospitalResponseDTO> findHospitalByName(String name);
    HospitalDTO showAllDataOfHospitalById(long hospitalId);
    String addHospital(HospitalDTO newHospital);
    String updateHospital(Long hospitalId,HospitalDTO updatedHospital);
    String deleteHospital(long hospitalId);
}
