/*package com.java.ecoaula.service;

import com.java.ecoaula.entity.User;
import com.java.ecoaula.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    JavaMailSender mailSender;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "fromEmail", "no-reply@ecoaula.test");
    }

    @Test
    void send_sendsSimpleMailMessage() {
        emailService.send("ana@test.com", "mensaje");

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());
        verifyNoInteractions(userRepository);
        verifyNoMoreInteractions(mailSender);

        SimpleMailMessage sent = captor.getValue();
        assertEquals("EcoAula <no-reply@ecoaula.test>", sent.getFrom());
        assertArrayEquals(new String[]{"ana@test.com"}, sent.getTo());
        assertEquals("mensaje", sent.getText());
    }

    @Test
    void sendToAllUsers_whenNoUsers_doesNotSend() {
        when(userRepository.findAll()).thenReturn(List.of());

        emailService.sendToAllUsers("asunto", "contenido");

        verify(userRepository, times(1)).findAll();
        verifyNoInteractions(mailSender);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void sendToAllUsers_whenUsersExist_sendsToEach() {
        User u1 = new User();
        u1.setEmail("a@test.com");
        User u2 = new User();
        u2.setEmail("b@test.com");

        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        emailService.sendToAllUsers("asunto", "contenido");

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(userRepository, times(1)).findAll();
        verify(mailSender, times(2)).send(captor.capture());
        verifyNoMoreInteractions(userRepository, mailSender);

        Set<String> recipients = captor.getAllValues().stream()
                .map(m -> m.getTo()[0])
                .collect(Collectors.toSet());
        assertEquals(Set.of("a@test.com", "b@test.com"), recipients);
        assertTrue(captor.getAllValues().stream().allMatch(m -> "contenido".equals(m.getText())));
    }
}
*/