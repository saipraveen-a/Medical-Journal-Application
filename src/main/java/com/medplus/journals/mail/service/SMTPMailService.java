package com.medplus.journals.mail.service;

import com.medplus.journals.mail.Mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class SMTPMailService implements MailService {
  private static final Logger LOGGER = LoggerFactory.getLogger(SMTPMailService.class);
  private final JavaMailSender mailSender;

  @Autowired
  public SMTPMailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Override
  public void sendEmail(Mail mail) {
    MimeMessage mimeMessage = mailSender.createMimeMessage();

    try {
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

      mimeMessageHelper.setSubject(mail.getMailSubject());
      mimeMessageHelper.setFrom(mail.getMailFrom());
      for (String address : mail.getMailTo()) {
        mimeMessageHelper.addTo(address);
      }
      mimeMessageHelper.setText(mail.getMailContent(), true);

      mailSender.send(mimeMessageHelper.getMimeMessage());
    } catch (MessagingException e) {
      LOGGER.error("Could not send mail", e);
    }
  }
}
