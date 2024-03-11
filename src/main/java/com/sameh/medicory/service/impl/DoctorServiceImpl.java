package com.sameh.medicory.service.impl;

import com.sameh.medicory.entity.otherEntities.Allergies;
import com.sameh.medicory.entity.otherEntities.ChronicDiseases;
import com.sameh.medicory.entity.phoneEntities.OwnerPhoneNumber;
import com.sameh.medicory.entity.phoneEntities.RelativePhoneNumber;
import com.sameh.medicory.entity.usersEntities.Owner;
import com.sameh.medicory.entity.usersEntities.User;
import com.sameh.medicory.mapper.AllergiesMapper;
import com.sameh.medicory.mapper.ChronicDiseasesMapper;
import com.sameh.medicory.model.AllergiesDTO;
import com.sameh.medicory.model.chronicDisease.ChronicDiseasesDTO;
import com.sameh.medicory.model.patient.PatientPersonalInformation;
import com.sameh.medicory.repository.OwnerRepository;
import com.sameh.medicory.service.DoctorService;
import com.sameh.medicory.utils.OwnerContext;
import com.sameh.medicory.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DoctorServiceImpl implements DoctorService {

    private final OwnerRepository ownerRepository;
    private final ChronicDiseasesMapper chronicDiseasesMapper;
    private final OwnerContext ownerContext;
    private final AllergiesMapper allergiesMapper;

    @Override
    public PatientPersonalInformation getPatientPersonalInformation() {
        User patientUser = ownerContext.getCurrentUser();
        Owner patientOwner = ownerContext.getCurrentOwner();
        log.info("Doctor Wants to get patient personal information for owner with id {}", patientOwner.getId());
        PatientPersonalInformation patientPersonalInformation = new PatientPersonalInformation(
                patientOwner.getFirstName() + " " + patientOwner.getMiddleName() + " " + patientOwner.getLastName(),
                getCurrentAge(patientOwner.getDateOfBirth()),
                patientOwner.getGender().name(),
                getPatientPhoneNumbers(patientUser),
                getPatientRelativePhoneNumbers(patientUser),
                patientOwner.getAddress(),
                patientUser.getEmail(),
                patientOwner.getBloodType().name()
        );
        log.info("patient personal information for owner with id: {} is {}", patientOwner.getId(), patientPersonalInformation);
        return patientPersonalInformation;
    }

    @Override
    public List<ChronicDiseasesDTO> getPatientChronicDiseases() {
        Owner patientOwner = ownerContext.getCurrentOwner();
        log.info("Doctor Wants to get patient Chronic Diseases for owner with id {}", patientOwner.getId());
        List<ChronicDiseases> chronicDiseases = patientOwner.getChronicDiseases();
        List<ChronicDiseasesDTO> chronicDiseasesDTOS = chronicDiseases
                .stream()
                .map(chronicDiseasesMapper::toDTO)
                .collect(Collectors.toList());
        log.info("patient Chronic Diseases for owner with id: {} is {}", patientOwner.getId(), chronicDiseases);
        return chronicDiseasesDTOS;
    }

    @Override
    public String addNewChronicDiseasesForPatient(ChronicDiseasesDTO chronicDiseasesDTO) {
        Owner patientOwner = ownerContext.getCurrentOwner();
        log.info("Doctor wants to add a new chronic disease: {} for patient with owner id: {}", chronicDiseasesDTO, patientOwner.getId());

        ChronicDiseases newChronicDisease = chronicDiseasesMapper.toEntity(chronicDiseasesDTO);
        List<ChronicDiseases> patientChronicDiseases = patientOwner.getChronicDiseases();
        patientChronicDiseases.add(newChronicDisease);
        log.info("patientChronicDiseases {}", patientChronicDiseases);
        patientOwner.setChronicDiseases(patientChronicDiseases);
        ownerRepository.save(patientOwner);

        log.info("The new chronic disease added successfully. All the patient chronic diseases: {}", patientOwner.getChronicDiseases());

        return "Chronic Disease added successfully";
    }

    @Override
    public List<AllergiesDTO> getPatientAllergies() {
        Owner patientOwner = ownerContext.getCurrentOwner();
        log.info("Doctor want to get patient Allergies owner with id: {}", patientOwner.getAllergies());
        List<AllergiesDTO> allergiesDTOS = patientOwner.getAllergies()
                .stream()
                .map(allergiesMapper::toDTO)
                .collect(Collectors.toList());
        log.info("All Patient Allergies {}", allergiesDTOS);
        return allergiesDTOS;
    }

    @Override
    public String addNewAllergiesForPatient(AllergiesDTO allergiesDTO) {
        Owner patientOwner = ownerContext.getCurrentOwner();
        log.info("Doctor want to add new Allergies: {} for patient owner his ID: {}", allergiesDTO,patientOwner.getId());
        Allergies allergies = allergiesMapper.toEntity(allergiesDTO);
        List<Allergies> patientAllergies = patientOwner.getAllergies();
        patientAllergies.add(allergies);
        patientOwner.setAllergies(patientAllergies);
        ownerRepository.save(patientOwner);
        log.info("the new Allergies Added successfully {}", patientOwner.getAllergies());
        return "New Allergies Added successfully";
    }


    private Integer getCurrentAge(LocalDate dateOfBirth) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(dateOfBirth, currentDate);
        return period.getYears();
    }

    private List<String> getPatientPhoneNumbers(User patientUser){
        List<OwnerPhoneNumber> phoneNumbers =patientUser.getOwnerPhoneNumbers();
        List<String> patientPhoneNumbers = phoneNumbers.stream()
                .map(phoneNumber -> phoneNumber.getPhone())
                .collect(Collectors.toList());
        return patientPhoneNumbers;
    }

    private List<String> getPatientRelativePhoneNumbers(User patientUser){
        List<RelativePhoneNumber> relativePhoneNumbers = patientUser.getRelativePhoneNumbers();
        List<String> patientRelativePhoneNumbers = relativePhoneNumbers.stream()
                .map(phoneNumber -> phoneNumber.getPhone())
                .collect(Collectors.toList());
        return patientRelativePhoneNumbers;
    }

}