package com.graduationProject.medicory.entity.usersEntities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pharmacies")
public class Pharmacy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "google_maps_link", nullable = false)
    private String googleMapsLink;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String ownerName;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
