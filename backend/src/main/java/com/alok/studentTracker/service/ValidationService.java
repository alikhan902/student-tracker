package com.alok.studentTracker.service;

import com.alok.studentTracker.entity.User;
import java.util.Optional;

public interface ValidationService {
    
    User validateHeadmanAccess(Long groupId);
}
