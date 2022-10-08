package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.TrainingPlan;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.repositories.TrainingPlanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingPlanServiceTest {

    @InjectMocks
    TrainingPlanService trainingPlanService;

    @Mock
    TrainingPlanRepository trainingPlanRepository;

    @Mock
    SecurityContext context;
    @Mock
    Authentication authentication;

    @Test
    void findAll() {
        TrainingPlan trainingPlan1 = new TrainingPlan();
        TrainingPlan trainingPlan2 = new TrainingPlan();
        Set<TrainingPlan> trainingPlans = new HashSet<>();
        trainingPlans.add(trainingPlan1);
        trainingPlans.add(trainingPlan2);

        when(trainingPlanRepository.findAll()).thenReturn(trainingPlans);

        Set<TrainingPlan> trainingPlansAll = trainingPlanService.findAll();

        assertNotNull(trainingPlansAll);
        assertEquals(2, trainingPlansAll.size());

        verify(trainingPlanRepository, times(1)).findAll();
    }

    @Test
    void findById() {
        when(trainingPlanRepository.findById(anyLong())).thenReturn(Optional.of(new TrainingPlan()));

        TrainingPlan trainingPlan = trainingPlanService.findById(1L);

        assertNotNull(trainingPlan);
        verify(trainingPlanRepository).findById(anyLong());
    }

    @Test
    void save() {
        when(trainingPlanRepository.save(any())).thenReturn(new TrainingPlan());

        TrainingPlan trainingPlan = trainingPlanService.save(new TrainingPlan());

        assertNotNull(trainingPlan);
        verify(trainingPlanRepository).save(any());
    }

    @Test
    void deleteById() {
        TrainingPlan trainingPlan = new TrainingPlan();
        User user = new User();
        user.setUsername("test");
        trainingPlan.setCoach(user);

        when(context.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test");
        SecurityContextHolder.setContext(context);

        when(trainingPlanRepository.findById(anyLong())).thenReturn(Optional.of(trainingPlan));
        trainingPlanService.deleteById(1L);

        verify(trainingPlanRepository).deleteById(anyLong());
    }

    @Test
    void findAllByUser() {
        TrainingPlan trainingPlan1 = new TrainingPlan();
        TrainingPlan trainingPlan2 = new TrainingPlan();
        Set<TrainingPlan> trainingPlansByUser = new HashSet<>();
        trainingPlansByUser.add(trainingPlan1);
        trainingPlansByUser.add(trainingPlan2);

        when(trainingPlanRepository.findAllByCoach(notNull())).thenReturn(trainingPlansByUser);

        Set<TrainingPlan> trainingPlans = trainingPlanService.findAllByUser(new User());

        assertEquals(2, trainingPlans.size());
        verify(trainingPlanRepository).findAllByCoach(any());
    }
}