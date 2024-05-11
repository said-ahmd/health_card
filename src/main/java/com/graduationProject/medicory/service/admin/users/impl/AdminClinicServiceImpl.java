package com.graduationProject.medicory.service.admin.users.impl;

import com.graduationProject.medicory.entity.phoneEntities.UserPhoneNumber;
import com.graduationProject.medicory.entity.usersEntities.Clinic;
import com.graduationProject.medicory.entity.usersEntities.User;
import com.graduationProject.medicory.exception.ConflictException;
import com.graduationProject.medicory.exception.RecordNotFoundException;
import com.graduationProject.medicory.exception.UserDisabledException;
import com.graduationProject.medicory.mapper.usersMappers.ClinicMapper;
import com.graduationProject.medicory.mapper.usersMappers.UserMapper;
import com.graduationProject.medicory.model.users.clinic.ClinicDTO;
import com.graduationProject.medicory.model.users.clinic.ClinicRequestDTO;
import com.graduationProject.medicory.model.users.clinic.ClinicResponseDTO;
import com.graduationProject.medicory.repository.usersRepositories.ClinicRepository;
import com.graduationProject.medicory.repository.phoneRepositories.UserPhoneNumberRepository;
import com.graduationProject.medicory.repository.usersRepositories.UserRepository;
import com.graduationProject.medicory.service.admin.users.AdminClinicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor

//TODO enhance this
public class AdminClinicServiceImpl implements AdminClinicService {

    private final ClinicRepository clinicRepository;
    private final UserRepository userRepository;
    private final ClinicMapper map;
    private final UserMapper userMapper;
    private final UserPhoneNumberRepository userPhoneRepo;


    @Override
    public ClinicResponseDTO findClinicByUserCode(String userCode) {
        Clinic clinic = clinicRepository.findByUserCode(userCode)
                .orElseThrow(() -> new RecordNotFoundException("No user *CLINIC* has code : " + userCode));
        return map.toResponseDTO(clinic);
    }

    @Override
    public List<ClinicResponseDTO> findClinicsByName(String name) {

        List<Clinic> clinics = clinicRepository.findByName(name);
        if (!clinics.isEmpty()) {
            return clinics.stream()
                    .map(map::toResponseDTO)
                    .collect(Collectors.toList());
        } else {
            throw new RecordNotFoundException("No clinics with this name : " + name);
        }
    }

    @Override
    public ClinicResponseDTO findClinicByUserEmail(String userEmail) {
        Clinic clinic = clinicRepository
                .findByUserEmail(userEmail)
                .orElseThrow(() -> new RecordNotFoundException("No user *CLINIC* with email " + userEmail));
        return map.toResponseDTO(clinic);
    }

    @Override
    public ClinicDTO showAllDataOfClinicByClinicId(long clinicId) {
        if (clinicId > 0) {
            Clinic clinic = clinicRepository.findById(clinicId)
                    .orElseThrow(() -> new RecordNotFoundException("No clinic with id " + clinicId));
            return map.toDto(clinic);
        }
        throw new IllegalArgumentException("Invalid id " + clinicId);
    }


    @Override
    public String addNewClinic(ClinicRequestDTO clinicDTO) {
        Clinic newClinic = map.toRequestEntity(clinicDTO);
        User newUser = newClinic.getUser();
        Optional<User> exsistingUser = userRepository.findByEmail(newUser.getEmail());
        if (!exsistingUser.isPresent()) {

            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setUpdatedAt(LocalDateTime.now());
            // user phone numbers
            List<UserPhoneNumber> userPhoneNumbers = newUser.getUserPhoneNumbers()
                    .stream()
                    .map(userPhoneNumber -> {
                        Optional<UserPhoneNumber> user = userPhoneRepo.findUserByPhone(userPhoneNumber.getPhone());
                        if (user.isPresent())
                            throw new ConflictException("This phone number " + userPhoneNumber.getPhone() + " already exist");

                        userPhoneNumber.setUser(newUser);
                        return userPhoneNumber;
                    })
                    .collect(Collectors.toList());
            userRepository.save(newUser);
            clinicRepository.save(newClinic);
            userPhoneRepo.saveAll(userPhoneNumbers);
            return "Clinic added successfully";
        }
        throw new ConflictException("The user email " + newUser.getEmail() + " already exist");

    }

    @Override
    public String updateClinic(ClinicDTO updatedClinic, Long clinicId) {
        if (clinicId > 0) {
            Clinic clinic = clinicRepository.findById(clinicId)
                    .orElseThrow(() -> new RecordNotFoundException("No Clinic with id " + clinicId));

            clinic.setName(updatedClinic.getName());
            clinic.setGoogleMapsLink(updatedClinic.getGoogleMapsLink());
            clinic.setOwnerName(updatedClinic.getOwnerName());
            clinic.setSpecialization(updatedClinic.getSpecialization());

            User oldUser = clinic.getUser();
            oldUser.setEmail(updatedClinic.getEmail());
            oldUser.setPassword(updatedClinic.getPassword());
            oldUser.setRole(updatedClinic.getRole());
            oldUser.setEnabled(updatedClinic.isEnabled());
            oldUser.setUpdatedAt(LocalDateTime.now());
            // Update or add user phone numbers
            List<String> updatedPhoneNumbers = updatedClinic.getUserPhoneNumbers();
            List<UserPhoneNumber> oldUserPhoneNumbers = oldUser.getUserPhoneNumbers();

            for (int i = 0; i < updatedPhoneNumbers.size(); i++) {
                String updatedPhoneNumber = updatedPhoneNumbers.get(i);
                UserPhoneNumber userPhoneNumber = oldUserPhoneNumbers.get(i);

                if (!userPhoneNumber.getPhone().equals(updatedPhoneNumber)) {
                    Optional<UserPhoneNumber> existingUser = userPhoneRepo.findUserByPhone(updatedPhoneNumber);
                    if (existingUser.isPresent()) {
                        throw new ConflictException("This phone number " + updatedPhoneNumber + " already exists");
                    }
                    userPhoneNumber.setPhone(updatedPhoneNumber);
                }
            }

            userRepository.save(oldUser);
            clinicRepository.save(clinic);
            userPhoneRepo.saveAll(oldUserPhoneNumbers);
            return "Clinic updted sucessfully";
        }

        throw new IllegalArgumentException("Invalid id " + clinicId);
    }

    @Override
    public String deleteClinicById(Long clinicId) {
        if (clinicId > 0) {
            Clinic clinic = clinicRepository.findById(clinicId)
                    .orElseThrow(() -> new RecordNotFoundException("No Clinic with id " + clinicId));

            User unEnabledUser = clinic.getUser();
            if (unEnabledUser.isEnabled()) {
                unEnabledUser.setEnabled(false);
                unEnabledUser.setUpdatedAt(LocalDateTime.now());
                userRepository.save(unEnabledUser);
                return "Clinic deleted successfully";
            }
            throw new UserDisabledException("This user is unEnabled already");
        }
        throw new IllegalArgumentException("Invalid id");


    }
}
