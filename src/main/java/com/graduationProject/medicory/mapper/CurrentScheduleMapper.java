package com.graduationProject.medicory.mapper;

import com.graduationProject.medicory.entity.medicationEntities.CurrentSchedule;
import com.graduationProject.medicory.model.medication.CurrentScheduleRequest;

public interface CurrentScheduleMapper {
    CurrentSchedule toEntity(CurrentScheduleRequest currentScheduleRequest);
}
