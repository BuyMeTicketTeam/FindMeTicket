package com.booking.app.entity;

import com.booking.app.enums.EnumRole;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "role")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Role{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private EnumRole enumRole;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private Set<User> users;

}
