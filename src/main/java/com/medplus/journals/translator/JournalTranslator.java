package com.medplus.journals.translator;

import com.medplus.journals.dto.JournalDTO;
import com.medplus.journals.model.JournalEntity;
import org.springframework.stereotype.Component;

@Component
public class JournalTranslator extends BaseTranslator<JournalEntity, JournalDTO> {

  @Override
  public JournalDTO translateTo(JournalEntity from) {
    JournalDTO journalDto = new JournalDTO
        .Builder()
        .withId(from.getId())
        .withCategory(from.getCategory().getName())
        .withName(from.getName())
        .withPublisher(from.getPublisher().getName())
        .withNotified(from.isNotified())
        .withPublishDate(from.getPublishDate())
        .build();
    return journalDto;
  }
}
