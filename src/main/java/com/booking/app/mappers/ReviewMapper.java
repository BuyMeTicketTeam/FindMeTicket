package com.booking.app.mappers;

import com.booking.app.dto.users.CreateReviewDto;
import com.booking.app.entities.user.Review;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReviewMapper {
    Review toEntity(CreateReviewDto dto);

    Review toDto(Review dto);
}
