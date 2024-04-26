package com.sameh.medicory.controller.admin.users;

import com.sameh.medicory.model.users.lab.LabRequestDTO;
import com.sameh.medicory.model.users.lab.LabResponseDTO;
import com.sameh.medicory.service.admin.users.AdminLabService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/labs")
@RequiredArgsConstructor
public class AdminLabController {
    private final AdminLabService labService;

    @GetMapping("/id/{labId}")
    public ResponseEntity<LabResponseDTO> showAllLabDataById(@PathVariable Long labId) {
        LabResponseDTO lab = labService.showAllLabDataById(labId);
        return ResponseEntity.ok(lab);
    }

    @GetMapping("/name/{labName}")
    public ResponseEntity<List<LabResponseDTO>> findLabByName(@PathVariable String labName) {
        List<LabResponseDTO> labs = labService.findLabByName(labName);
        return ResponseEntity.ok(labs);
    }


    @GetMapping("/email/{userEmail}")
    public ResponseEntity<LabResponseDTO> findLabByEmail(@PathVariable String userEmail) {
        LabResponseDTO lab = labService.findLabByEmail(userEmail);
        return ResponseEntity.ok(lab);
    }

    @PostMapping("/lab")
    public ResponseEntity<String> addLab(@RequestBody LabRequestDTO newLab) {
        String message = labService.addLab(newLab);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PutMapping("/lab/{labId}")
    public ResponseEntity<String> updateLab(@RequestBody LabRequestDTO updatedLab, @PathVariable Long labId) {
        String message = labService.updateLab(updatedLab, labId);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/lab/{labId}")
    public ResponseEntity<String> deleteById(@PathVariable Long labId) {
        String message = labService.deleteLab(labId);
        return ResponseEntity.ok(message);
    }
}
