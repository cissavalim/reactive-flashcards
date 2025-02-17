package br.com.cissavalim.reactive_flashcards.domain.mapper;

import br.com.cissavalim.reactive_flashcards.domain.document.DeckDocument;
import br.com.cissavalim.reactive_flashcards.domain.document.UserDocument;
import br.com.cissavalim.reactive_flashcards.domain.dto.MailMessageDTO;
import br.com.cissavalim.reactive_flashcards.domain.dto.StudyDTO;
import jakarta.mail.MessagingException;
import org.mapstruct.*;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;

@Mapper(componentModel = "spring", injectionStrategy = CONSTRUCTOR)
@DecoratedWith(MailMapperDecorator.class)
public interface MailMapper {

    @Mapping(target = "username", source = "user.name")
    @Mapping(target = "destination", source = "user.email")
    @Mapping(target = "subject", constant = "Relatório de estudos")
    @Mapping(target = "questions", source = "study.questions")
    MailMessageDTO toDTO(final StudyDTO study, final DeckDocument deck, final UserDocument user);

    @Mapping(target = "to", expression = "java(new String[]{mailMessageDTO.destination()})")
    @Mapping(target = "from", source = "sender")
    @Mapping(target = "subject", source = "mailMessageDTO.subject")
    @Mapping(target = "fileTypeMap", ignore = true)
    @Mapping(target = "encodeFilenames", ignore = true)
    @Mapping(target = "validateAddresses", ignore = true)
    @Mapping(target = "replyTo", ignore = true)
    @Mapping(target = "cc", ignore = true)
    @Mapping(target = "bcc", ignore = true)
    @Mapping(target = "priority", ignore = true)
    @Mapping(target = "sentDate", ignore = true)
    @Mapping(target = "text", ignore = true)
    MimeMessageHelper toMimeMessageHelper(@MappingTarget final MimeMessageHelper helper, final MailMessageDTO mailMessageDTO,
                                          final String sender, final String body) throws MessagingException;

}
