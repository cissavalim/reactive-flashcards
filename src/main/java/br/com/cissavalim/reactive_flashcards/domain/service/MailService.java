package br.com.cissavalim.reactive_flashcards.domain.service;

import br.com.cissavalim.reactive_flashcards.domain.dto.MailMessageDTO;
import br.com.cissavalim.reactive_flashcards.domain.helper.RetryHelper;
import br.com.cissavalim.reactive_flashcards.domain.mapper.MailMapper;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
public class MailService {

    private final RetryHelper retryHelper;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final MailMapper mailMapper;
    private final String sender;

    public MailService(final RetryHelper retryHelper, JavaMailSender mailSender, TemplateEngine templateEngine, MailMapper mailMapper,
                       @Value("${reactive-flashcards.mail.sender}") String sender) {
        this.retryHelper = retryHelper;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.mailMapper = mailMapper;
        this.sender = sender;
    }

    public Mono<Void> send(final MailMessageDTO mailMessageDTO) {
        return Mono.just(mailSender.createMimeMessage())
                .flatMap(mimeMessage -> buildMessage(mimeMessage, mailMessageDTO))
                .flatMap(this::send)
                .then();
    }

    private Mono<MimeMessage> buildMessage(final MimeMessage mimeMessage, final MailMessageDTO mailMessageDTO) {
        return Mono.fromCallable(() -> {
            var helper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());
            mailMapper.toMimeMessageHelper(
                    helper, mailMessageDTO, sender, buildTemplate(mailMessageDTO.template(), mailMessageDTO.variables()));
            return mimeMessage;
        });
    }

    private String buildTemplate(final String template, final Map<String, Object> variables) {
        var context = new Context(Locale.of("pt_BR"));
        context.setVariables(variables);
        return templateEngine.process(template, context);
    }

    private Mono<Void> send(final MimeMessage mimeMessage) {
        return Mono.fromCallable(() -> {
                    mailSender.send(mimeMessage);
                    return mimeMessage;
                }).retryWhen(retryHelper.processRetry(UUID.randomUUID().toString(), throwable -> throwable instanceof MailException))
                .then();
    }
}
