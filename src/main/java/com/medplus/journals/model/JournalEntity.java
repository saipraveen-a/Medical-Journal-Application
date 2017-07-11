package com.medplus.journals.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "journals")
public class JournalEntity {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Date publishDate;

  @Column(nullable = false)
  private boolean notified;

  @ManyToOne(optional = false)
  @JoinColumn(name = "publisher_id")
  private PublisherEntity publisher;

  @Column(nullable = false)
  private String uuid; //external id

  @ManyToOne(optional = false)
  @JoinColumn(name = "category_id")
  private CategoryEntity category;

  @PrePersist
  void onPersist() {
    this.publishDate = new Date();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public PublisherEntity getPublisher() {
    return publisher;
  }

  public void setPublisher(PublisherEntity publisher) {
    this.publisher = publisher;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public CategoryEntity getCategory() {
    return category;
  }

  public void setCategory(CategoryEntity category) {
    this.category = category;
  }
}
