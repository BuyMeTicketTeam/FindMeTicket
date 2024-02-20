package com.booking.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("TRAIN")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@SuperBuilder
public class TrainTicket extends Ticket {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "train_info", joinColumns = @JoinColumn(name = "train_ticket_id"))
    @Column(name = "train_info")
    List<TrainComfortInfo> infoList = new ArrayList<>();

}
