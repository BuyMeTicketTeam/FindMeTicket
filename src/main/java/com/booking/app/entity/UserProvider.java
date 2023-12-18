package com.booking.app.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "providers")
public class UserProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "provider_id")
    private Integer id;

    @Column(name = "provider_name")
    private String name;

    private String email;

    private String userIdByProvider;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIdentityInfo(
            property = "id",
            generator = ObjectIdGenerators.PropertyGenerator.class)
    private UserSecurity user;
}
