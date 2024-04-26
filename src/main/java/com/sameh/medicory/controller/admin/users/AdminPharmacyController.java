package com.sameh.medicory.controller.admin.users;

import com.sameh.medicory.model.users.pharmacy.PharmacyRequestDTO;
import com.sameh.medicory.model.users.pharmacy.PharmacyResponseDTO;
import com.sameh.medicory.service.admin.users.AdminPharmacyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/pharmacies")
@RequiredArgsConstructor
public class AdminPharmacyController {

    private final AdminPharmacyService pharmacyService;


    @GetMapping("/id/{pharmacyId}")
    public ResponseEntity<PharmacyRequestDTO> showAllDataOfPharmacyById(@PathVariable Long pharmacyId) {
        PharmacyRequestDTO pharmacy = pharmacyService.showAllDataOfPharmacyById(pharmacyId);
        return ResponseEntity.ok(pharmacy);
    }

    @GetMapping("/name/{pharmacyName}")
    public ResponseEntity<List<PharmacyResponseDTO>> findPharmacyByName(@PathVariable String pharmacyName) {
        List<PharmacyResponseDTO> pharmacies = pharmacyService.findPharmacyByName(pharmacyName);
        return ResponseEntity.ok(pharmacies);
    }

    @GetMapping("code/{code}")
    public ResponseEntity<PharmacyResponseDTO> findPharmacyByCode(@PathVariable String code){
        PharmacyResponseDTO pharmacy = pharmacyService.findPharmacyByUserCode(code);
        return new ResponseEntity<>(pharmacy,HttpStatus.OK);
    }

    @GetMapping("/email/{userEmail}")
    public ResponseEntity<PharmacyResponseDTO> findPharmacyByEmail(@PathVariable String userEmail) {
        PharmacyResponseDTO pharmacy = pharmacyService.findPharmacyByUserEmail(userEmail);
        return ResponseEntity.ok(pharmacy);
    }

    @PostMapping("/pharmacy")
    public ResponseEntity<String> addPharmacy(@RequestBody PharmacyRequestDTO newPharmacy) {
        String message = pharmacyService.addPharmacy(newPharmacy);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }


    @PutMapping("/pharmacy/{pharmacyId}")
    public ResponseEntity<String> updatePharmacy(@RequestBody PharmacyRequestDTO updatedPharmacy, @PathVariable Long pharmacyId) {
        String message = pharmacyService.updatePharmacy(updatedPharmacy, pharmacyId);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/pharmacy/{pharmacyId}")
    public ResponseEntity<String> deletePharmacyById(@PathVariable Long pharmacyId) {
        String message = pharmacyService.deletePharmacy(pharmacyId);
        return ResponseEntity.ok(message);
    }
}