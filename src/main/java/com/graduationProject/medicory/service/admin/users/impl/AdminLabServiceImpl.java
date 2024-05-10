package com.graduationProject.medicory.service.admin.users.impl;

import com.graduationProject.medicory.entity.phoneEntities.UserPhoneNumber;
import com.graduationProject.medicory.entity.usersEntities.Lab;
import com.graduationProject.medicory.entity.usersEntities.User;
import com.graduationProject.medicory.exception.ConflictException;
import com.graduationProject.medicory.exception.RecordNotFoundException;
import com.graduationProject.medicory.exception.UserDisabledException;
import com.graduationProject.medicory.mapper.usersMappers.LabMapper;
import com.graduationProject.medicory.mapper.usersMappers.UserMapper;
import com.graduationProject.medicory.model.users.lab.LabDTO;
import com.graduationProject.medicory.model.users.lab.LabRequestDTO;
import com.graduationProject.medicory.model.users.lab.LabResponseDTO;
import com.graduationProject.medicory.repository.usersRepositories.LabRepository;
import com.graduationProject.medicory.repository.phoneRepositories.UserPhoneNumberRepository;
import com.graduationProject.medicory.repository.usersRepositories.UserRepository;
import com.graduationProject.medicory.service.admin.users.AdminLabService;
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
public class AdminLabServiceImpl implements AdminLabService {
    private final LabRepository labRepo;
    private final UserRepository userRepo;
    private final LabMapper labMap;
    private final UserPhoneNumberRepository userPhoneRepo;
    private final UserMapper userMapper;


//TODO add logs


    @Override
    public LabDTO showAllLabDataById(Long labId) {
        if (labId > 0) {
            Lab lab = labRepo.findById(labId)
                    .orElseThrow(() -> new RecordNotFoundException("No lab with id " + labId));
            return labMap.toDTO(lab);
        }

        throw new RuntimeException("Invalid id" + labId);
    }

    @Override
    public LabResponseDTO findLabByEmail(String userEmail) {
        Lab lab = labRepo.findByUserEmail(userEmail)
                .orElseThrow(() -> new RecordNotFoundException("No user *LAB* with email " + userEmail));
        return labMap.toResponseDTO(lab);
    }

    @Override
    public LabResponseDTO findLabByUserCode(String userCode) {
        Lab lab = labRepo.findByUserCode(userCode)
                .orElseThrow(() -> new RecordNotFoundException("No lab with code " + userCode));
        return labMap.toResponseDTO(lab);
    }

    @Override
    public List<LabResponseDTO> findLabByName(String labName) {
        List<Lab> labs = labRepo.findByName(labName);
        if (!labs.isEmpty()) {
            return labs.stream()
                    .map(labMap::toResponseDTO)
                    .collect(Collectors.toList());
        }
        throw new RecordNotFoundException("No labs with name " + labName);
    }

    @Override
    public String addLab(LabRequestDTO newLab) {
        Lab lab = labMap.toRequestEntity(newLab);
        User newUser = lab.getUser();
        Optional<User> existingUser = userRepo.findByEmail(newUser.getEmail());
        if (!existingUser.isPresent()) {

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

            userRepo.save(newUser);
            labRepo.save(lab);
            userPhoneRepo.saveAll(userPhoneNumbers);
            return "Lab inserted successfully";
        }
        throw new ConflictException("The user email " + newUser.getEmail() + " already exists");
    }

    @Override
    public String updateLab(LabDTO updatedLab, Long labId) {
        log.info("Updating lab with id: {}", labId);
        if (labId > 0) {
            Lab oldLab = labRepo.findById(labId)
                    .orElseThrow(() -> new RecordNotFoundException("No lab with id " + labId));
            oldLab.setName(updatedLab.getName());
            oldLab.setGoogleMapsLink(updatedLab.getGoogleMapsLink());
            oldLab.setAddress(updatedLab.getAddress());
            oldLab.setOwnerName(updatedLab.getOwnerName());

            User oldUser = oldLab.getUser();

            oldUser.setEmail(updatedLab.getEmail());
            oldUser.setEnabled(updatedLab.isEnabled());
            oldUser.setPassword(updatedLab.getPassword());
            oldUser.setRole(updatedLab.getRole());
            oldUser.setUpdatedAt(LocalDateTime.now());
            oldLab.setUser(oldUser);

            // Update or add user phone numbers
            List<String> updatedPhoneNumbers = updatedLab.getUserPhoneNumbers();
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
            userRepo.save(oldUser);
            labRepo.save(oldLab);
            userPhoneRepo.saveAll(oldUserPhoneNumbers);
            log.info("Lab with id {} updated successfully", labId);
            return "Lab updated successfully";
        }
        log.error("Invalid lab id: {}", labId);
        throw new IllegalArgumentException("Invalid id: " + labId);
    }


    @Override
    public String deleteLab(Long labId) {
        if (labId > 0) {
            Lab lab = labRepo.findById(labId)
                    .orElseThrow(() -> new RecordNotFoundException("No lab with id " + labId));

            User unEnabledUser = lab.getUser();
            if (unEnabledUser.isEnabled()) {
                unEnabledUser.setEnabled(false);
                unEnabledUser.setUpdatedAt(LocalDateTime.now());

                userRepo.save(unEnabledUser);
                return "Deleted successfully";
            }
            throw new UserDisabledException("This user is unEnabled already");
        }
        throw new IllegalArgumentException("Invalid id: " + labId);
    }
}