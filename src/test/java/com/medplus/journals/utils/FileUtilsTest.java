package com.medplus.journals.utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.medplus.journals.Application;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.UUID;

public class FileUtilsTest {

  @Test
  public void getFileName_givenPublisherIdAndUuid_thenReturnsFileName() {
    String uuid = UUID.randomUUID().toString();
    long publisherId = 1;
    String fileName = FileUtils.getFileName(publisherId, uuid);

    assertThat(fileName, is(equalTo(Application.ROOT + "/" + publisherId + "/" + uuid + ".pdf")));
  }

  @Test
  public void createDirectoryIfNotExists_givenDirectoryDoesntExist_thenDirectoryIsCreated() {
    File dir = new File("./uploads/test");

    FileUtils.createDirectoryIfNotExist(dir);

    assertThat(dir.exists(), is(true));
    dir.delete();
  }
}
