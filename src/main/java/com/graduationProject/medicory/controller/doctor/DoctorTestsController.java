package com.graduationProject.medicory.controller.doctor;

import com.graduationProject.medicory.model.tests.ImagingTestRequestDTO;
import com.graduationProject.medicory.model.tests.ImagingTestResponseDTO;
import com.graduationProject.medicory.model.tests.LabTestRequestDTO;
import com.graduationProject.medicory.model.tests.LabTestResponseDTO;
import com.graduationProject.medicory.service.doctor.DoctorMedicalHistoryService;
import com.graduationProject.medicory.service.doctor.DoctorTestsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors/patients")
@RequiredArgsConstructor
public class DoctorTestsController {

    private final DoctorTestsService doctorTestsService;

    @GetMapping("/{userCode}/tests")
    public List<LabTestResponseDTO> getAllLabTestsForPatient(@PathVariable String userCode) {
        return doctorTestsService.findAllLabTestsForPatient(userCode);
    }

    @DeleteMapping("tests/{testId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String deleteLabTestFromHistory(@PathVariable(name = "testId") Long testId){
        return  doctorTestsService.deleteLabTestFromHistory(testId);
    }

    @PutMapping("tests/{testId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public boolean updateLabTest(@PathVariable(name = "testId") Long testId, @RequestBody LabTestRequestDTO labTestRequestDTO){
        return doctorTestsService.updateLabTest(testId, labTestRequestDTO);
    }

    @PostMapping("{userCode}/tests")
    @ResponseStatus(HttpStatus.CREATED)
    public boolean addLabTestsForPatientThatRequiredNow (@PathVariable String userCode,
                                                         @RequestBody List<LabTestRequestDTO> requiredTests) {
        return doctorTestsService.addLabTestsForPatientThatRequiredNow(userCode, requiredTests);
    }

    @GetMapping("{userCode}/active-tests")
    public List<LabTestResponseDTO> getActiveLabTests (@PathVariable String userCode) {
        return doctorTestsService.getActiveLabTests(userCode);
    }

    @GetMapping("tests")
    public LabTestResponseDTO findLabTestById (@RequestParam Long testId) {
        return doctorTestsService.findLabTestById(testId);
    }




    @GetMapping("/{userCode}/imaging-tests")
    public List<ImagingTestResponseDTO> getAllImagingTestForPatient(@PathVariable String userCode) {
        return doctorTestsService.getAllImagingTestForPatient(userCode);
    }

    @DeleteMapping("imaging-tests/{testId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public boolean deleteImagingTestFromHistory(@PathVariable(name = "testId") Long testId){
        return doctorTestsService.deleteImagingTestFromHistory(testId);
    }

    @PutMapping("imaging-tests/{testId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public boolean updateImagingTest(@PathVariable(name = "testId") Long testId, @RequestBody ImagingTestRequestDTO imagingTestRequestDTO){
        return doctorTestsService.updateImagingTest(testId, imagingTestRequestDTO);
    }

    @PostMapping("{userCode}/imaging-tests")
    @ResponseStatus(HttpStatus.CREATED)
    public boolean addImagingTestForPatientThatRequiredNow (@PathVariable String userCode,
                                                         @RequestBody List<ImagingTestRequestDTO> requiredTests) {
        return doctorTestsService.addImagingTestForPatientThatRequiredNow(userCode, requiredTests);
    }

    @GetMapping("{userCode}/active-imaging-tests")
    public List<ImagingTestResponseDTO> getActiveImagingTest (@PathVariable String userCode) {
        return doctorTestsService.getActiveImagingTest(userCode);
    }

    @GetMapping("imaging-tests")
    public ImagingTestResponseDTO findImagingTestById (@RequestParam Long testId) {
        return doctorTestsService.findImagingTestById(testId);
    }

}