package com.medplus.journals.dto;

import java.util.Date;

public final class JournalDTO {
  private String name;
  private String publisher;
  private String category;
  private Date publishDate;
  private boolean notified;
  private long id;

  private JournalDTO(long id, String name, String publisher, String category, Date publishDate, boolean notified) {
    this.id = id;
    this.name = name;
    this.publisher = publisher;
    this.category = category;
    this.publishDate = publishDate;
    this.notified = notified;
  }

  public String getName() {
    return name;
  }

  public String getPublisher() {
    return publisher;
  }

  public String getCategory() {
    return category;
  }

  public Date getPublishDate() {
    return publishDate;
  }

  public boolean isNotified() {
    return notified;
  }

  public void setNotified(boolean notified) {
    this.notified = notified;
  }

  public long getId() {
    return id;
  }

  public static final class Builder {
    private String name;
    private String publisher;
    private String category;
    private Date publishDate;
    private boolean notified;
    private long id;

    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public Builder withPublisher(String publisher) {
      this.publisher = publisher;
      return this;
    }

    public Builder withCategory(String category) {
      this.category = category;
      return this;
    }

    public Builder withPublishDate(Date publishDate) {
      this.publishDate = publishDate;
      return this;
    }

    public Builder withNotified(boolean notified) {
      this.notified = notified;
      return this;
    }

    public Builder withId(long id) {
      this.id = id;
      return this;
    }

    public JournalDTO build() {
      return new JournalDTO(id, name, publisher, category, publishDate, notified);
    }
  }
}
