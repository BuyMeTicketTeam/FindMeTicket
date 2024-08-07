package com.booking.app.entities.user;

import com.booking.app.constants.TransportType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "departure_city")
    private Long departureCityId;

    @Column(name = "arrival_city")
    private Long arrivalCityId;

    @Column(name = "departure_date")
    private LocalDate departureDate;

    @Column(name = "adding_time")
    @Builder.Default
    private LocalDateTime addingTime = LocalDateTime.now();

    @Column(name = "type", columnDefinition = "transport[]")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.ARRAY)
    private TransportType[] typeTransport;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
