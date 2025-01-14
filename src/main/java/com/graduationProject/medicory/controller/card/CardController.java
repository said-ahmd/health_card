package com.graduationProject.medicory.controller.card;

import com.graduationProject.medicory.entity.enums.Role;
import com.graduationProject.medicory.service.card.CardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("card")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping("/scan")
    public String scanCard(@RequestParam("userCode") String userCode) {
        return userCode;
    }

    @Operation(summary = "get interacting role based on user code")
    @PostMapping("/interacting-role/{userCode}")
    public ResponseEntity<Role> getInteractingRoleBasedOnCard(@PathVariable String userCode) {
        Role interactingRole = cardService.getInteractingRoleBasedOnCard(userCode);
        return ResponseEntity.ok(interactingRole);
    }
}
